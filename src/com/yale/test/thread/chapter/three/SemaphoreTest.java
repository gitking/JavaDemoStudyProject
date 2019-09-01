package com.yale.test.thread.chapter.three;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class SemaphoreTest {
	public static void main(String[] args) {
		final ExecutorService es = Executors.newCachedThreadPool();
		//Semaphore可以保证某个东西同时最多能有几个线程访问,只用来控制访问的数量,不用来控制同步或者互斥
		final Semaphore st = new Semaphore(3,true);//创建3个线程信号灯,true代表是否公平
		for (int i=0;i<10;i++) {
			Runnable runnable = new Runnable(){
				public void run (){
					try {
						st.acquire();
						System.out.println(Thread.currentThread().getName()+"进入,当前已有"+(3-st.availablePermits())+"并发");
						Thread.sleep((int)(Math.random()*1000));
						System.out.println("线程:"+Thread.currentThread().getName()+"即将离开");
					} catch (InterruptedException e) {
						e.printStackTrace();
					} finally{
						st.release();//释放信号灯
						System.out.println("线程:"+Thread.currentThread().getName()+"已离开,当前已有"+(3-st.availablePermits())+"并发");
					}
				}
			};
			es.execute(runnable);
		}
		System.out.println("创建10个线程任务提交到线程池里面");
		es.shutdown();
		System.out.println("10个线程任务执行完毕");
	}
}
