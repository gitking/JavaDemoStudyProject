package com.yale.test.run.demo;

/*
 * https://juejin.cn/post/6844904004569464840#comment 一个JDK线程池BUG引发的GC机制思考
 * 本类一定要结合com.yale.test.thread.heima.zhangxiaoxiang.ThreadPoolBugTest.java一起观看.这俩个类是在说同一个问题.
 * 下面来验证一下猜想：
 */
public class FinalizedTest {

	public TFutureTask submit() {
		TExecutorService texecutorService = Executors.create();
		texecutorService.execute();
		return null;
	}
	
	/*
	 * 执行若干时间后报错：
	 * Exception in thread "Thread-1" Exception in thread "Thread-5" Exception in thread "Thread-3" Exception in thread "Thread-4" Exception in thread "Thread-6" Exception in thread "Thread-0" Exception in thread "Thread-7" java.lang.RuntimeException: reject!!![true]
	 * java.lang.RuntimeException: reject!!![true]
	 * 从错误上来看，“线程池”同样被提前shutdown了，那么一定是由于新建线程导致的吗？
	 * 下面将新建线程修改为Thread.sleep测试一下：
	 * //TThreadPoolExecutor.java，修改后的execute方法
	 * 执行结果一样是报错
	 * Exception in thread "Thread-3" java.lang.RuntimeException: reject!!![true]
	 * 由此可得，如果在执行的过程中，发生一次显式的线程切换，则会让编译器/代码生成器认为外层包装对象不可达
	 * 总结
	 * 虽然GC只会回收不可达GC ROOT的对象，但是在编译器（没有明确指出，也可能是JIT）/代码生成器的优化下，可能会出现对象提前置null，或者线程切换导致的“提前对象不可达”的情况。
		所以如果想在finalize方法里做些事情的话，一定在最后显示的引用一下对象（toString/hashcode都可以），保持对象的可达性（reachable）
		上面关于线程切换导致的对象不可达，没有官方文献的支持，只是个人一个测试结果，如有问题欢迎指出
		综上所述，这种回收机制并不是JDK的bug，而算是一个优化策略，提前回收而已；但Executors.newSingleThreadExecutor的实现里通过finalize来自动关闭线程池的做法是有Bug的，在经过优化后可能会导致线程池的提前shutdown，从而导致异常。
		线程池的这个问题，在JDK的论坛里也是一个公开但未解决状态的问题
	 * https://bugs.openjdk.java.net/browse/JDK-8145304
	 * 不过在JDK11下，该问题已经被修复：
	 * JUC  Executors.FinalizableDelegatedExecutorService
		public void execute(Runnable command) {
		    try {
		        e.execute(command);
		    } finally { reachabilityFence(this); }
		}
	 * https://juejin.cn/post/6844904004569464840 一个JDK线程池BUG引发的GC机制思考
	 */
	public static void main(String[] args) {
		final FinalizedTest finalizedTest = new FinalizedTest();
		for (int i=0; i<8; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					while(true) {
						TFutureTask future = finalizedTest.submit();
					}
				}
			}).start();
		}
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					System.gc();
				}
			}
		}).start();
	}
}
