package com.yale.test.thread.heima.zhangxiaoxiang.read;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
public class Test {

	public static void main(String[] args) {
		final BlockingQueue<String> queue = new ArrayBlockingQueue<String>(16);
		//final BlockingQueue<String> queue1 = new ArrayBlockingQueue<String>(1);这里用1个也是可以的
		for (int i=0;i <4; i++) {
			new Thread(new Runnable(){
				@Override
				public void run() {
					while(true) {
						try {
							String log = queue.take();
							parseLog(log);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}).start();
		}
		System.out.println("begin:" + (System.currentTimeMillis()/1000));
		/**
		 * 模拟处理16行日志,下面的代码产生了16个日志对象,当前代码需要运行16秒才能打印完成这些日志,
		 * 修改程序代码,开四个线程让这16个对象在4秒钟打完
		 */
		for (int i=0; i<16; i++) {//这行代码不能改动
			final String log = "" + (i + 1);//这个行代码不能改动
			{
				//Test.parseLog(log);//这行代码可以改动
				try {
					queue.put(log);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	//parselog方法内部的代码不能改动
	public static void parseLog(String log) {
		System.out.println(log + ":" + System.currentTimeMillis()/1000);
	}
}
