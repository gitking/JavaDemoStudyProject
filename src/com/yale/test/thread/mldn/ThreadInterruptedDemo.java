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
		
		
		/**
		 * isInterrupted(true),参数传true的意思是：先返回线程当前是否处于中断状态后再清除中断状态。这样描述比较正确。
		 * isInterrupted(false),参数传false的意思是：不清除线程状态,并返回线程状态。
		 * 终于搞明白了, isInterrupted(true) 这个方法实际上干了俩件事情, 第一件事情是先获取线程的状态, 第二件事情是清除线程的中断状态。
		 * interrupted()源码调用的就是isInterrupted(true)
		 * isInterrupted()源码调用的就是isInterrupted(false)
		 */
		Thread.currentThread().interrupt();//将main线程的状态设置为中断状态,仅仅是设置状态,线程依然可以运行
		System.out.println("interrupted()注意这里判断的是main线程是否被中断(即使你用的是thread这个对象):" + Thread.interrupted());
		System.out.println("interrupted()注意这里判断的是main线程是否被中断:" + Thread.interrupted());
		System.out.println("interrupted()注意这里判断的是main线程是否被中断:" + Thread.interrupted());
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
