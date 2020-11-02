package com.yale.test.design.structural.flyweight;

public class Test {
	public static void main(String[] args) {
		Integer t1 = Integer.valueOf(100);
		Integer t2 = Integer.valueOf(100);
		System.out.println(t1 == t2);
		
		Student s1 = Student.create(1, "小明");
		Student s2 = Student.create(2, "小红");
		Student s3 = Student.create(1, "小明");
		
		System.out.println(s1);
		System.out.println(s2);
		System.out.println(s3);
		System.out.println(s1 == s3);
	}
}
