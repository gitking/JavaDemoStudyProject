package com.yale.test.thread.chapter.four;


public class Thread6{
	public static void main(String[] args) {
		final Thread6 t5 = new Thread6();
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
		 * 尽管线程A与B访问了同一个Inner对象中两个毫不相关的部分,但因为A先获得了对Inner的对象锁，
		 * 所以B对Inner.method2()的访问也被阻塞，因为method2()是Inner中的一个同步方法。
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
		
		public synchronized void method2() {
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
