package com.yale.test.spring.vo;

public class Hello {
	public Hello() {
		System.out.println("构造方法,我被Spring实例化了");
	}
	
	public Hello(String name, int age) {
		super();
		this.name = name;
		this.age = age;
	}

	private String name;
	private int age;

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		System.out.println("注意Spring调我这个setName方法了......,Spring传进来的参数为:" + name);
		this.name = name;
	}
	
	public void show() {
		System.out.println("hello, " + name);
	}
}
