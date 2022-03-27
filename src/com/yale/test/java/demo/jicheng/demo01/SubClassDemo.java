package com.yale.test.java.demo.jicheng.demo01;

public class SubClassDemo extends SuperClassDemo{
	public String name = "【子类的属性】";

	public void sameMethod() {
		System.out.println("【来自子类】:我是子类,把父类的sameMethod方法覆盖了,当用父类new子类对象的时候,会执行子类的sameMethod()这个方法.属性不会覆盖:" + name);
	}
}
