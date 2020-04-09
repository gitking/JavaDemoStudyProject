package com.yale.test.thread.heima.zhangxiaoxiang;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ThreadLocalTest {

	/**
	 * 线程内共享变量，每个线程有自己的单独的变量
	 * 一个ThreadLocal代表一个变量,故其中只能放一个数据,你有俩个变量都要线程内共享,你要定义多个ThreadLocal
	 * Runtime Runtime是JVM虚拟机的对象
	 */
	private static ThreadLocal<Integer> x = new ThreadLocal<Integer>();
	//Runtime run = Runtime.getRuntime();//Runtime是JVM虚拟机的对象
	//ThreadDeathEvent,ThreadDeathRequest线程死亡的时候可以得到通知
	
	public static void main(String[] args) {
		for(int i=0; i <2; i++) {
			new Thread(new Runnable () {
				@Override
				public void run() {
					int data = new Random().nextInt();
					System.out.println(Thread.currentThread().getName() + " has put data:" + data);
					x.set(data);
					MyThreadScopeData.getThreadInstance().setName("name" + data);
					MyThreadScopeData.getThreadInstance().setAge( data);
					new A().get();
					new B().get();
				}
			}).start();
		}
	}
	
	static class A {//外部类可以用static修改吗？
		public void get() {
			int data = x.get();
			System.out.println("A from " + Thread.currentThread().getName() + " get data:" + data);
			MyThreadScopeData myData = MyThreadScopeData.getThreadInstance();

			System.out.println("A from " + Thread.currentThread().getName() + " getMyData:" + myData.getName() + "," + myData.getAge());

		}
	}
	
	static class B {
		public void get() {
			int data = x.get();
			System.out.println("B from " + Thread.currentThread().getName() + " get data:" + data);
			
			MyThreadScopeData myData = MyThreadScopeData.getThreadInstance();
			System.out.println("B from " + Thread.currentThread().getName() + " getMyData:" + myData.getName() + "," + myData.getAge());
		}
	}
}

class MyThreadScopeData {
	private MyThreadScopeData(){
	}
	private static ThreadLocal<MyThreadScopeData> map = new ThreadLocal<MyThreadScopeData>();
	public static MyThreadScopeData getThreadInstance() {
		MyThreadScopeData instance = map.get();
		if (instance == null) {
			instance = new MyThreadScopeData();
			map.set(instance);
		}
		return instance;
	}
	
	private String name;
	private int age;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
}
