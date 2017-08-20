package com.yale.test.thread.chapter.three;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ThreadPoolTest {
	public static void main (String[] args){
		ExecutorService executorService = Executors.newFixedThreadPool(3);//创建一个有3个线程的线程池
		for (int i =0;i<10;i++) {
			final int num = i;
			executorService.execute(new Runnable(){//Runnable代表任务,循环往线程池里面放10个任务进去
				public void run() {
					for (int i=0;i<10;i++) {
						System.out.println(i + ","+Thread.currentThread().getName() + "run of num " + num);
					}
				}
			});
		}
		System.out.println("创建10个任务,然后放进线程池");
		executorService.shutdown();//当线程池里面的任务完成后,关闭线程
		
		//创建一个线程,如果这个线程意外死亡,那么线程池会重新创建一个线程,保证有个一线程是存活的
		ExecutorService executorServiceSec = Executors.newSingleThreadExecutor();
		executorServiceSec.shutdown();
		
		//创建一个定时器,定时执行任务
		ScheduledExecutorService ss = Executors.newScheduledThreadPool(3);
		for (int i=0;i<10;i++) {
			ss.schedule(new Runnable(){
				public void run () {
					System.out.println(Thread.currentThread().getName() + "BOOMING");
				}
			}, 3, TimeUnit.SECONDS);
		}
		ss.shutdown();
		//创建一个定时器,定时执行任务,第一次6秒,之后每隔2秒就执行一次
		ScheduledExecutorService ses = Executors.newScheduledThreadPool(3);
		for (int i =0;i<10;i++) {
			ses.scheduleAtFixedRate(new Runnable(){
				public void run () {
					System.out.println("***"+Thread.currentThread().getName() + "BOOMING");
				}
			}, 6, 2, TimeUnit.SECONDS);
		}
	}
}
