package com.yale.test.thread;

class Run01 implements Runnable {
	public void run () {
		String name = "wangyale";
		for (int i=0; i <name.length();i++){
			System.out.print(name.charAt(i));
		}
		System.out.println();
	}
}
class Run02 implements Runnable {
	public void run () {
		String name = "runyifeng";
		for (int i=0; i <name.length();i++){
			System.out.print(name.charAt(i));
		}
		System.out.println();
	}
}
public class ThreadPrintName {
	public static void main(String[] args) {
		Run01 run01 = new Run01();
		Run02 run02 = new Run02();
		Thread th1 = new Thread(run01);
		Thread th2 = new Thread(run02);
		th1.start();
		th2.start();
	}
}
