package com.yale.test.java.demo;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
/*
 * JavaBean主要用来传递数据，即把一组数据组合成一个JavaBean便于传输。
 */
public class JavaBean {
	public static void main(String[] args) throws IntrospectionException {
		//要枚举一个JavaBean的所有属性，可以直接使用Java核心库提供的Introspector：
		BeanInfo info = Introspector.getBeanInfo(PersonDemo.class);
        for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
            System.out.println(pd.getName());//注意class属性是从Object继承的getClass()方法带来的。
            System.out.println("  " + pd.getReadMethod());
            System.out.println("  " + pd.getWriteMethod());
        }
	}
}
