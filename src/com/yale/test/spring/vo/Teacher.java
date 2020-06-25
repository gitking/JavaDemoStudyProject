package com.yale.test.spring.vo;

public class Teacher {
	private String name;
	private int age;
	private Student stu;
	public Teacher(){
		
	}
	
	public Teacher(String name, int age) {
		super();
		this.name = name;
		this.age = age;
	}

	public Teacher(String name, int age, Student stu) {
		super();
		this.name = name;
		this.age = age;
		this.stu = stu;
	}
	
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
	
	public Student getStu() {
		return stu;
	}

	public void setStu(Student stu) {
		this.stu = stu;
	}

	public void show() {
		if (this.stu != null) {
			System.out.println("演示Spring的p注入和c注入:name" + this.name + ",age:" + this.getAge() + "学生名字:" + this.stu.getName());
		} else {
			System.out.println("演示Spring的p注入和c注入:name" + this.name + ",age:" + this.getAge());
		}
	}
}
