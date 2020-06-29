package com.yale.test.java.demo.pro;

import com.yale.test.java.demo.SubPersonDemo;

public class ProtectedTest{
	public static void main(String[] args) {
		SubPersonDemo spd = new SubPersonDemo("test", 10, 99);
		//int sc = spd.score;score是对象属性,只能在子类的对象方法里面使用
		//spd.staticFile = 10;//staticFile使用static修饰的,所以可以在子类中使用
		SubClass sc = new SubClass("test", 10, 99);
		//sc.score = 10;//因为在不同包中,这里会报错,只能在子类中使用,
		//sc.staticFile = 20;//因为在不同包中,这里会报错,只能在子类中使用
	}
}
