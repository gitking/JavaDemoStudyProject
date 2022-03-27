package com.yale.test.java.demo.jicheng.demo03;

public class Father {
	private String name;//子类继承不了私有属性
	private int age;
	public String getName() {//但是公共方法子类可以继承
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
