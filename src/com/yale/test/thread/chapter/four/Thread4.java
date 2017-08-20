package com.yale.test.thread.chapter.four;


public class Thread4{
	public static void main(String[] args) {
		final Thread3 t1 = new Thread3();
		/**
		 * 第三个例子同样适用其它同步代码块。也就是说，当一个线程访问object的一个synchronized(this)同步代码块时，
		 * 它就获得了这个object的对象锁。结果，其它线程对该object对象所有同步代码部分的访问都被暂时阻塞。
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
	
	public synchronized void synMethod2() {
		int i=5;
		while(--i > 0){
			System.out.println(Thread.currentThread().getName() + ":" + i);
		}
	}
}
