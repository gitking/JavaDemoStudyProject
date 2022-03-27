package com.yale.test.java.fanshe;

import java.util.Date;

interface IFruit {
	public void eat();
}

class Apple implements IFruit {
	@Override
	public void eat() {
		System.out.println("【Apple】吃苹果");
	}
}
class Orange implements IFruit {
	@Override
	public void eat() {
		System.out.println("【Orange】吃橘子");
	}
}

interface IMessage {
	public void print();
}

class MessageImpl implements IMessage {
	@Override
	public void print() {
		System.out.println("www.mldn.cn,注意FactoryThir这个工厂类可以为任意接口实现实例化");
	}
}

class Factory {
	private Factory() {
	}
	public static IFruit getInstance(String className) {
		if ("apple".equals(className)) {
			return new Apple();
		} else if ("orange".equals(className)) {
			return new Orange();
		}
		return null;
	}
}

class FactorySec {
	private FactorySec() {
	}
	public static IFruit getInstance(String className) {
		IFruit fruit = null;
		try {
			fruit = (IFruit)Class.forName(className).newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return fruit;
	}
}

class FactoryThir<T> {
	private FactoryThir() {
	}
	/**
	 * Cannot make a static reference to the non-static type T
	 * @param className
	 * @return
	 */
	public static <T> T getInstance(String className) {
		T fruit = null;
		try {
			fruit = (T)Class.forName(className).newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return fruit;
	}
}

public class ClassDemo {

	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		//这里只能用Class<?>接收不能用Class<Date>接收
		Class<?> cls = new Date().getClass();
		System.out.println("这个前面会输出一个class：-->" + cls);
		System.out.println("这个前面不会输出class：包点类名称就是类的名字-->" + cls.getName());
		System.out.println("****************************************");

		Class<?> clsSec = Date.class;
		System.out.println("这个前面会输出一个class：-->" + clsSec);
		System.out.println("这个前面不会输出class：包点类名称就是类的名字-->" + cls.getName());
		
		System.out.println("****************************************");
		Class<?> clsThir = Class.forName("java.util.Date");//包点类名称就是类的名字-->
		System.out.println("这个前面会输出一个class：-->" + clsThir);
		System.out.println("这个前面不会输出class：包点类名称就是类的名字-->" + clsThir.getName());
		System.out.println("****************************************");
		
		System.out.println("在以上给出的三种方法里面我们会发现一个神奇的地方:除了第一种形式会产生Date类的实例化对象之外,其它的俩种都不会产生");
		System.out.println("Date类的实例化对象。于是取的Class类对象有一个最直接的好处:可以直接通过反射实例化对象,在Class类里面定义有");
		System.out.println("有如下一个方法:newInstance");
		
		Object obj = clsThir.newInstance();//实例化对象,等价:new java.util.Date()
		System.out.println(obj);

		IFruit fruit = Factory.getInstance("apple");
		fruit.eat();
		
		IFruit fruitSec = FactorySec.getInstance("com.yale.test.java.fanshe.Apple");
		fruitSec.eat();
		
		IFruit fruitThir = FactoryThir.getInstance("com.yale.test.java.fanshe.Apple");
		fruitThir.eat();
		
		IMessage message = FactoryThir.getInstance("com.yale.test.java.fanshe.MessageImpl");
		message.print();
		System.out.println("从实际的开发来讲,工厂类上使用泛型之后,就可以更好的为更多的接口进行服务了");
	}
}
