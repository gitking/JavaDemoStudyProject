package com.yale.test.thread;

class MyThreadSec implements Runnable{
	private int ticketCount = 5;
	public void run () {
		while (ticketCount > 0) {
			ticketCount --;
			System.out.println(Thread.currentThread().getName()+"卖出一张票,剩余票数:" + ticketCount);
		}
	}
}

public class RunabledTest {

	public static void main(String[] args) {
		MyThreadSec myThread = new MyThreadSec();
		Thread m1 = new Thread(myThread,"窗口1");
		Thread m2 = new Thread(myThread,"窗口2");
		Thread m3 = new Thread(myThread,"窗口3");
		m1.start();
		m2.start();
		m3.start();
	}

}
