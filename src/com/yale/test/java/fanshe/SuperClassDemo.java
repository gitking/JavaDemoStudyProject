package com.yale.test.java.fanshe;

interface IMessageSec {
	
}

interface IFruitSec {
	
}

class Person implements IMessageSec, IFruitSec {
	
}

public class SuperClassDemo {

	public static void main(String[] args) {
		/*
		 * 除Object外，其他任何非interface的Class都必定存在一个父类类型。
		 */
		Class<?> cls = Person.class;
		System.out.println("得到类的包名称:" + cls.getPackage().getName());
		System.out.println("得到父类的名称:Class<? super T> " + cls.getSuperclass().getName());
		/*
		 * 要特别注意：getInterfaces()只返回当前类直接实现的接口类型，并不包括其父类实现的接口类型：
		 * 此外，对所有interface的Class调用getSuperclass()返回的是null，获取接口的父接口要用getInterfaces()：
		 * 如果一个类没有实现任何interface，那么getInterfaces()返回空数组。
		 */
		Class<?>[] inArr = cls.getInterfaces();
		for (int x=0;x<inArr.length; x++) {
			System.out.println("得到的类实现的接口名称:" + inArr[x].getName());
		}
		
		//如果是两个Class实例，要判断一个向上转型是否成立，可以调用isAssignableFrom()：
		System.out.println("向上转型能否成功:Person可以向上转型给IMessageSec:" + IMessageSec.class.isAssignableFrom(Person.class));
		System.out.println("向上转型能否成功:IMessageSec不可以向上转型给Person:" + cls.isAssignableFrom(IMessageSec.class));
		System.out.println("向上转型能否成功:" + cls.isAssignableFrom(Integer.class));
	}
}
