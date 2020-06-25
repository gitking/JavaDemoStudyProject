package com.yale.test.spring.factory;

import com.yale.test.spring.vo.Hello;

public class HelloFactory {
	public static Hello newInstance(String name, int age) {
		return new Hello(name, age);
	} 
}
