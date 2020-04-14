package com.yale.test.thread.heima.zhangxiaoxiang;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreeConditionCommunication {

	public static void main(String[] args) {
		
		/**
		 * Condition的功能类似在传统线程中的Object.wait()和Object.notify的功能.在等待Condition时,允许发生“虚假唤醒”,这通常作为对基础平台语义的让步
		 * 一个锁内部可以有多个Condition,既有多路等待通知,可以参考java1.5提供的Lock与Condition实现的可阻塞队里的应用案例
		 * 子线程循环10次，接着主线程循环100次,接着又回到子线程循环10次,接着回到主线程又循环100次,如此循环50次
		 * 传智播客_张孝祥_传统线程同步通信技术 第四集
		 */
		final Business business = new Business();
		new Thread(new Runnable(){
			@Override
			public void run() {
				for (int i=0;i<=50;i++) {
						business.sub2(i);
				}
			}
		}).start();
		
		
		
		new Thread(new Runnable(){
			@Override
			public void run() {
				for (int i=0;i<=50;i++) {
						business.sub3(i);
				}
			}
		}).start();
		
		for (int i=0;i<=50;i++) {
			business.main(i);
		}
	}
	
	static class Business {
		Lock lock = new ReentrantLock();
		/**
		 * 多路等待通知,可以实现线程按顺序运行,老大先运行,老二再运行,老三再运行
		 */
		Condition condtion1 = lock.newCondition();//Condition对象必须在Lock对象上面创建
		Condition condtion2 = lock.newCondition();//Condition对象必须在Lock对象上面创建
		Condition condtion3 = lock.newCondition();//Condition对象必须在Lock对象上面创建
		
		private int shouldSub = 1;//老大先运行
		
		public void main(int i){
			lock.lock();
			while (shouldSub != 1) {
				try {
					condtion1.await();//老大等待
				} catch(InterruptedException e){
					e.printStackTrace();
				}
			}
			for (int j=0; j<= 100;j ++){
				System.out.println("老大 thread sequence of " + j + " loop of" + i);
			}
			shouldSub = 2;
			condtion2.signal();//注意不是notify()方法，唤醒老二
			lock.unlock();
		}
		
		public void sub2(int i) {
			lock.lock();
			while (shouldSub != 2) {//这里不能用if,这里可以看Object里面的wait方法,例子.java官方API就是推荐使用while判断,防止虚假唤醒
				try {
					condtion2.await();//注意不是wati()方法
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			for (int j=1; j<=10; j++) {
				System.out.println("老二 thread sequence of "+ j + " loop of" + i);
			}
			shouldSub = 3;
			condtion3.signal();//注意不是notify()方法，唤醒老三
			lock.unlock();
		}
		
		public void sub3(int i) {
			lock.lock();
			while (shouldSub != 3) {//这里不能用if,这里可以看Object里面的wait方法,例子.java官方API就是推荐使用while判断,防止虚假唤醒
				try {
					condtion3.await();//注意不是wati()方法
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			for (int j=1; j<=10; j++) {
				System.out.println("老三 thread sequence of "+ j + " loop of" + i);
			}
			shouldSub = 1;
			condtion1.signal();//注意不是notify()方法，唤醒老大
			lock.unlock();
		}
	}
}


