package com.yale.test.thread.chapter.three;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Test {
	
	public static void main(String[] args){
		BlockingQueue<String> bq = new ArrayBlockingQueue<String>(16);
		for (int i=0;i<4;i++) {
			new Thread(new Runnable(){
				public void run(){
					while(true){
						try {
							String log = bq.take();
							Test.parseLog(log);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			},"线程0"+i).start();
		}
		System.out.println("开始:" + System.currentTimeMillis());
		/**
		 * 模拟处理16行日志,下面的代码产生了16个日志对象,当前代码需要运行16秒才能打印完成这些日志
		 * 修改代码,开四个线程让这16个对象在4秒钟内打印完毕
		 */
		for (int i=0;i<16;i++) {//这行代码不能改动
			final String log = "" + i;//这行代码不能改动
			{//定义一个范围,没有具体作用
				try {
					bq.put(log);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void parseLog(String log){
		System.out.println(log + ":" + (System.currentTimeMillis()/1000));
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
