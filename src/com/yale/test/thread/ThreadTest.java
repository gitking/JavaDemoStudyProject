package com.yale.test.thread;

class MyThread extends Thread {
	private int ticketCount = 5;
	public void run() {
		while (ticketCount > 0) {
			ticketCount--;
			System.out.println(Thread.currentThread().getName()+"卖出第"+ticketCount+"票");
		}
	}
}
public class ThreadTest {
	public static void main(String[] args) {
		MyThread m1 = new MyThread();
		MyThread m2 = new MyThread();
		MyThread m3 = new MyThread();
		m1.start();
		m2.start();
		m3.start();
	}
}
