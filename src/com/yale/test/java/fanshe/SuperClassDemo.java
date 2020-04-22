package com.yale.test.java.fanshe;

interface IMessageSec {
	
}

interface IFruitSec {
	
}

class Person implements IMessageSec, IFruitSec {
	
}

public class SuperClassDemo {

	public static void main(String[] args) {
		Class<?> cls = Person.class;
		System.out.println("得到类的包名称:" + cls.getPackage().getName());
		System.out.println("得到父类的名称:Class<? super T> " + cls.getSuperclass().getName());
		Class<?>[] inArr = cls.getInterfaces();
		for (int x=0;x<inArr.length; x++) {
			System.out.println("得到的类实现的接口名称:" + inArr[x].getName());
		}
	}
}
