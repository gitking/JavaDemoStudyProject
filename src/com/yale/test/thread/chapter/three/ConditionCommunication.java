package com.yale.test.thread.chapter.three;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 创建三个线程,线程1线运行10次,接着让线程2运行10次,接着让线程3运行10次,如次反复3次,运行过程总不能被对方打乱
 * @author Administrator
 */
public class ConditionCommunication {

	public static void main(String[] args) {
		Bussiness bs = new Bussiness();
		new Thread(){
			public void run(){
				for(int i=0;i<3;i++) {
					bs.sub1();
				}
			}
		}.start();
		
		new Thread(){
			public void run(){
				for(int i=0;i<3;i++) {
					bs.sub2();
				}
			}
		}.start();
		
		for (int i=0;i<3;i++){
			bs.sub3();
		}
	}

	static class Bussiness {
		private final Lock lock = new ReentrantLock();
		private final Condition condition01 = lock.newCondition();//竞争条件
		private final Condition condition02 = lock.newCondition();
		private final Condition condition03 = lock.newCondition();
		private int orderNum = 1;
		public void sub1(){
			lock.lock();
			try {
				while(orderNum != 1){
					try {
						condition01.await();//让当前线程满足条件condition01并让当前线程进入线程等待状态
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				for(int i=0;i<10;i++){
					System.out.println(Thread.currentThread().getName()+",01运行"+i+"次");
				}
				orderNum = 2;
				condition02.signal();//唤醒满足条件condition02的线程
			}finally {
				lock.unlock();
			}
		}
		
		public void sub2(){
			lock.lock();
			try {
				while(orderNum != 2){
					try {
						condition02.await();//让当前线程满足条件condition02并让当前线程进入线程等待状态
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				for(int i=0;i<10;i++){
					System.out.println(Thread.currentThread().getName()+",02运行"+i+"次");
				}
				orderNum = 3;
				condition03.signal();//唤醒满足条件condition02的线程
			}finally {
				lock.unlock();
			}
		}
		
		public void sub3(){
			lock.lock();
			try {
				while(orderNum != 3){
					try {
						condition03.await();//让当前线程满足条件condition03并让当前线程进入线程等待状态
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				for(int i=0;i<10;i++){
					System.out.println(Thread.currentThread().getName()+",03运行"+i+"次");
				}
				orderNum = 1;
				condition01.signal();//唤醒满足条件condition02的线程
			}finally {
				lock.unlock();
			}
		}
	}
}
