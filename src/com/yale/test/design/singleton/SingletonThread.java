package com.yale.test.design.singleton;

class SingletonDemo {
	private static SingletonDemo sd;
	private SingletonDemo() {
		System.out.println("单例模式构造方法只能被运行一次:" + Thread.currentThread().getName());
	}
	
	public static SingletonDemo getInstance() {
		if (sd == null) {
			sd = new SingletonDemo();
		}
		return sd;
	}
	
}
public class SingletonThread {
	public static void main(String[] args) {
		/**
		 * 在多线程环境下,SingletonDemo的构造方法会被运行多次,不再试单例模式了
		 * 这个类的单例模式是不安全的
		 */
		new Thread(()->SingletonDemo.getInstance(),"线程A").start();
		new Thread(()->SingletonDemo.getInstance(),"线程B").start();
		new Thread(()->SingletonDemo.getInstance(),"线程C").start();
		new Thread(()->SingletonDemo.getInstance(),"线程D").start();
		new Thread(()->SingletonDemo.getInstance(),"线程E").start();
	}
}
