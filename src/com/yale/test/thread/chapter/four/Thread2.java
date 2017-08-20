package com.yale.test.thread.chapter.four;


public class Thread2{
	public static void main(String[] args) {
		final Thread2 t2 = new Thread2();
		/**
		 * 当一个线程访问object的一个synchronized(this)同步代码块时，
		 * 另一个线程仍然可以访问该object中的非synchronized(this)同步代码块。
		 */
		Thread th = new Thread(new Runnable(){
			public void run(){
				t2.synMethod();
			}
		},"A");
		
		Thread th2 = new Thread(new Runnable(){
			public void run(){
				t2.method();
			}
		},"B");
		
		th.start();
		th2.start();
	}

	public void synMethod(){
		synchronized (this) {
			int i = 5;
			while(i-- > 0){
				System.out.println(Thread.currentThread().getName() + ":" + i);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void method(){
		int i=5;
		while(i-- > 0){
			System.out.println(Thread.currentThread().getName() + ":" + i);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
