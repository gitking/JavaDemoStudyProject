package com.yale.test.thread.chapter.four;


public class Thread3{

	public static void main(String[] args) {
		final Thread3 t1 = new Thread3();
		/**
		 * 尤其关键的是，当一个线程访问object的一个synchronized(this)同步代码块时，
		 * 其他线程对object中所有其它synchronized(this)同步代码块的访问将被阻塞。
		 */
		Thread th = new Thread(new Runnable(){
			public void run(){
				t1.synMethod();
			}
		},"A");
		Thread th2 = new Thread(new Runnable(){
			public void run(){
				t1.synMethod2();
			}
		},"B");
		th.start();
		th2.start();
	}

	public void synMethod(){
		synchronized (this) {
			int i=5;
			while(--i >0){
				System.out.println(Thread.currentThread().getName() + ":" + i);
			}
		}
	}
	
	public void synMethod2() {
		synchronized (this) {
			int i=5;
			while(--i > 0){
				System.out.println(Thread.currentThread().getName() + ":" + i);
			}
		}
	}
}
