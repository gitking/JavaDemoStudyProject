package com.yale.test.java.demo.jicheng;

public class Son extends Father {
	public static void main(String[] args) {
		Father fa = new Father();
		Son son = new Son();
		System.out.println("子类继承父类的属性name的值:" + son.name);
		fa.setName("变了");
		
		System.out.println("父类的属性name值变了之后,子类会跟着变吗?:" + son.name);
		System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
		System.out.println("父类的属性值变了没?:" + fa.name);
		
	}
}
