package com.yale.test.lambda;

public class Person {
	private String name;
	private int age;
	public Person() {
		
	}
	public Person(String name, int age){
		this.name = name;
		this.age = age;
	}
	
	@Override
	public String toString() {
		return "Person" + ",name=" + this.name + ",age=" + this.age;
	}
}
