package com.yale.test.compareable;

public class Person implements Comparable<Person>{
	public int age;
	public String name;
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
		this.name = name;
	}
	
	public Person(int age, String name) {
		super();
		this.age = age;
		this.name = name;
	}
	@Override
	public int compareTo(Person o) {
		if (this.age > o.age) {
			return 1;
		} else if (this.age < o.age) {
			return -1;
		} else {
			return 0;
		}
	}
	@Override
	public String toString() {
		return "Person [age=" + age + ", name=" + name + "]";
	}
}
