package com.yale.test.java.demo.jicheng.demo02;

/*
 * 没有在构造方法中初始化字段时，引用类型的字段默认是null，数值类型的字段用默认值，int类型默认值是0，布尔类型默认值是false：
 * https://www.liaoxuefeng.com/wiki/1252599548343744/1260454185794944
 * https://zhuanlan.zhihu.com/p/131584403
 * 可以和com.yale.test.java.fanshe.proxy.cglib.spring.UserService.java类一起学习
 */
public class Person {
	private String name = "初始化";//对字段直接进行初始化：
	private int age = 10;//对字段直接进行初始化：
	
	/**
	 * 那么问题来了：既对字段进行初始化，又在构造方法中对字段进行初始化：
	 * 当我们创建对象的时候，new Person("Xiao Ming", 12)得到的对象实例，字段的初始值是啥？
	 * 在Java中，创建对象实例的时候，按照如下顺序进行初始化：
	 * 1.先初始化字段，例如，int age = 10;表示age字段初始化为10，String name = "初始化",表示name字段初始化为初始化
	 * 2.执行构造方法的代码进行初始化。
	 * 因此，构造方法的代码由于后运行，所以，new Person("Xiao Ming", 12)的字段值最终由构造方法的代码确定。
	 * @param name
	 * @param age
	 */
	public Person(String name, int age) {
		this.name = name;
		this.age = age;
	}
}
