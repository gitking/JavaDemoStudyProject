package com.yale.test.thread.heima.zhangxiaoxiang;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/*
 * https://juejin.cn/post/6844904004569464840#comment 一个JDK线程池BUG引发的GC机制思考
 * https://club.perfma.com/article/2462327  一个 println 竟然比 volatile 还好使？ 大神 空无H 的这篇文章彻底解决了我所有的疑问。
 * 本类可以结合com.yale.test.java.fanshe.perfma.VolatileDemo.java和com.yale.test.java.fanshe.perfma.JitFinalize.java一起看
 * 当对象仍存在于作用域（stack frame）时，finalize也可能会被执行
 * oracle jdk文档中有一段关于finalize的介绍：
 * A reachable object is any object that can be accessed in any potential continuing computation from any live thread.
 * Optimizing transformations of a program can be designed that reduce the number of objects that are reachable to be less than those which would naively be considered reachable. For example, a Java compiler or code generator may choose to set a variable or parameter that will no longer be used to null to cause the storage for such an object to be potentially reclaimable sooner.
 * 大概意思是：可达对象(reachable object)是可以从任何活动线程的任何潜在的持续访问中的任何对象；java编译器或代码生成器可能会对不再访问的对象提前置为null，使得对象可以被提前回收
 * 也就是说，在jvm的优化下，可能会出现对象不可达之后被提前置空并回收的情况
 * JDK官方文档:https://docs.oracle.com/javase/specs/jls/se8/html/jls-12.html#jls-12.6.1
 * JDK官方文档:https://docs.oracle.com/javase/specs/jls/se8/html/jls-6.html#jls-6.3
 * 线程池的这个问题，在JDK的论坛里也是一个公开但未解决状态的问题 https://bugs.openjdk.java.net/browse/JDK-8145304 不过在JDK11下，该问题已经被修复：
 * https://stackoverflow.com/questions/58714980/rejectedexecutionexception-inside-single-executor-service
 */
public class ThreadPoolBugTest {
	/**
	 * 异步执行任务
	 * @return
	 */
	public Future<String> submit() {
		//关键点,通过Executors.newSingleThreadExecutor()创建一个单线程的线程池
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		FutureTask<String> futureTask = new FutureTask(new Callable() {
			@Override
			public Object call() throws Exception {
				Thread.sleep(50);//把这里的50改成5000就不会报错了
				return System.currentTimeMillis() + "";
			}
		});
		executorService.execute(futureTask);
		return futureTask;
	}
	
	/*
	 * 这个程序运行一段时间之后会报错,如下:
	 * Exception in thread "Thread-6" Exception in thread "Thread-0" java.util.concurrent.RejectedExecutionException: Task java.util.concurrent.FutureTask@3e666490 rejected from java.util.concurrent.ThreadPoolExecutor@46653985[Terminated, pool size = 0, active threads = 0, queued tasks = 0, completed tasks = 0]
	 * 为什么会报这个错误？
	 * 现在再回到上面的线程池问题，根据上面介绍的机制，在分析没有引用之后，对象会被提前finalize
	 * 可在上述代码中，return之前明明是有引用的executorService.execute(futureTask)，为什么也会提前finalize呢？
	 * 猜测可能是由于在execute方法中，会调用threadPoolExecutor，会创建并启动一个新线程，这时会发生一次主动的线程切换，导致在活动线程中对象不可达,注意是活动线程不可达，活动线程不可达。
	 * 结合上面Oracle Jdk文档中的描述“可达对象(reachable object)是可以从任何活动线程的任何潜在的持续访问中的任何对象”，可以认为可能是因为一次显示的线程切换，对象被认为不可达了，导致线程池被提前finalize了
	 */
	public static void main(String[] args) {
		final ThreadPoolBugTest threadPoolTest = new ThreadPoolBugTest();
		for (int i=0; i<8; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					while(true) {
						Future<String> future = threadPoolTest.submit();
						try {
							String s = future.get();
							System.out.println("线程ID:" + Thread.currentThread().getId() + ",线程返回的结果为:" + Thread.currentThread().getName() + "结果为:" + s);
						} catch (InterruptedException | ExecutionException e) {
							e.printStackTrace();
						}
					}
				}
			}).start();
		}
		
		new Thread(new Runnable() {//子线程不停的GC,模拟生产环境偶发的GC
			@Override
			public void run() {
				while(true) {
					System.gc();
				}
			}
		}).start();
	}
}
