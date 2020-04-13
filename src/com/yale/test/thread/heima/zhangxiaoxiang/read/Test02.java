package com.yale.test.thread.heima.zhangxiaoxiang.read;

import java.util.concurrent.Semaphore;
import java.util.concurrent.SynchronousQueue;

/**
 * 有10个线程来消费生产者生成的数据，这些消费者都调用Test.doSome()方法去进行处理,每个消费者需要1秒才能处理完成,程序应保证这些消费者线程
 * 依次有序的消费数据,只有上一个消费者消费完成后,下一个消费者才能消费数据,下一个消费者是谁都可以.但要保证消费者线程拿到的是数据是有序的.
 * @author dell
 *
 */
public class Test02 {
	public static void main(String[] args) {
		//SynchronousQueue是juc线程库了一个阻塞队列,
		/**
		 * SynchronousQueue的功能类似于他往里面放一个数据,然后可以有多个线程都来抢,谁先抢到谁就可以取出数据
		 */
		SynchronousQueue<String> queue = new SynchronousQueue<String>();
		final Semaphore sh = new Semaphore(1);
		for (int i=0;i<10;i++) {
			new Thread(new Runnable(){
				@Override
				public void run() {
					try {
						sh.acquire();
						String input = queue.take();
						String output = TestDo.doSome(input);
						System.out.println(Thread.currentThread().getName() + ":" + output);
						sh.release();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
		System.out.println("begin:" + (System.currentTimeMillis() / 1000));
		for(int i=0;i<10;i++) {//这行不能动
			String input = i+"";//这行不能动
			try {
				queue.put(input);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//String output = TestDo.doSome(input);
			//System.out.println(Thread.currentThread().getName() + ":" + output);
		}
	}
}
//不能改动此TestDo类
class TestDo {
	public static String doSome(String input) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String output = input + ":" + (System.currentTimeMillis()/1000);
		return output;
	}
}
