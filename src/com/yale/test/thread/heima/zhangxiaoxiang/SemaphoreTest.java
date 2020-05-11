package com.yale.test.thread.heima.zhangxiaoxiang;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Semaphore可以维护当前访问自身线程的个数,并提供了同步机制.使用Semaphore可以控制同时访问资源
 * 的线程个数,例如,实现一个文件允许的并发访问数.
 * 1、Semaphore等待线程可以是随机获得优先机会,也可以是按照先来后到的顺序获得机会,这取决于构造Semaphore对象时传入的参数选项
 * 2，单个信号量的Semaphore对象可以实现互斥锁的功能,并且可以是由一个线程获得了锁,.再由另一个线程释放锁,这可以用于死锁恢复的一些场合
 * @author dell
 */
public class SemaphoreTest {

	public static void main(String[] args) {
		ExecutorService es = Executors.newCachedThreadPool();
		final Semaphore sp = new Semaphore(3);//每个线程都持有这个对象,最多有三个线程同时运行,Semaphore的主要含义就是限流
		//Semaphore还有一个构造方法,第二个参数是:是否公平,默认是不公平的,线程随机获得机会
//		  public Semaphore(int permits, boolean fair) {
//		        sync = fair ? new FairSync(permits) : new NonfairSync(permits);
//		    }
		/**
		 * 怎么实现公平，排队就是公平,靠队列来实现
		 * reentrantlock.CountDownLatch. CyclicBarrier、 Phaser. ReadWriteLock、 Semaphore还有后面要讲的Exchanger
		 * 都是用同一个队列，同一个类来实现的，这个类叫AQS。
		 */
		for (int i=0;i<10;i++) {
			Runnable runable = new Runnable(){
				public void run() {
					try {
						sp.acquire();//获得许可,这个方法是阻塞方法，阻塞方法的意思是说我大概acquire不到的话我就停在这，acquire的意思就是得到。
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					//availablePermits当前可以获得的许可
					System.out.println("线程" + Thread.currentThread().getName()+"进入,当前已有" + (3-sp.availablePermits())
							+ "个并发");
					try {
						Thread.sleep((long)(Math.random()*10000));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("线程" + Thread.currentThread().getName() + "即将离开");
					sp.release();
					//下面代码有时候执行不准确,因为其没有和上面的代码合成原子单元
					System.out.println("线程" + Thread.currentThread().getName() + "已离开,当前已有" + (3-sp.availablePermits())
							+ "个并发");
				}
			};
			es.execute(runable);
		}
		es.shutdown();
	}
}
