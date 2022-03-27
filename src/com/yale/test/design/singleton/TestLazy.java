package com.yale.test.design.singleton;

public class TestLazy {
	public static void main(String[] args) {
		SingletonLday s = null;
		System.out.println("此时SingletonLday类到底有没有被加载?:" + SingletonLday.class);
		/*
		 * 你会发现高版本的JVM加载class并没有初始化静态变量，直到第一次调用getInstance()才会执行createInstance()方法，
		 * 也就是JVM本身就是懒加载static变量的
		 * Java的类加载器太复杂，不是几句话能讲清楚的
		 * 可以参考ClassForNameDemo.java
		 */
		s = SingletonLday.getInstance();//create singleton instance...
		System.out.println("加载完毕:" + s);
	}
}
