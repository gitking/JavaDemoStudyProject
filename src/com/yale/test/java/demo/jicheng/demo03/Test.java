package com.yale.test.java.demo.jicheng.demo03;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;


public class Test {
	public static void main(String[] args) throws IntrospectionException {
		Son son = new Son();
		son.setName("存储到父类里面去了");
		/**
		 * 这里有一个很有意思的现象就是,Son这个类和son这个对象都没有name这个属性,
		 * 但是却可以通过setName和getName方法往属性name里面存值和取值.
		 * 那这里就有一个疑问父类Father的属性name到底有没有被子类继承呢？假如用反射把私有变成公开的,通过子类是否可以取到呢？
		 */
		System.out.println(son.getName());
		System.out.println("son对象的Class:" + son.getClass());
		Father ft = (Father)son;
		if (ft == son) {
			System.out.println("引用地址是否一样呢？答:是一样的。");
		} else {
			System.out.println("引用地址是否一样呢？答:是不一样的。");
		}
		System.out.println(ft.getName());
		System.out.println("ft对象的Class:" + ft.getClass());
		Father father = new Father();
		System.out.println("father对象的Class:" + father.getClass());
		Son sonSec = (Son)ft;
		if (ft == son && sonSec == ft && sonSec== son) {
			System.out.println("引用地址是否一样呢？答:是一样的。");
		} else {
			System.out.println("引用地址是否一样呢？答:是不一样的。");
		}
		System.out.println(sonSec.getName());
		
		Class<?> clsSec = son.getClass();
		Field[] fieldArr =clsSec.getFields();
		if (fieldArr != null) {
			System.out.println("fieldArr的长度为:" + fieldArr.length);
			for (Field field:fieldArr) {
				System.out.println("通过反射得到Son类的字段:" + field.getName());
			}
		} else {
			System.out.println("通过反射得到Son类的字段:" + fieldArr);
		}
		
		Field[] decFiledArr = clsSec.getDeclaredFields();
		System.out.println("decFiledArr的长度为:" + decFiledArr.length);
		for (Field field: decFiledArr) {
			System.out.println("通过反射得到Son类的字段:" + field.getName());
		}
		
		//要枚举一个JavaBean的所有属性，可以直接使用Java核心库提供的Introspector：
		BeanInfo info = Introspector.getBeanInfo(Son.class);
		System.out.println("通过反射得不到Son类的任何字段,但是通过Introspector却可以得到字段(属性)");
		System.out.println("注意字段和属性不是一个意思,看com.yale.test.java.demo.JavaBean.java这个类就明白了");
        for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
            System.out.println("属性:" + pd.getName());//注意class属性是从Object继承的getClass()方法带来的。
            System.out.println("  " + pd.getReadMethod());
            System.out.println("  " + pd.getWriteMethod());
            System.out.println("----------------------------------");
        }
	}
}
