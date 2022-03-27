package com.yale.test.thread;

import java.util.Scanner;

/**
 * Java 的线程可以分为两大类：
 * Daemon Thread（守护线程）,User Thread（用户线程）
 * 所谓的守护线程就是指程序运行的时候，在后台提供一种通用服务的线程，比如垃圾回收线程就是一个守护线程。守护线程并不属于程序中不可或缺的部分，因此，当所有的用户线程结束，程序也就终止，程序终止的同时也会杀死进程中所有的守护线程。
 * 上面的实例程序中，main 执行完毕，程序就终止了，所以守护线程也就被杀死，finally 块的代码也就无法执行到了。
 * https://mp.weixin.qq.com/s?__biz=MzU2NzAzMjQyOA==&mid=2247483988&idx=1&sn=5b34b96a5312f2687dd28bce39d542f4&chksm=fca22d57cbd5a441db81097d265556c18cdb386e1ea456e607e78f9a629a4ce9033947d19721&scene=21#wechat_redirect
 * @author issuser
 */
public class DameonThread2 {
	public static void main (String [] args) {
		Thread th = new Thread(new DameonThread2().new Task());
		th.setDaemon(true);
		th.start();
		try {
			Thread.sleep(1000);
			System.out.println("main线程结束之后,守护线程就自动结束了：" + Thread.currentThread().isDaemon());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	class Task implements Runnable {
		@Override
		public void run() {
			try {
				System.out.println("我是守护线程:" + Thread.currentThread().isDaemon());
				System.out.println("用户线程执行完毕后,守护线程也没有什么要守护的了,守护线程就自动退出了,这个finally也不会执行" + Thread.currentThread().isDaemon());
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				System.out.println("+++++++++++++++++用户线程执行完毕后,守护线程也没有什么要守护的了,守护线程就自动退出了,这个finally也不会执行" + Thread.currentThread().isDaemon());
			}
		}
	}
}
