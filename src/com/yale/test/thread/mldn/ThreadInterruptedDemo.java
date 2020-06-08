package com.yale.test.thread.mldn;

//https://blog.csdn.net/qq_39682377/article/details/81449451
public class ThreadInterruptedDemo {

	public static void main(String[] args) {
		
		
		System.out.println(Thread.currentThread().getName() + "_" + Thread.currentThread().isInterrupted());
		System.out.println(Thread.currentThread().getName() + "_" +  Thread.currentThread().isInterrupted());
		
		System.out.println(Thread.currentThread().getName() + "_" + Thread.interrupted());
		System.out.println(Thread.currentThread().getName() + "_" + Thread.interrupted());
		
		System.out.println(Thread.currentThread().getName() + "_" + Thread.currentThread().isInterrupted());
		System.out.println(Thread.currentThread().getName() + "_" + Thread.currentThread().isInterrupted());
		
		
		InterruptedRun thread = new InterruptedRun();
		thread.start();
		thread.interrupt();
		System.out.println("第一次调用thread.isInterrupted()："+thread.isInterrupted());
		System.out.println("第二次调用thread.isInterrupted()："+thread.isInterrupted());
		System.out.println("thread是否存活："+thread.isAlive());
		
		System.out.println("interrupted()注意这里判断的是main线程是否被中断(即使你用的是thread这个对象):" + thread.interrupted());
		System.out.println("interrupted()注意这里判断的是main线程是否被中断:" + Thread.interrupted());
		System.out.println("thread是否存活："+thread.isAlive());
		
		
		Thread.currentThread().interrupt();//将main线程中断
		System.out.println("interrupted()注意这里判断的是main线程是否被中断(即使你用的是thread这个对象):" + Thread.currentThread().interrupted());
		System.out.println("interrupted()注意这里判断的是main线程是否被中断:" + Thread.currentThread().interrupted());
		System.out.println("interrupted()注意这里判断的是main线程是否被中断:" + Thread.currentThread().interrupted());
	}
}

class InterruptedRun extends Thread {
	@Override
	public void run() {
		String dem = "";
		String str = "";
		for (int i=0; i<10000; i++) {
			dem = i + "测试";
			str = str + dem;
			System.out.println("i = " + (i+1));
		}
	}
}
