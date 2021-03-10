package com.yale.test.thread.test;

/*
 * 目前CPU调度的最基本单位是线程,Java也是基于多线程模式的,而非多进程模式,由于在多线程模式中是多个线程共享进程的资源,所以当Java程序运行过程中
 * 的一个线程使用资源过大时(例如内存资源),就可能导致整个JVM进程挂掉。这种情况在多进程模型中不会发生,因为在多进程模型中当一个进程挂掉后并不会影响其他的进程运行
 * 程序可以继续fock()新的进程出来,虽然分配进程需要较多的资源,但这似乎确实可以做到没什么影响。如果将问题深入地想一下,进程的资源如果没有任何的使用限制。单个进程使用
 * 资源过多的时候,导致整个机器资源耗尽,最终就是操作系统挂掉了,所以我们说不会影响也是相对的。
 * Web应用的线程池的线程数并非一个绝对值,在CPU密集型系统中理想状态下的线程数是CPU数加一或者减一,目的是让CPU转起来。
 * 在IO密集型系统中,本身就需要大量的线程,并不在乎上下文切换的开销。IO密集型是指系统大部分时间是在做I/O交互,而这个时间线程不会占用CPU来处理(但是通常
 * 会在系统中记录下这个线程正在等待I/O,以便I/O数据返回时,系统可以将其激活,被CPU再次调度)
 * 计算System.out.println也是一种I/O,在这里提到的都是阻塞I/O(在非阻塞I/O中,有一个十分短暂的与Kenel交互的停顿,而上下文信息很多是通过程序来保存的,
 * 因此它的好与坏也是针对实际场景说的).
 */
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
