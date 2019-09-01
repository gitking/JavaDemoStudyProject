package com.yale.test.thread.chapter.three;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CyclisBarrietTest {

	public static void main(String[] args) {
		ExecutorService es = Executors.newCachedThreadPool();
		final CyclicBarrier cb = new CyclicBarrier(3);//可以让多人线程等待,等线程都到达某个点,再让所有线程一起出发
		for (int i=0;i<3;i++) {
			Runnable runnable = new Runnable(){
				public void run(){
					try {
						Thread.sleep((int)(Math.random()*10000));
						System.out.println(Thread.currentThread().getName()+"已到达集合点01,已经到达"+(cb.getNumberWaiting()+1)+"个人"+
						(cb.getNumberWaiting()==2?",人到齐了,继续走":"继续等待,等人到齐"));
						cb.await();
						Thread.sleep((int)(Math.random()*10000));
						System.out.println(Thread.currentThread().getName()+"已到达集合点02,已经到达"+cb.getNumberWaiting()+"个人"+(cb.getNumberWaiting()==2?",人到齐了,继续走":"继续等待,等人到齐"));
						cb.await();
						Thread.sleep((int)(Math.random()*10000));
						System.out.println(Thread.currentThread().getName()+"已到达集合点03,已经到达"+cb.getNumberWaiting()+"个人"+(cb.getNumberWaiting()==2?",人到齐了,继续走":"继续等待,等人到齐"));
						cb.await();
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (BrokenBarrierException e) {
						e.printStackTrace();
					}
				}
			};
			es.execute(runnable);
		}
		System.out.println("创建3个线程扔进线程池子里面");
		es.shutdown();
		System.out.println("*********************");
	}

}
