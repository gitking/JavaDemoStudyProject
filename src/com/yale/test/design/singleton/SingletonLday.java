package com.yale.test.design.singleton;

/*
 * https://www.liaoxuefeng.com/wiki/1252599548343744/1281319214514210#0
 */
public class SingletonLday {
	static {
		System.out.println("init SingletonLday class");
	}
	
	private static SingletonLday instance = createInstance();
	
	private static SingletonLday createInstance() {
		System.out.println("create singletonlday instance...");
		return new SingletonLday();
	}
	
	private SingletonLday() {
	}
	
	public static SingletonLday getInstance() {
		System.out.println("在这类里面");
		return instance;
	}
}
