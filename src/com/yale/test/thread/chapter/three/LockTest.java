package com.yale.test.thread.chapter.three;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockTest {

	public static void main(String[] args) {
		final Outputer outputer = new Outputer();
		new Thread(new Runnable(){
			public void run(){
				while(true){
					outputer.output("zhangxiaoxiang");
				}
			}
		}).start();
		new Thread(new Runnable(){
			public void run(){
				while(true){
					outputer.output("yuhongming");
				}
			}
		}).start();
	}

	static class Outputer {
		//Lock比synchronized更加面向对象,也更好用,可以对比TraditioanlThreadSynchrized学习使用
		Lock lock = new ReentrantLock();
		public void output(String name){
			try{
				lock.lock();
				int len = name.length();
				for (int i=0;i<len;i++) {
					System.out.print(name.charAt(i));
				}
				System.out.println();
			} finally{
				lock.unlock();
			}
		}
	}
}

