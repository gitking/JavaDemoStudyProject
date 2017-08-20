package com.yale.test.thread.test;

public class ThreadDemo extends Thread{
	
	public ThreadDemo (Runnable run) {
		super(run);
	}
	
	public void run() {
		System.out.println("继承了Thread类,JVM会优先调用子类的方法");
	}
	
	public static void main(String[] args) {
		ThreadDemo sd = new ThreadDemo(new ThreadRun());
		sd.run();
	}
}

class ThreadRun implements Runnable {
	public void run () {
		System.out.println("实现了runnable接口");
	}
}
