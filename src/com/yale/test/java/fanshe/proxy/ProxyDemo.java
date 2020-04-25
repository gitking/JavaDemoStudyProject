package com.yale.test.java.fanshe.proxy;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

interface ISubject {
	public void eat();
}

class RealSubject implements ISubject {
	public void eat() {
		System.out.println("饿了一定要吃饭");
	}
}
class Factory {
	private Factory(){}
	@SuppressWarnings("unchecked")//压制代码警告
	public static <T> T getInstance(String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		T t = (T)Class.forName(className).newInstance();
		return t;
	}
	
	@SuppressWarnings("unchecked")//压制代码警告
	public static <T> T getInstance(String proxyClassName, String realClassName) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		T obj = getInstance(realClassName);
		System.out.println(obj.toString());
		System.out.println(obj.getClass());
		Class<?>[] inArr = obj.getClass().getInterfaces();
		for(int i=0;i<inArr.length;i++) {
			System.out.println(inArr[i]);
		} 
		Constructor<?> cons = Class.forName(proxyClassName).getConstructor(obj.getClass().getInterfaces()[0]);
		T t = (T)cons.newInstance(obj);
		return t;
	}
}
class ProxySubject implements ISubject {
	private ISubject subject;
	public ProxySubject(ISubject subject) {
		this.subject = subject;
	}
	public void prepare() {
		System.out.println("需要准备食材,收拾食材");
	}
	@Override
	public void eat() {
		this.prepare();
		this.subject.eat();
		this.clear();
	}
	public void clear() {
		System.out.println("洗刷碗筷");
	}
}
public class ProxyDemo {
	public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		ISubject subject = Factory.getInstance("com.yale.test.java.fanshe.proxy.ProxySubject", "com.yale.test.java.fanshe.proxy.RealSubject");
		subject.eat();
	}
}
