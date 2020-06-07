package com.yale.test.java.demo;

public class Student extends Person {

	public void test() {
		System.out.println("方法的参数是子类,实际传参数时不能传父类,如果想传父类,就要把父类强制转换为子类后,再进行传参");
		System.out.println("将父类强制转换为子类传给方法时,代码运行时会有风险,因为方法内部有可能调用子类特有的方法.");
	}
}
