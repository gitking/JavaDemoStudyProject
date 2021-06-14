package com.yale.test.thread.heima.zhangxiaoxiang;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.ThreadMXBean;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
/**
 * 【强制】线程资源必须通过线程池提供，不允许在应用中自行显式创建线程。 
 * 说明：线程池的好处是减少在创建和销毁线程上所消耗的时间以及系统资源的开销，解决资源不足的问题。如果不使用线程池，有可能造成系统创建大量同类线程而导致消耗完内存或者“过度切换”的问题。
 * 【强制】线程池不允许使用Executors去创建，而是通过ThreadPoolExecutor的方式，这样的处理方式让写的同学更加明确线程池的运行规则，规避资源耗尽的风险。
 *  说明：Executors返回的线程池对象的弊端如下：
 *  	 1） FixedThreadPool和SingleThreadPool： 允许的请求队列长度为Integer.MAX_VALUE，可能会堆积大量的请求，从而导致OOM。
 *		 2） CachedThreadPool： 允许的创建线程数量为Integer.MAX_VALUE，可能会创建大量的线程，从而导致OOM。
 * 《阿里巴巴Java开发手册（泰山版）.pdf》
 * https://club.perfma.com/article/2375443#/article/2375443 Java 线程池中的线程复用是如何实现的？
 * @author dell
 */
public class ThreadPoolTest {

	public static void main(String[] args) {
		/**
		 * Executors.newFixedThreadPool的源码实际上是通过new ThreadPoolExecutor创建的线程池
		 */
		ExecutorService threadPool = Executors.newFixedThreadPool(3, new UserThreadFactory("创建线程池时,自己设置线程的名字"));
		for(int i=0;i<=10;i++) {
			final int task = i;
			threadPool.execute(new Runnable(){
				@Override
				public void run() {
					for(int j=0; j<=10;j++){
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						System.out.println(Thread.currentThread().getName() + "is loop of" + j + "for task of" + task);
						if (task ==2) {
							int ss = 1/0;//当线程由于代码异常撕掉了,newFixedThreadPool会自动再创建一个线程来运行
						}
					}
				}
			});
		}
		
		System.out.println("上面我往线程池里面丢了10个任务,但是在同一时间最多有3个线程在运行。");
		
		threadPool.shutdown();//当线程池里面的所有任务都执行完时,线程处于空闲状态时,关闭线程池
//		try {
//			Thread.currentThread().sleep(100000000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		
		/**
		 * newCachedThreadPool这个线程池里面的线程数是不固定的,有多少任务就创建几个线程,
		 * 任务没了,线程过一段时间就会被回收掉
		 */
		ExecutorService threadCachePool = Executors.newCachedThreadPool();
		for(int i=0;i<=10;i++) {
			final int task = i;
			threadCachePool.execute(new Runnable(){
				@Override
				public void run() {
					for(int j=0; j<=10;j++){
						System.out.println(Thread.currentThread().getName() + " thread cache is loop of" + j + "for task of" + task);
						if (task ==2) {
							int ss = 1/0;//当线程由于代码异常撕掉了,newFixedThreadPool会自动再创建一个线程来运行
						}
					}
				}
			});
		}
		threadCachePool.shutdown();//当线程池里面的所有任务都执行完时,线程处于空闲状态时,关闭线程池
		
		/**
		 * newSingleThreadExecutor这个线程池里面只有一个线程,当线程死了会重新创建一个线程出来
		 * 总之保证永远有一个线程在干活
		 */
		ExecutorService threadSinglePool = Executors.newSingleThreadExecutor();
		for(int i=0;i<=10;i++) {
			final int task = i;
			threadSinglePool.execute(new Runnable(){
				@Override
				public void run() {
					for(int j=0; j<=10;j++){
						System.out.println(Thread.currentThread().getName() + " single is loop of" + j + "for task of" + task);
						if (task ==2) {
							int ss = 1/0;//当线程由于代码异常撕掉了,newFixedThreadPool会自动再创建一个线程来运行
						}
					}
				}
			});
		}
		threadSinglePool.shutdown();//当线程池里面的所有任务都执行完时,线程处于空闲状态时,关闭线程池
		
		 ScheduledExecutorService ss = Executors.newScheduledThreadPool(3);
		 ss.schedule(new Runnable(){
			@Override
			public void run() {
				System.out.println("定时器线程池");
			}	
		}, 10, TimeUnit.SECONDS);
		ss.shutdown();
		
		
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
		
		
		/**
		 * scheduleAtFixedRate这个定时器没有指定日期的功能,比如你指定一个date是周二晚上10点,
		 * 不过java官方提供了一种方法,你可以先指定一个时间,然后用你指定的那个时间减去现在的时间,就是到那个时间炸。
		 * date.gettime() - System.currentTimeMillis()
		 * 见scheduleAtFixedRate.pngJAVA官方的说明
		 */
		 ScheduledExecutorService scheduleSer = Executors.newScheduledThreadPool(3);
		 scheduleSer.scheduleAtFixedRate(new Runnable(){
			@Override
			public void run() {
				System.out.println("定时器线程池,6秒以后炸,然后每隔2秒炸一下" + new Date().getSeconds());
			}	
		}, 6, 2, TimeUnit.SECONDS);
		//scheduleSer.shutdown();这里不能调用这个,要不然scheduleSer就不会执行了
		 
		//如果任务以固定的3秒为间隔执行，我们可以这样写：
	   // 2秒后开始执行定时任务，以3秒为间隔执行:
	   //ses.scheduleWithFixedDelay(new Task("fixed-delay"), 2, 3, TimeUnit.SECONDS);
		 /*
		  * 注意FixedRate和FixedDelay的区别。
		  * FixedRate是指任务总是以固定时间间隔触发，不管任务执行多长时间：
		  * │░░░░   │░░░░░░ │░░░    │░░░░░  │░░░  
			├───────┼───────┼───────┼───────┼────>
			│<─────>│<─────>│<─────>│<─────>│
		  * 而FixedDelay是指，上一次任务执行完毕后，等待固定的时间间隔，再执行下一次任务：
		  * │░░░│       │░░░░░│       │░░│       │░
			└───┼───────┼─────┼───────┼──┼───────┼──>
			    │<─────>│     │<─────>│  │<─────>│
		  * 因此，使用ScheduledThreadPool时，我们要根据需要选择执行一次、FixedRate执行还是FixedDelay执行。
		  * 细心的童鞋还可以思考下面的问题：
		  * 在FixedRate模式下，假设每秒触发，如果某次任务执行时间超过1秒，后续任务会不会并发执行？答:如果此任务的任何执行时间超过其周期，则后续执行可能会延迟开始，但不会并发执行。
		  * 如果任务抛出了异常，后续任务是否继续执行？答:如果任务的任何执行遇到异常，则将禁止后续任务的执行。
		  * Java标准库还提供了一个java.util.Timer类，这个类也可以定期执行任务，但是，一个Timer会对应一个Thread，所以，一个Timer只能定期执行一个任务，
		  * 多个定时任务必须启动多个Timer，而一个ScheduledThreadPool就可以调度多个定时任务，所以，我们完全可以用ScheduledThreadPool取代旧的Timer。
		  */
	}
}
