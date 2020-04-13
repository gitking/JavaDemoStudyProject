package com.yale.test.thread.heima.zhangxiaoxiang;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BlockingQueueCommunication {

	public static void main(String[] args) {
		final Business business = new Business();
		new Thread(new Runnable(){
			@Override
			public void run() {
					for (int i=1;i<=50;i++) {
						business.sub(i);
					}
			}
		}).start();
		
		for (int i=1;i<=100;i++) {
			business.main(i);
		}
	}
	
	static class Business {//用俩个具有1个空间的队列来实现同步通知的功能
		BlockingQueue<Integer> queue = new ArrayBlockingQueue<Integer>(1);
		BlockingQueue<Integer> queue1 = new ArrayBlockingQueue<Integer>(1);
		{//这种写法叫匿名构造方法,他的运行时机在任何构造方法之前
			try {
				queue1.put(1);
				System.out.println("看看这里在什么时候输出就行了,这种写法叫匿名构造方法,他的运行时机在任何构造方法之前");
				System.out.println("这块代码可以运行多次,你创建几个实例就运行几次,static静态代码块只能够运行一次");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		public void sub(int i) {//这个方法和下面方法上面不能加synchronized,加上就会发生死锁现象
			try {
				queue.put(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for (int j=1; j<=10;j ++) {
				System.out.println("sub thread sequece of " + j + ", loop of " + j);
			}
			try {
				queue1.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		public void main(int i) {//这个方法和下面方法上面不能加synchronized,加上就会发生死锁现象,用jstack可以看到线程死锁的现象
			try {
				queue1.put(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for (int j=1; j<=100;j++) {
				System.out.println("main thread sequece of " + j + ", loop of " + i);
			}
			try {
				queue.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
