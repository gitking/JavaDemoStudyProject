package com.yale.test.thread.chapter.three;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 利用阻塞队里也可以实现线程之间的同步
 * 这个类实现功能:子线程先运行10次,主线程再运行20次,如次反复3次,跟ThreadSyn02类实现同样的功能
 * @author Administrator
 */
public class BlockingQueueCommunication {
	public static void main(String[] args){
		final Business bs = new Business();
		new Thread(){
			public void run(){
				for(int i=0;i<3;i++){
					bs.sun(i);
				}
			}
		}.start();
		
		for (int i=0;i<3;i++){
			bs.main(i);
		}
	}
	
	static class Business {
		final BlockingQueue bq = new ArrayBlockingQueue(1);
		final BlockingQueue bqSec = new ArrayBlockingQueue(1);
		
		{//匿名构造函数,会在任何构造函数方法执行执行执行
			try {
				bqSec.put(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		public void sun (int num){
			try {
				bq.put(1);
				for(int i=0;i<10;i++){
					System.out.println("子线程循环执行第"+i+"次,子线程运行第"+num+"次循环");
				}
				bqSec.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		public void main (int num){
			try {
				bqSec.put(1);
				for(int i=0;i<20;i++){
					System.out.println("主线程循环执行第"+i+"次,主线程运行第"+num+"次循环");
				}
				bq.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
