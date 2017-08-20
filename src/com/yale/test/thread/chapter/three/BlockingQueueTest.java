package com.yale.test.thread.chapter.three;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * BlockingQueue阻塞队列,当线程取不到数据时,线程就会进入阻塞状态,当有数据可取时线程立马恢复为就绪状态
 * @author Administrator
 */
public class BlockingQueueTest {

	public static void main(String[] args) {
		final BlockingQueue bq = new ArrayBlockingQueue(3);
		for (int i=0;i<2;i++) {
			new Thread(){
				public void run(){
					while(true){
						try {
							Thread.sleep((int)(Math.random()*1000));
							System.out.println(Thread.currentThread().getName()+"准备放数据了:");
							bq.put(1);//放不进去时,就会堵塞
							System.out.println(Thread.currentThread().getName()+"已经放过数据,队列目前有" + bq.size() + "数据");
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}.start();
		}
		
		new Thread(){
			public void run(){
				while(true){
					try {
						//将此处睡眠时间分别改为100和1000,观察运行结果
						Thread.sleep(100);
						System.out.println(Thread.currentThread().getName()+"准备取数据了:");
						bq.take();//取不到数据时,就会进入堵塞队列
						System.out.println(Thread.currentThread().getName()+"已经取走数据,队列目前有" + bq.size() + "数据");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
}
