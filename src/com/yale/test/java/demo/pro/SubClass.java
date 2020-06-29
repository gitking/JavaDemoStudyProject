package com.yale.test.java.demo.pro;

import com.yale.test.java.demo.SubPersonDemo;

public class SubClass extends SubPersonDemo{
	
	public SubClass(String name, int age, int score) {
		super(name, age, score);
	}
	public static void main(String[] args) {
		SubPersonDemo spd = new SubPersonDemo("test", 10, 99);
		//int sc = spd.score;score是对象属性,只能在子类的对象方法里面使用
		spd.staticFile = 10;//staticFile使用static修饰的,所以可以在子类中使用
		SubClass sc = new SubClass("test", 10, 99);
		sc.score = 10;
		sc.staticFile = 20;
	}
	
	public void testScore() {
		System.out.println("用protected修饰的属性,只能在子类的中使用:" + this.score);
		System.out.println("如果子类跟父类不在同一个包下:那么用protected修饰的属性不是静态属性,那么只能在子类的对象方法里面访问:" + this.score);
	}
}
