package com.yale.test.thread.heima.zhangxiaoxiang;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TraditionalTimerTest {

	static int count = 0;

	public static void main(String[] args) {
		//TimerTask类实现了Runnable的接口
//		new Timer().schedule(new TimerTask(){
//			@Override
//			public void run() {
//				System.out.println("boming!");
//			}
//			//开始之后10秒之后执行一次,然后每隔3秒之后再执行一次
//		}, 10000, 3000);
		
		class MyTimerTask extends TimerTask{
			//static int count = 0; 内部类不能创建静态变量
			@Override
			public void run() {
				count = (count + 1) % 2;
				System.out.println("boming!");
				//2秒之后执行，然后4秒之后执行，然后又2妙执行,又4秒执行一次
				new Timer().schedule(new MyTimerTask(), 2000 + 2000 * count);
			}
		}
		
		//2秒之后执行MyTimerTask的run方法,MyTimerTask方法2秒之后又调自己
		//这个思想很重要,点子keyword
		new Timer().schedule(new MyTimerTask(), 2000);
		
		
		//这里的new Date可以指定一个时间,然后每隔多少秒执行一下
		//new Timer().schedule(new MyTimerTask(), new Date(), 2000);

		
		
		//quartz是个开源的框架,是一个定时任务框架
		
		
		while(true) {
			System.out.println(new Date().getSeconds());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
