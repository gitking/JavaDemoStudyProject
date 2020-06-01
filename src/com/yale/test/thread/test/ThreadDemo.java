package com.yale.test.thread.test;

public class ThreadDemo extends Thread{
	
	public ThreadDemo (Runnable run) {
		super(run);
	}
	
	public void run() {
		System.out.println("继承了Thread类,JVM会优先调用子类的方法");
	}
	
	public static void main(String[] args) {
		
		ThreadRun ddd = new ThreadRun();
		
//		Thread t1 = new Thread(ddd);
//		Thread t2 = new Thread(ddd);
//		Thread t3 = new Thread(ddd);
//		Thread t4 = new Thread(ddd);
//		
//		t1.start();
//		t2.start();
//		t3.start();
//		t4.start();
		
		
		ThreadDemo sd = new ThreadDemo(new ThreadRun());
		sd.run();
	}
}

class ThreadRun implements Runnable {
	private int num = 10;
	
	public void run () {
		num = num -1;
		System.out.println("实现了runnable接口" + num);
	}
}
