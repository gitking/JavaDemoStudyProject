package com.yale.test.java.fanshe.imooc.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/*
 *  反射工具类 MethodUtils  org.apache.commons.beanutils.MethodUtils    commons-beanutils-1.8.3.jar 
 *	反射工具类 MethodUtils  org.apache.commons.lang.reflect.MethodUtils commons-lang-2.6.jar
 */
public class MethodInvoke {
	public static void main(String[] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		A a1 = new A();
		Class c1 = a1.getClass();
		Method m = c1.getMethod("add", int.class, int.class);
		m.invoke(a1, 10, 20);
		Method m1 = c1.getMethod("print", String.class, String.class);
		m1.invoke(a1, "abc", "DEF");
		
		Method m2 = c1.getMethod("minus");
		m2.invoke(a1);
	}
}
class A {
	public void add(int a, int b) {
		System.out.println("add：" + (a + b));
	}
	
	public void print(String a, String b) {
		System.out.println(a.toUpperCase() + "," + b.toLowerCase());
	}
	
	public void minus () {
		System.out.println("minuMethod");
	}
}