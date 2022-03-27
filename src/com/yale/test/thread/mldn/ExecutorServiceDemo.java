package com.yale.test.thread.mldn;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.yale.test.thread.heima.zhangxiaoxiang.UserThreadFactory;

/**
 * Java语言虽然内置了多线程支持，启动一个新线程非常方便，但是，创建线程需要操作系统资源（线程资源，栈空间等），频繁创建和销毁大量线程需要消耗大量时间
 * 如果可以复用一组线程：那么我们就可以把很多小任务让一组线程来执行，而不是一个任务对应一个新线程。这种能接收大量小任务并进行分发处理的就是线程池。
 * 简单地说，线程池内部维护了若干个线程，没有任务的时候，这些线程都处于等待状态。如果有新任务，就分配一个空闲线程执行。如果所有线程都处于忙碌状态，新任务要么放入队列等待，要么增加一个新线程进行处理。
 * 从JDK1.5之后追加了一个并发访问程序包java.util.concurrent
 * 普通的执行线程池定义:java.util.concurrent Interface ExecutorService
 * ExecutorService的JAVA官方文档里面使用例子,最好去看一下java官方的例子:https://docs.oracle.com/javase/8/docs/api/index.html
 * 调度线程池(可以定时执行的线程池):ava.util.concurrent Interface ScheduledExecutorService
 * 那么如果要进行线程池的创建,一般可以使用java.util.concurrent.Executors类完成,有如下几个方法:
 * 创建无大小限制的线程池:
 * 	public static ExecutorService newCachedThreadPool(),线程数根据任务动态调整的线程池
 * 	public static ExecutorService newCachedThreadPool(ThreadFactory threadFactory)
 * 创建固定大小的线程池:
 * 		public static ExecutorService newFixedThreadPool(int nThreads)
 * 		public static ExecutorService newFixedThreadPool(int nThreads, ThreadFactory threadFactory)
 * 创建单线程池：
 * 	public static ExecutorService newSingleThreadExecutor()仅单线程执行的线程池。
 * 	public static ExecutorService newSingleThreadExecutor(ThreadFactory threadFactory)
 * 创建定时调度池:
 * 	public static ScheduledExecutorService newScheduledThreadPool(int corePoolSize)
 * 	public static ScheduledExecutorService newScheduledThreadPool(int corePoolSize, ThreadFactory threadFactory)
 * 【强制】线程资源必须通过线程池提供，不允许在应用中自行显式创建线程。 
 * 说明：线程池的好处是减少在创建和销毁线程上所消耗的时间以及系统资源的开销，解决资源不足的问题。如果不使用线程池，有可能造成系统创建大量同类线程而导致消耗完内存或者“过度切换”的问题。
 * 【强制】线程池不允许使用Executors去创建，而是通过ThreadPoolExecutor的方式，这样的处理方式让写的同学更加明确线程池的运行规则，规避资源耗尽的风险。
 *  说明：Executors返回的线程池对象的弊端如下：
 *  	 1） FixedThreadPool和SingleThreadPool： 允许的请求队列长度为Integer.MAX_VALUE，可能会堆积大量的请求，从而导致OOM。
 *		 2） CachedThreadPool： 允许的创建线程数量为Integer.MAX_VALUE，可能会创建大量的线程，从而导致OOM。
 * 《阿里巴巴Java开发手册（泰山版）.pdf》
 * 如果优雅的使用线程池:https://crossoverjie.top/2018/07/29/java-senior/ThreadPool/
 * 一个公平的洗牌算法 :https://imoegirl.com/2019/12/30/algo-knuth-shuffle/
 * https://www.zhihu.com/question/51244545/answer/126055789
 *  http://hg.openjdk.java.net/jdk8u/jdk8u/jdk/file/tip/src/share/classes/java/util/concurrent/Executors.java#l677 《 Executors$DelegatedExecutorService.submit() 》
 * @author dell
 *
 */
public class ExecutorServiceDemo {
	public static void main(String[] args) {
		//newCachedThreadPool创建无大小限制的线程池
		/**
		 * Executors.newCachedThreadPool的源码实际上是通过new ThreadPoolExecutor创建的线程池
		 * newCachedThreadPool线程数根据任务动态调整的线程池
		 */
		ExecutorService executorService = Executors.newCachedThreadPool(new UserThreadFactory("创建线程池时,自己设置线程的名字"));
		for (int i=0; i<10; i++) {
			int index = i;
			executorService.submit(()->{
				System.out.println("注意看线程池的名字,可以看到创建了好多个线程" + Thread.currentThread().getName() + ",index = " + index);
			});
		}
		executorService.shutdown();//当线程池里面的所有任务都执行完时,线程处于空闲状态时,关闭线程池
		
		ExecutorService executorServiceFuture = Executors.newCachedThreadPool(new UserThreadFactory("创建线程池时,自己设置线程的名字"));
		Future future1 = executorServiceFuture.submit(()->{
			System.out.println("注意看线程池的名字,可以看到创建了好多个线程" + Thread.currentThread().getName() + ",index = ");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		
		Future future2= executorServiceFuture.submit(()->{
			System.out.println("注意看线程池的名字,可以看到创建了好多个线程" + Thread.currentThread().getName() + ",index = ");
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		
		try {
			future1.get(10 , TimeUnit.SECONDS );
			boolean future1Done = future1.isDone(); 
			
			future2.get(30 , TimeUnit.SECONDS);
			boolean future2Done = future2.isDone();
			System.out.println("第一个线程跑完了:" + future1Done);
			System.out.println("第二个线程跑完了:" + future2Done + "这个肯定要比第一个慢2s,当线程没有执行完毕时,线程会阻塞在这里");
		} catch (InterruptedException | ExecutionException | TimeoutException e1) {
			e1.printStackTrace();
			 if (future1 != null) {//发生异常时取消这个线程的执行
				 future1.cancel(true);
			 }
			 if (future1 != null) {
				 future2.cancel(true);
			 }
		} 
		executorServiceFuture.shutdown();//当线程池里面的所有任务都执行完时,线程处于空闲状态时,关闭线程池
		/*
		 * 线程池在程序结束的时候要关闭。
		 * 使用shutdown()方法关闭线程池的时候，它会等待正在执行的任务先完成，然后再关闭。
		 * shutdownNow()会立刻停止正在执行的任务，
		 * awaitTermination()则会等待指定的时间让线程池关闭。
		 */
		
		System.out.println("**************");
		
		ExecutorService es = Executors.newCachedThreadPool();//newCachedThreadPool
		for (int i=0; i<10; i++) {
			try {
				/**
				 * 我在这里让线程休眠一秒,这个时候线程池里面的第一个线程肯定把活干完了,
				 * 那么线程池就不会重新再创建一个新的线程出来干活了,还会沿用上一个线程.
				 * 结果就会看到的就是只会创建一个线程
				 */
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			int index = i;
			es.submit(()->{
				System.out.println("注意看线程池的名字,可以看到这次值创建了一个线程" + Thread.currentThread().getName() + ",index = " + index);
			});
		}
		es.shutdown();
		System.out.println("**************");

		/**
		 * newSingleThreadExecutor创建单线程池,永远就只有一个线程在干活,线程死掉会重新创建新的线程
		 */
		ExecutorService single = Executors.newSingleThreadExecutor();
		for (int i=0; i<10; i++) {
			int index = i;
			single.submit(()->{
				if (index == 3) {
					System.out.println("注意看这里实际上会发生异常,让当前线程死掉,然后newSingleThreadExecutor会重新创建一个线程来执行任务");
					int sd = 1/0;
					System.out.println(sd);
				}
				System.out.println("newSingleThreadExecutor可以看到这次值创建了一个线程" + Thread.currentThread().getName() + ",index = " + index);
			});
		}
		single.shutdown();
		
		System.out.println("------------------------------------------------------");

		
		ExecutorService fixed = Executors.newFixedThreadPool(3);
		for (int i=0; i<10; i++) {
			int index = i;
			fixed.submit(()->{
				if (index == 3) {
					System.out.println("注意看这里实际上会发生异常,让当前线程死掉,然后newFixedThreadPool会重新创建一个线程来执行任务");
					int sd = 1/0;
					System.out.println(sd);
				}
				System.out.println("newFixedThreadPool可以看到最多有三个线程" + Thread.currentThread().getName() + ",index = " + index);
			});
		}
		fixed.shutdown();
		
		System.out.println("*===================================================*");
		
		/*
		 * 如果我们想把线程池的大小限制在4～10个之间动态调整怎么办？我们查看Executors.newCachedThreadPool()方法的源码：
		 * public static ExecutorService newCachedThreadPool() {
			    return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
			                                    60L, TimeUnit.SECONDS,
			                                    new SynchronousQueue<Runnable>());
			}
		 * 因此，想创建指定动态范围的线程池，可以这么写：
		 */
		int min = 4;
		int max = 10;
		ExecutorService esDiy = new ThreadPoolExecutor(min, max, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
		
		
		System.out.println("*===================================================*");
		
		ScheduledExecutorService schedule = Executors.newScheduledThreadPool(1);
		schedule.scheduleAtFixedRate(()->{
			System.out.println("newScheduledThreadPool定时调度线程池,启动3秒后执行,然后每隔2秒后执行一次");
			System.out.println("newScheduledThreadPool定时调度线程池,不能调用schedule.shutdown()方法");
		}, 3, 2, TimeUnit.SECONDS);
		//schedule.shutdown();
	}
}
