package com.yale.test.thread.chapter.three;

/**
 * 子线程循环运行10次,接着主线程循环100次,接着又回到子线程运行10次,接着再回到主线程循环100次
 * 如此循环50次
 */
public class ThreadSyn {
	public static void main (String[] args) {
		
		/**
		 * 字线程运行10次,然后主线程运行100次,这样反复50次
		 */
		final Business bs = new Business();
		
		new Thread(){
			public void run(){
				for (int i=0;i<50;i++) {
					bs.sub(i);
				}
			}
		}.start();
		
		for (int i =0;i <50;i ++) {
			bs.mian(i);
		}
	}
}

class Business {
	
	private boolean beShouldSub = true;//运行子线程
	
	/**
	 * 主线程
	 * @param j
	 */
	public synchronized void mian (int j) {
		while (beShouldSub) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		for (int i = 0; i<100; i++) {
			System.out.println("main thread run [" + i + "]" + j);
		}
		beShouldSub = true;
		this.notifyAll();
	}
	
	/**
	 * 子线程
	 * @param j
	 */
	public synchronized void sub (int j) {
		while (!beShouldSub) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		for (int i = 0; i<10; i++) {
			System.out.println("sub thread run [" + i + "]" + j);
		}
		beShouldSub = false;
		this.notifyAll();
	}
}