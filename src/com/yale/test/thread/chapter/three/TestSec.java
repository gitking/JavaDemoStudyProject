package com.yale.test.thread.chapter.three;

import java.util.concurrent.Semaphore;
import java.util.concurrent.SynchronousQueue;

/**
 * 现成的TestSec类中的代码在不断地产生数据,然后交给TestDo.doSome()方法处理,就好像生产者在不断地产生数据,消费者在不断消费数据
 * 请将程序改造成有10个线程来消费生产者生产的数据,这些消费者都调用TestDo.doSome()方法处理,故每个消费者都需要一秒才能处理完,程序应该保证这些消费者
 * 线程依次有序地消费数据,只有一个消费者消费完成后,下一个消费者才能消费数据,但是下一个消费者是谁都可以,但要保证这些消费者线程拿到的是有序的数据
 * @author Administrator
 *
 */
public class TestSec {

	public static void main(String[] args) {
		final Semaphore sh = new Semaphore(1);
		/**
		 * SynchronousQueue只有有人在排队等着取数据了,sq.put()该方法才会将数据放进去,否则不放数据
		 * sq.put()方法放进数据后,sq.take()就会立即把数据取出来,所以说SynchronousQueue这个队列中永远都没有数据存在
		 * 同步队列类似于 CSP 和 Ada 中使用的 rendezvous 信道。
		 * 它非常适合于传递性设计，在这种设计中，在一个线程中运行的对象要将某些信息、事件或任务传递给在另一个线程中运行的对象，它就必须与该对象同步。 
		 */
		final SynchronousQueue<String> sq = new SynchronousQueue<String>();
		TestDo td = new TestDo();
		for (int i=0;i<10;i++) {
			new Thread(new Runnable(){
				public void run(){
					try {
						sh.acquire();
						String result = td.doSome(sq.take());
						System.out.println(Thread.currentThread().getName() + ":" + result);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					sh.release();
				}
			}).start();
		}
		System.out.println("开始:" +(System.currentTimeMillis()/1000));
		for(int i=0;i<10;i++){//这行不能改动
			try {
				String str = ""+i;//这行不能改动
				sq.put(str);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

class TestDo { 
	public static String doSome(String input){
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String output = input + ":" + (System.currentTimeMillis()/1000);
		return output;
	}
} 