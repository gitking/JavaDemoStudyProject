package com.yale.test.thread.chapter.one;

public class Stage extends Thread {
	
	public void run() {
		ArmThread armThread = new ArmThread();
		Thread arm = new Thread(armThread,"隋军");
		
		ArmThread armFarmer = new ArmThread();
		Thread farmer = new Thread(armFarmer,"农民军");
		
		arm.start();//让线程进入准备状态,等待CPU的调用
		System.out.println("**********");
		farmer.start();//让线程进入准备状态,等待CPU的调用
		
		try {
			Thread.sleep(50);//50毫秒的攻击时间
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		armThread.keepAttack = false;//结束攻击
		armFarmer.keepAttack = false;//结束攻击
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		KeyPresonThread keyPersonThread = new KeyPresonThread("程咬金");
		keyPersonThread.start();
		try {
			keyPersonThread.join();//其他所有线程必须等到调用join()方法的线程执行完毕
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("****程咬金结束了战争****");
	}
	
	public static void main(String[] args) {
		new Stage().start();
	}
}
