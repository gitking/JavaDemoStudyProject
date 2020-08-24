package com.yale.test.java.fanshe;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

class PersonSec{
	public PersonSec() throws RuntimeException, Exception{}
	public PersonSec(String name) throws RuntimeException, Exception{}
	public PersonSec(String name, int age) throws RuntimeException, Exception{}
}

class PersonThir {
	private String name;
	private int age;
	public PersonThir(String name, int age) {
		this.name = name;
		this.age = age;
	}
	@Override
	public String toString() {
		return "PersonThir [name=" + name + ", age=" + age + "]";
	}
}
public class ConstructorDemo {

	public static void main(String[] args) throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		Class<?> cls = PersonSec.class;
		Constructor<?>[] conts = cls.getConstructors();
		for (int i=0;i<conts.length; i++) {
			System.out.println("获取类的所有构造方法:" + conts[i]);
			System.out.println("获取类的所有构造方法(注意构造方法的getName方法):" + conts[i].getName());
			System.out.println("得到构造方法的修饰符public还是别的什么,返回的是数字:" + conts[i].getModifiers());
			int m = conts[i].getModifiers();
			System.out.println(Modifier.isFinal(m));
			System.out.println(Modifier.isPublic(m));
			System.out.println(Modifier.isProtected(m));
			System.out.println(Modifier.isPrivate(m));
			System.out.println(Modifier.isStatic(m));
			System.out.println("得到构造方法的修饰符public还是别的什么,Modifier类里面定义了数字对应的中文:" + Modifier.toString(conts[i].getModifiers()));
			Class<?>[] params = conts[i].getParameterTypes();
			for (int y=0;y<params.length; y++) {
				System.out.println("得到构造方法的参数类型:" + params[y].getName());
			}
			
			Class<?>[] exps = conts[i].getExceptionTypes();
			for (int x=0; x<exps.length; x++) {
				System.out.println("构造方法抛出的异常" + exps[x].getName());
			}
			System.out.println("----------------------------------------------------------");

		}
		
		
		/*
		 * 
		    getConstructor(Class...)：获取某个public的Constructor；
		    getDeclaredConstructor(Class...)：获取某个Constructor；
		    getConstructors()：获取所有public的Constructor；
		    getDeclaredConstructors()：获取所有Constructor。
		 * 注意Constructor总是当前类定义的构造方法，和父类无关，因此不存在多态的问题。
		 * 调用非public的Constructor时，必须首先通过setAccessible(true)设置允许访问。setAccessible(true)可能会失败。
		 * https://www.liaoxuefeng.com/wiki/1252599548343744/1264803033837024
		 */
		Class<?> per = PersonThir.class;
		//现在明确表示取的指定参数类型的构造方法
		Constructor<?> cont = per.getConstructor(String.class, int.class);
		System.out.println("反射通过构造方法实例化对象" + cont.newInstance("张三", 10));
		System.out.println("PersonThir没有无参构造方法,下面这行的代码会报错");
		System.out.println(per.newInstance());
	}
}
