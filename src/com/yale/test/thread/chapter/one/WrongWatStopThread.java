package com.yale.test.thread.chapter.one;

public class WrongWatStopThread extends Thread {

	public static void main(String[] args) {
		WrongWatStopThread m1 = new WrongWatStopThread();
		m1.start();
		System.out.println("线程开始运行");
		try {
			m1.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		m1.interrupt();//将线程设置为终止状态,慎用,最后不用
		
		System.out.println("调用了interrupt()方法来终止线程");
		
		try {
			m1.sleep(3000);
		} catch (InterruptedException e) {
		}
	}

	public void run (){
		while (!this.isInterrupted()) {//不是终止状态就继续运行
			System.out.println("线程依旧在运行");
			long runTime = System.currentTimeMillis();
			while ((System.currentTimeMillis() - runTime) < 1000) {
				
			}
//			try {
//				this.sleep(1000);//如果在调用sleep前面调用过interrupt()方法,那么就会跑出异常,并且线程的终止状态会同时被清除掉
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
		}
	}
}
