package com.yale.test.spring.vo;

/**
 * 这个类模仿的是我们公司的CommonDao类,
 * @author dell
 */
public class Cat extends Animal{
	public void print(){
		System.out.println("可以在Spring的配置文件里面,配置init-method初始化方法,这样"
				+ "Spring在创建这个类的时候,就会直接调用这个方法");
	}
	
	public void destroy(){
		System.out.println("可以在Spring的配置文件里面,配置destroy-method注销方法,这样"
				+ "对象在销毁的时候,Spring就会自动调用这个方法进行注销");
	}
}
