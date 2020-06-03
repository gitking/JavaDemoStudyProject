package com.yale.test.thread.mldn;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 从JDK1.5之后追加了一个并发访问程序包java.util.concurrent
 * 普通的执行线程池定义:java.util.concurrent Interface ExecutorService
 * ExecutorService的JAVA官方文档里面使用例子,最好去看一下java官方的例子:https://docs.oracle.com/javase/8/docs/api/index.html
 * 调度线程池(可以定时执行的线程池):ava.util.concurrent Interface ScheduledExecutorService
 * 那么如果要进行线程池的创建,一般可以使用java.util.concurrent.Executors类完成,有如下几个方法:
 * 创建无大小限制的线程池:
 * 	public static ExecutorService newCachedThreadPool(),
 * 	public static ExecutorService newCachedThreadPool(ThreadFactory threadFactory)
 * 创建固定大小的线程池:
 * 		public static ExecutorService newFixedThreadPool(int nThreads)
 * 		public static ExecutorService newFixedThreadPool(int nThreads, ThreadFactory threadFactory)
 * 创建单线程池：
 * 	public static ExecutorService newSingleThreadExecutor()
 * 	public static ExecutorService newSingleThreadExecutor(ThreadFactory threadFactory)
 * 创建定时调度池:
 * 	public static ScheduledExecutorService newScheduledThreadPool(int corePoolSize)
 * 	public static ScheduledExecutorService newScheduledThreadPool(int corePoolSize, ThreadFactory threadFactory)
 * @author dell
 *
 */
public class ExecutorServiceDemo {
	public static void main(String[] args) {
		//newCachedThreadPool创建无大小限制的线程池
		ExecutorService executorService = Executors.newCachedThreadPool();
		for (int i=0; i<10; i++) {
			int index = i;
			executorService.submit(()->{
				System.out.println("注意看线程池的名字,可以看到创建了好多个线程" + Thread.currentThread().getName() + ",index = " + index);
			});
		}
		executorService.shutdown();//当线程池里面的所有任务都执行完时,线程处于空闲状态时,关闭线程池
		
		System.out.println("**************");
		
		ExecutorService es = Executors.newCachedThreadPool();
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
		
		ScheduledExecutorService schedule = Executors.newScheduledThreadPool(1);
		schedule.scheduleAtFixedRate(()->{
			System.out.println("newScheduledThreadPool定时调度线程池,启动3秒后执行,然后每隔2秒后执行一次");
			System.out.println("newScheduledThreadPool定时调度线程池,不能调用schedule.shutdown()方法");
		}, 3, 2, TimeUnit.SECONDS);
		//schedule.shutdown();
	}
}
