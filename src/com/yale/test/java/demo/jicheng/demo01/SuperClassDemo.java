package com.yale.test.java.demo.jicheng.demo01;

public class SuperClassDemo {
	public void test() {
		System.out.println("test()这个方法只在父类SuperClassDemo.java里面定义了,子类会把这个方法给继承过去。代码从父类开始执行");
		sameMethod();
	}
	
	public void sameMethod() {
		System.out.println("父类这个sameMethod方法被子类覆盖了,当用父类new子类对象时,调用test(),test()方法再调用sameMethod()实际上会执行子类的sameMethod()方法");
	}
}
