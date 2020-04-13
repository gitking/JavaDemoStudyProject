package com.yale.test.thread.heima.zhangxiaoxiang;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 先进先出就叫队列,BlockingQueue是阻塞队列的接口,
 * @author dell
 *
 */
public class BlockingQueueTest {

	public static void main(String[] args) {
		final BlockingQueue queue = new ArrayBlockingQueue(3);//阻塞队列的长度为3
		for (int i=0;i<2;i++) {
			new Thread(){
				public void run (){
					while (true) {
						try {
							Thread.sleep((long)(Math.random()*1000));
							System.out.println(Thread.currentThread().getName() + "准备放数据");
							queue.put(1);//该方法放不进去的时候会阻塞,在这里一直等,等到能放进去的时候
							//queue.add(1);该方法放不进去的时候会抛出异常
							//queue.offer(1);该方法放不进去的时候会返回false
							//queue.offer(1,10,TimeUnit.SECONDS);该方法放不进去的时候会等待一段时间,超时之后返回false

							System.out.println(Thread.currentThread().getName() + "已经把数据放进去了," + 
							"队列目前有" + queue.size() + "数据");
						} catch(InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}.start();;
		}
		
		new Thread(){
			public void run() {
				while(true){
					try {
						Thread.sleep(1000);//将此处的睡眠时间分别改为100和1000,观察运行结果
						System.out.println(Thread.currentThread().getName() + "准备取数据!");
						queue.take();//该方法取不到数据的时候会阻塞,在这里一直等,等到能取到数据的时候
						//queue.remove();该方法取不到数据的时候会抛出异常
						//queue.poll();该方法取不到数据的时候会返回false
						//queue.poll(10,TimeUnit.SECONDS);该方法取不到数据的时候会等待一段时间,超时之后返回false
						System.out.println(Thread.currentThread().getName() + "已经取走数据,队列目前有" + queue.size() + "个数据.");
					} catch(InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
	}
}
