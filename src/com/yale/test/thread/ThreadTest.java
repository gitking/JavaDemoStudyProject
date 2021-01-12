package com.yale.test.thread;

/*
 * 线程是独立的线程。它代表独立的执行空间。每个JAVA应用程序会启动一个主线程--将main线程放在它自己执行空间的最开始处。
 * java虚拟机会负责主线程的启动(以及比如垃圾收集所需的系统用线程)。程序员得负责启动自己建立的线程。
 * 当有超过一个以上的执行空间时,看起来会像是有好几件事情同时发生。实际上,只有真正的多处理器系统能够同时执行好几件事，但使用java
 * 的线程可以让它看起来好像同时都在执行中。也就是说,执行动作可以在执行空间非常快速地来回交互，因此你会感觉到每项任务都在执行。
 * 要记得,java也只是个在底层操作系统上执行的进程。一单轮到java执行的时候,java虚拟机实际上会执行什么？
 * 哪个字节码会被执行?答案是目前执行空间最上面的会被执行!在100个毫秒内,目前执行程序代码会被切换到不同的空间上的不同方法。
 * 线程要记录的一项事物是目前线程执行空间做到哪里了
 * 当你调用线程的start()方法时,线程会变成可执行状态。意思是说它准备好要执行了,只要轮到它就可以开始。这时,该线程已经布置好执行空间。
 * 执行中:所有的线程都在等待这一刻，成为执行中的那一个.这只能靠Java虚拟机的线程调度机制来决定。你有时也能对java虚拟机选择执行线程提点意见，
 * 但无法强制它把线程从可执行状态移动到执行中。
 * 问:Thread对象可以重复使用吗？能否调用start()指定新的任务给它？
 * 答:不行。一旦线程的run()方法完成之后,该线程就不能再重新启动。事实上过了该点线程就会死翘翘。Thread对象可能还呆在堆上 ,如果活着的对象一般还能接受
 * 某些方法的调用,但已经永远地失去了线程的执行性,只剩下对象本身。
 * Thread alpha = new Thread(new Runnable());
 * alpha.setName(“给线程起个名字”);
 * 《Head First Java》
 * 建议阅读O'Reilly出版的"Java Thread",上面有一些概念的澄清和设计的提示可以帮忙避免死锁(有中文译本)
 */
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
