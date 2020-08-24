package com.yale.test.thread.mldn;

/*
 * https://blog.csdn.net/qq_39682377/article/details/81449451
 * https://www.liaoxuefeng.com/wiki/1252599548343744/1306580767211554
 * 中断线程
 * 如果线程需要执行一个长时间任务，就可能需要能中断线程。中断线程就是其他线程注意是其他线程给该线程发一个信号，该线程收到信号后结束执行run()方法，使得自身线程能立刻结束运行。
 * 我们举个栗子：假设从网络下载一个100M的文件，如果网速很慢，用户等得不耐烦，就可能在下载过程中点“取消”，这时，程序就需要中断下载线程的执行。
 * 中断一个线程非常简单，只需要在其他线程中对目标线程调用interrupt()方法，目标线程需要反复检测自身状态是否是interrupted状态，如果是，就立刻结束运行。
 * 我们还是看示例代码：
 */
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
