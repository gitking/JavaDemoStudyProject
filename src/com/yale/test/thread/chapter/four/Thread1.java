package com.yale.test.thread.chapter.four;


public class Thread1 implements Runnable{

	public static void main(String[] args) {
		Thread1 t1 = new Thread1();
		Thread1 t2 = new Thread1();
		Thread th = new Thread(t1,"A");
		Thread th2 = new Thread(t1,"B");
		th.start();
		th2.start();
	}

	public void run() {
		/**
		 * 当两个并发线程访问同一个对象object中的这个synchronized(this)同步代码块时，
		 * 一个时间内只能有一个线程得到执行。另一个线程必须等待当前线程执行完这个代码块以后才能执行该代码块。
		 */
		synchronized (this) {
			for(int i=0;i<5;i++) {
				System.out.println(Thread.currentThread().getName() + ",loop:" + i);
			}
		}
	}
}
