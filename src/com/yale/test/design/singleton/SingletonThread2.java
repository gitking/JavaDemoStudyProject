package com.yale.test.design.singleton;

class SingletonDemo2 {
	private static volatile SingletonDemo2 sd;
	private SingletonDemo2() {
		System.out.println("单例模式构造方法只能被运行一次:" + Thread.currentThread().getName());
	}
	
	public static SingletonDemo2 getInstance() {
		if (sd == null) {
			synchronized (SingletonDemo2.class) {
				if (sd == null) {
					sd = new SingletonDemo2();
				}
			}
		}
		return sd;
	}
	
}
public class SingletonThread2 {
	public static void main(String[] args) {
		/**
		 * 在多线程环境下,SingletonDemo的构造方法会被运行多次,不再试单例模式了
		 */
		new Thread(()->SingletonDemo2.getInstance(),"线程A").start();
		new Thread(()->SingletonDemo2.getInstance(),"线程B").start();
		new Thread(()->SingletonDemo2.getInstance(),"线程C").start();
		new Thread(()->SingletonDemo2.getInstance(),"线程D").start();
		new Thread(()->SingletonDemo2.getInstance(),"线程E").start();
	}
}
