package com.yale.test.thread.chapter.three;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CountDownLatchTest {

	public static void main(String[] args) {
		ExecutorService es = Executors.newCachedThreadPool();
		/**
		 * CountDownLatch就像计时器,调用方法countDown一次就相当于计时器走一次
		 * CountDownLatch计时器没有归零之前,cdl.await();将一直处于等待状态
		 * CountDownLatch计时器没有归零之后,,cdl.await();自动被唤醒
		 */
		final CountDownLatch cdl  = new CountDownLatch(1);//线程倒计时1
		final CountDownLatch cdlSec  = new CountDownLatch(3);//线程倒计时3
		for(int i=0;i<3;i++){
			Runnable runnable = new Runnable(){
				public void run(){
					try {
						System.out.println("运动员"+Thread.currentThread().getName()+",等待裁判吹哨...");
						cdl.await();//等待计时器归0
						System.out.println("运动员"+Thread.currentThread().getName()+",已跑完全程");
						cdlSec.countDown();//计时器走一下
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
			es.execute(runnable);
		}
		es.shutdown();
		try {
			Thread.sleep(3000);
			System.out.println("裁判吹哨子了...");
			cdl.countDown();
			cdlSec.await();
			System.out.println("所有运动员已跑完全程");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
