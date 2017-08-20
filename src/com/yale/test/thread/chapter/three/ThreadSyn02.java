package com.yale.test.thread.chapter.three;

/**
 * 子线程循环运行10次,接着主线程循环100次,接着又回到子线程运行10次,接着再回到主线程循环100次
 * 如此循环50次
 */
public class ThreadSyn02 {

	public static void main(String[] args) {
		Task task = new Task();
		new Thread(new Runnable(){
			public void run(){
				try {
					int i = 0;
					while (i < 50) {
						task.subThread();
						i++;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}, "子线程").start();
		
		Thread.currentThread().setName("主线程");
		int i = 0;
		while (i < 50){
			try {
				task.mainThread();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			i++;
		}
	}
}

class Task {
	private boolean isSubThread = true;
	/**
	 * 主线程运行10次
	 * @throws InterruptedException 
	 */
	public synchronized void mainThread() throws InterruptedException {
		while (isSubThread) {
			this.wait();
		}
		int i = 0;
		while (i<100) {
			i++;
			System.out.println(Thread.currentThread().getName()+"运行" + i +"次");
		}
		isSubThread = true;
		this.notifyAll();
	}
	
	/**
	 * 子线程运行10次
	 * @throws InterruptedException 
	 */
	public synchronized void subThread() throws InterruptedException {
		while(!isSubThread) {
			this.wait();
		}
		int i = 0;
		while (i<10) {
			i++;
			System.out.println(Thread.currentThread().getName()+"运行" + i +"次");
		}
		isSubThread = false;
		this.notifyAll();
	}
}
