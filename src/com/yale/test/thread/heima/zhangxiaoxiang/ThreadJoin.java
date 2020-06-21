package com.yale.test.thread.heima.zhangxiaoxiang;

public class ThreadJoin {
	public static void main(String[] args) {
		Thread thA = new Thread(new JoniDemo(), "线程-1");
		Thread thB = new Thread(new JoniDemo(), "线程-2");
		thA.start();
		thB.start();
		
		try {
			/**
			 * join()等待该线程死亡。线程死亡的时候notifyAll方法会被调用,会通知join
			 * join()方法调用的是join(0)方法.join(0)方法调用的是wait(0)方法,0意味着永远等待.
			 * join()方法的逻辑是先调用线程的isAlive()方法判断线程是否存活,如果存活就调用wait(time)方法
			 * https://yq.aliyun.com/articles/610881?spm=a2c4e.11155435.0.0.24d67229e5YHHF
			 * https://blog.csdn.net/zhutulang/article/details/48504487?spm=a2c4e.10696291.0.0.365519a4fCLA2E
			 */
			System.out.println(thA.getName() + "此时线程一定是存活状态:" + thA.isAlive());
			thA.join();//等同于调wait方法,自己看源码一清二楚
			System.out.println(thA.getName() + "此时线程一定死亡了:" + thA.isAlive());
			
			/**
			 * thA在join的时候,thB的run方法已经执行完毕了,所以这里thB的状态不一定是存活状态
			 */
			System.out.println(thB.getName() + "此时线程不一定是存活状态:" + thB.isAlive());
			thB.join();
			System.out.println(thB.getName() + "此时线程一定死亡了:" + thB.isAlive());

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("***************" + Thread.currentThread().getName() + "线程执行完毕");
	}
}


class JoniDemo implements Runnable {
	@Override
	public void run() {
		System.out.println(Thread.currentThread().getName() + " 开始执行任务");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("join");
		System.out.println(Thread.currentThread().getName() + "线程执行完毕");
	}
}