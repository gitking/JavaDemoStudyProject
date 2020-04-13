package com.yale.test.thread.heima.zhangxiaoxiang;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * CountDownLatch犹如倒计时计数器,调用CountDownLatch对象的countDown方法就将计数器减1,当计数器到达0时
 * 则所有等待者开始执行
 * @author dell
 *
 */
public class CountDownLatchTest {

	public static void main(String[] args) {
		ExecutorService es = Executors.newCachedThreadPool();
		final CountDownLatch cdOrder = new CountDownLatch(1);
		final CountDownLatch cdAnswer = new CountDownLatch(3);
		for (int i=0;i<3;i++) {
			Runnable runnable = new Runnable(){
				@Override
				public void run() {
					try {
						System.out.println("线程" + Thread.currentThread().getName() + "正准备接受命令");
						cdOrder.await();//这里在等待cdOrder的计数器清零,必须调用countDown方法将计时器归零,否则这里将一直到等待
						System.out.println("线程" + Thread.currentThread().getName() + "已接受命令");
						Thread.sleep((long)(Math.random()*10000));
						System.out.println("线程" + Thread.currentThread().getName() + "回应命令处理结果");
						cdAnswer.countDown();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}	
			};
			es.execute(runnable);
		}
		try {
			Thread.sleep((long)(Math.random()*10000));
			System.out.println("线程" + Thread.currentThread().getName() + "即将发布命令");
			cdOrder.countDown();
			System.out.println("线程" + Thread.currentThread().getName() + "已发送命令,正在等待结果");
			cdAnswer.await();//这里在等cdAnswer调用countDown方法将计时器归零
			System.out.println("线程" + Thread.currentThread().getName()+ "已收到所有响应结果");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		es.shutdown();
	}
}
