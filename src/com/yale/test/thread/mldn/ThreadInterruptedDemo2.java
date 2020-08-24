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
public class ThreadInterruptedDemo2 {

	public static void main(String[] args) {
		/*
		 * 下面代码来自廖雪峰JAVA教程
		 * https://www.liaoxuefeng.com/wiki/1252599548343744/1306580767211554
		 * 仔细看下述代码，main线程通过调用t.interrupt()方法中断t线程，但是要注意，interrupt()方法仅仅向t线程发出了“中断请求”，
		 * 至于t线程是否能立刻响应，要看具体代码。而t线程的while循环会检测isInterrupted()，所以上述代码能正确响应interrupt()请求，使得自身立刻结束运行run()方法。
		 * 如果线程处于等待状态，例如，t.join()会让main线程进入等待状态，此时，如果对main线程调用interrupt()，
		 * join()方法会立刻抛出InterruptedException，因此，目标线程只要捕获到join()方法抛出的InterruptedException，
		 * 就说明有其他线程对其调用了interrupt()方法，通常情况下该线程应该立刻结束运行。
		 */
		try {
			MyThreadIsInterrupted mtii = new MyThreadIsInterrupted();
			mtii.start();
			Thread.sleep(1);//main线程休息1毫秒
			mtii.interrupt();//中断mtii线程
			mtii.join();//等待mtii线程结束
			System.out.println("mtii线程被中断了");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

class MyThreadIsInterrupted extends Thread {
	int n = 0;
	@Override
	public void run() {
		while(!isInterrupted()) {
			n ++;
			System.out.println(n + "我总是在检查我自己是否被中断了");
		}
	}
}
