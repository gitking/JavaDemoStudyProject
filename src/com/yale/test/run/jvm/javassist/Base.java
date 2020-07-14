package com.yale.test.run.jvm.javassist;

public class Base {
	static {
		System.out.println("---------------------Javassist是在什么时候加载我的---------------------");
	}
	public void process() {
		System.out.println("process");
	}
	
	public static void main(String[] args) {
		Base b = new Base();
		b.process();
	}
}
