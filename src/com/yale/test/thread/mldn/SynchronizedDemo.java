package com.yale.test.thread.mldn;

class TicketDemo implements Runnable {
	private int ticket = 10;
	@Override
	public void run() {
		for (int x=0; x<20; x++) {
			synchronized (this) {//上锁,在同一时刻,只允许一个线程进入并且操作,其它线程需要等待
				if (this.ticket > 0) {
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println(Thread.currentThread().getName() + "买票,ticket = " + this.ticket--);
				}
			}
		}
	}
}
public class SynchronizedDemo {
	public static void main(String[] args) {
		System.out.println("所谓的同步指的是所有的线程不是一起进入到方法中执行,而是按照顺序一个一个进来。");
		System.out.println("同步虽然可以保证数据的完整性(线程安全操作),但是其执行的速度会很慢。");
		TicketDemo td = new TicketDemo();
		new Thread(td, "票贩子A").start();
		new Thread(td, "票贩子B").start();
		new Thread(td, "票贩子C").start();
	}
}
