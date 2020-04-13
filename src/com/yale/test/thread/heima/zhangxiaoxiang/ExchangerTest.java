package com.yale.test.thread.heima.zhangxiaoxiang;

import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Exchanger用于实现俩个人之间的数据交换,每个人在完成一定的事务后想与对方交换数据，
 * 第一个先拿出数据的人将一直等待第二个人拿着数据到来,此时才能彼此交换数据
 * @author dell
 *
 */
public class ExchangerTest {

	public static void main(String[] args) {
		ExecutorService service = Executors.newCachedThreadPool();
		final Exchanger exchanger = new Exchanger();
		service.execute(new Runnable(){
			@Override
			public void run() {
				try {
					String data = "zxx";
					System.out.println("线程" + Thread.currentThread().getName() + "正在把数据" + data + "换出去");
					Thread.sleep((long)(Math.random()*10000));
					String data2 = (String)exchanger.exchange(data);//到这里代码会等待,等待另外一个线程把数据给我
					System.out.println("线程" + Thread.currentThread().getName() + "换回的数据为:" + data2);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		
		service.execute(new Runnable(){
			@Override
			public void run() {
				try {
					String data = "lhm";
					System.out.println("线程" + Thread.currentThread().getName() + "正在把数据" + data + "换出去");
					Thread.sleep((long)(Math.random()*10000));
					String data2 = (String)exchanger.exchange(data);//到这里代码会等待,等待另外一个线程把数据给我
					System.out.println("线程" + Thread.currentThread().getName() + "换回的数据为" + data2);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		service.shutdown();
	}
}
