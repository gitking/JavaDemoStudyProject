package com.yale.test.thread.chapter.three;

import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Exchanger可以实现俩个线程之间进行数据交换
 * @author Administrator
 *
 */
public class ExchangerTest {
	public static void main(String[] args) {
		ExecutorService es = Executors.newCachedThreadPool();
		final Exchanger ex = new Exchanger();
		es.execute(new Runnable(){
			public void run(){
				try {
					String data = "王宁";
					System.out.println("线程:" + Thread.currentThread().getName()+"正在把数据:" + data +",换出去");
					Thread.sleep((int)(Math.random()*1000));
					String dataSec = (String)ex.exchange(data);
					System.out.println("线程:"+Thread.currentThread().getName()+"换回的数据为:"+dataSec);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		
		es.execute(new Runnable(){
			public void run(){
				try {
					String data = "王亚乐";
					System.out.println("线程:" + Thread.currentThread().getName()+"正在把数据:" + data +",换出去");
					Thread.sleep((int)(Math.random()*1000));
					String dataSec = (String)ex.exchange(data);
					System.out.println("线程:"+Thread.currentThread().getName()+"换回的数据为:"+dataSec);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		
		es.execute(new Runnable(){
			public void run(){
				try {
					String data = "张崇博";
					System.out.println("线程:" + Thread.currentThread().getName()+"正在把数据:" + data +",换出去");
					Thread.sleep((int)(Math.random()*1000));
					String dataSec = (String)ex.exchange(data);
					System.out.println("线程:"+Thread.currentThread().getName()+"换回的数据为:"+dataSec);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		
		es.execute(new Runnable(){
			public void run(){
				try {
					String data = "张翱翔";
					System.out.println("线程:" + Thread.currentThread().getName()+"正在把数据:" + data +",换出去");
					Thread.sleep((int)(Math.random()*1000));
					String dataSec = (String)ex.exchange(data);
					System.out.println("线程:"+Thread.currentThread().getName()+"换回的数据为:"+dataSec);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		es.shutdown();
	}
}
