package com.yale.test.thread.mldn;

/*
 * https://blog.csdn.net/qq_39682377/article/details/81449451
 * https://www.liaoxuefeng.com/wiki/1252599548343744/1306580767211554
 * 中断线程
 * 如果线程需要执行一个长时间任务，就可能需要能中断线程。中断线程就是其他线程注意是其他线程给该线程发一个信号，该线程收到信号后结束执行run()方法，使得自身线程能立刻结束运行。
 * 我们举个栗子：假设从网络下载一个100M的文件，如果网速很慢，用户等得不耐烦，就可能在下载过程中点“取消”，这时，程序就需要中断下载线程的执行。
 * 中断一个线程非常简单，只需要在其他线程中对目标线程调用interrupt()方法，目标线程需要反复检测自身状态是否是interrupted状态，如果是，就立刻结束运行。
 * 我们还是看示例代码：
 * 小结
 * 对目标线程调用interrupt()方法可以请求中断一个线程，目标线程通过检测isInterrupted()标志获取自身是否已中断。如果目标线程处于等待状态，该线程会捕获到InterruptedException；
 * 目标线程检测到isInterrupted()为true或者捕获了InterruptedException都应该立刻结束自身线程；
 * 通过标志位判断需要正确使用volatile关键字；
 * volatile关键字解决了共享变量在线程间的可见性问题。
 */
public class ThreadInterruptedMarkDemo {
	public static void main(String[] args) throws InterruptedException {
		/*
		 * 另一个常用的中断线程的方法是设置标志位。我们通常会用一个running标志位来标识线程是否应该继续运行，在外部线程中，
		 * 通过把HelloThread.running置为false，就可以让线程结束：
		 * 注意到HelloThread的标志位boolean running是一个线程间共享的变量。
		 * 线程间共享变量需要使用volatile关键字标记，确保每个线程都能读取到更新后的变量值。
		 */
		HelloMarkThread hmt = new HelloMarkThread();
		hmt.start();
		Thread.sleep(1);
		hmt.running = false;//标志位设置为false,告诉线程要结束了
	}
}

class HelloMarkThread extends Thread {
	public volatile boolean running = true;
	@Override
	public void run() {
		int n =0;
		while (running) {
			n++;
			System.out.println(n + "hello!");
		}
		System.out.println("线程结束了end!");
	}
}
