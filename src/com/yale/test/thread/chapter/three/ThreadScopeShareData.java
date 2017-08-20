package com.yale.test.thread.chapter.three;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ThreadScopeShareData {

	private static int data;
	private static Map<Thread,Integer> dataMap = new HashMap<Thread,Integer>();
	
	public static void main(String[] args) {
		for (int i =0;i <2;i++) {
			new Thread(){
				public void run () {
					int data = new Random().nextInt();
					dataMap.put(Thread.currentThread(), data);
					System.out.println(Thread.currentThread().getName() + "has put data:" + data);
					new A().get();
					new B().get();
				}
			}.start();
		}
	}
	static class A {
		public void get () {
			int data = dataMap.get(Thread.currentThread());
			System.out.println("A from "+Thread.currentThread().getName() + ",get data:" + data);
		}
	}
	
	static class B {
		public void get () {
			int data = dataMap.get(Thread.currentThread());
			System.out.println("B "+Thread.currentThread().getName() + ",get data:" + data);
		}
	}
}
