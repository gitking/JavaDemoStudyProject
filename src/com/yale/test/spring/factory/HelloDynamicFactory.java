package com.yale.test.spring.factory;

import com.yale.test.spring.vo.Hello;

public class HelloDynamicFactory {
	//动态工厂类
	public Hello newInstance(String name, int age) {
		return new Hello(name, age);
	} 
}
