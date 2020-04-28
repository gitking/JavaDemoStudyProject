package com.yale.test.java.fanshe.imooc.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ClassUtils {
	
	public static void printClassMessage(Object obj) {
		Class c = obj.getClass();
		System.out.println("类的名称是:" + c.getName());
		
		Method[] ms = c.getMethods();
		for (int i=0; i<ms.length; i++) {
			//得到方法的返回值类型的类类型
			Class returnType = ms[i].getReturnType();
			System.out.print(returnType.getName() + "  ");
			System.out.print(ms[i].getName() + "(");
			Class[] paramTypes = ms[i].getParameterTypes();
			for (Class c1 : paramTypes) {
				System.out.print(c1.getName() + ",");
			}
			System.out.println(")");
		}
		
		System.out.println("成员变量的信息:-----------------------");
		//Field[] fs = c.getFields();
		Field[] fs = c.getDeclaredFields();
		for (Field field : fs) {
			Class fieldType = field.getType();
			String typeName = fieldType.getName();
			String fieldName = field.getName();
			System.out.println(typeName + " " + fieldName);
		}
		
		System.out.println("构造函数的信息:-----------------------");
		Constructor[] cs = c.getDeclaredConstructors();
		for (Constructor cons : cs) {
			System.out.print(cons.getName() + "(");
			Class[] paramType = cons.getParameterTypes();
			for (Class param : paramType) {
				System.out.print(param.getName() + ",");
			}
			System.out.println(")");
		}
	}
	
	public static void main(String[] args) {
		String s = "hello";
		printClassMessage(s);
		
	}
}
