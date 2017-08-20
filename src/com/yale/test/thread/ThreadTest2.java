package com.yale.test.thread;

public class ThreadTest2 {

	public static void main(String[] args) {
		new Thread(){
			public void run () {
				while (true) {
					System.out.println("Trhead:"+Thread.currentThread().getName());
				}
			}
		}.start();//这种方式可以创建一个匿名的Thread子类
		
		new Thread(new Runnable(){
			public void run () {
				while (true) {
					System.out.println("runnable:"+Thread.currentThread().getName());
				}
			}
		}).start();//这种方式在Thread构造方法内部创建一个Runnbale的匿名子类
		
		new Thread(new Runnable(){
			public void run () {
				while (true) {
					System.out.println("Runnable04:" +Thread.currentThread().getName());
				}
			}
		}){
			public void run () {
				while(true){
					System.out.println("Thread03:" +Thread.currentThread().getName());
				}
			}
		}.start();
	}
}
