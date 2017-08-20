package com.yale.test.thread.chapter.four;


public class Thread5{
	public static void main(String[] args) {
		final Thread5 t5 = new Thread5();
		final Inner inner = t5.new Inner();
		new Thread(new Runnable(){
			public void run(){
				t5.method3(inner);
			}
		},"A").start();
		
		new Thread(new Runnable(){
			public void run(){
				t5.method4(inner);
			}
		},"B").start();;
	}
	
	private void method3(Inner inner){
		/**
		 * 尽管线程A获得了对Inner的对象锁，但由于线程B访问的是同一个Inner中的非同步部分。所以两个线程互不干扰。
		 */
		synchronized (inner) {
			inner.method();
		}
	}
	
	private void method4(Inner inner){
		inner.method2();
	}

	class Inner{
		public void method(){
			int i=5;
			while(--i >0){
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println(Thread.currentThread().getName() + ":" + i);
			}
		}
		
		public void method2() {
			int i=5;
			while(--i > 0){
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println(Thread.currentThread().getName() + ":" + i);
			}
		}
	}
}
