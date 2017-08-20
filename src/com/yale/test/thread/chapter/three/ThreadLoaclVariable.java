package com.yale.test.thread.chapter.three;

import java.util.Random;

public class ThreadLoaclVariable {
	public static void main(String[] args) {
		for (int i=0;i<2;i++) {
			new Thread(){
				public void run(){
					int data = new Random().nextInt();
					System.out.println(Thread.currentThread().getName() + "put data :" + data);
					MyThreadLoaclScopeData myThreadLocal = MyThreadLoaclScopeData.getTrheadLocalInstance();
					myThreadLocal.setAge(data);
					myThreadLocal.setName("name" + data);
					new A().get();
					new B().get();
				}
			}.start();
		}
	}
	
	static class A {
		public void get() {
			MyThreadLoaclScopeData myThreadLocal = MyThreadLoaclScopeData.getTrheadLocalInstance();
			System.out.println("A " + Thread.currentThread().getName() + "get data:" + myThreadLocal.getName()+"age:"
								+ myThreadLocal.getAge());
		}
	}
	
	static class B {
		public void get() {
			MyThreadLoaclScopeData myThreadLocal = MyThreadLoaclScopeData.getTrheadLocalInstance();
			System.out.println("B " + Thread.currentThread().getName() + "get data:" + myThreadLocal.getName()+"age:"
								+ myThreadLocal.getAge());
		}
	}
}

class MyThreadLoaclScopeData{
	
	private MyThreadLoaclScopeData(){
	}
	
	public static MyThreadLoaclScopeData getTrheadLocalInstance() {
		MyThreadLoaclScopeData myThreadLocalScopedata= myThreadLocal.get();
		if (myThreadLocalScopedata == null) {
			myThreadLocalScopedata = new MyThreadLoaclScopeData();
			myThreadLocal.set(myThreadLocalScopedata);
		}
		return myThreadLocalScopedata;
	}
	
	private static ThreadLocal<MyThreadLoaclScopeData> myThreadLocal = new ThreadLocal<MyThreadLoaclScopeData>();
	
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

