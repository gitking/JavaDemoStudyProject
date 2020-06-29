package com.yale.test.java.demo;

/*
 * 如果我们需要在src目录下执行javac命令：
 * javac -d ../bin ming/Person.java hong/Person.java mr/jun/Arrays.java
 * 位于同一个包的类，可以访问包作用域的字段和方法。不用public、protected、private修饰的字段和方法就是包作用域。
 * 包作用域是指一个类允许访问同一个package的没有public、private修饰的class，以及没有public、protected、private修饰的字段和方法。
 * 只要在同一个包，就可以访问package权限的class、field和method：
 * 注意，包名必须完全一致，包没有父子关系，com.apache和com.apache.abc是不同的包。
 */
public class Student extends Person {

	public void test() {
		System.out.println("方法的参数是子类,实际传参数时不能传父类,如果想传父类,就要把父类强制转换为子类后,再进行传参");
		System.out.println("将父类强制转换为子类传给方法时,代码运行时会有风险,因为方法内部有可能调用子类特有的方法.");
	}
	
	public void superDemo() {
		System.out.println("Student类:中间的父类,");
	}
}
