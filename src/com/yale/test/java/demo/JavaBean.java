package com.yale.test.java.demo;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
/*
 * 在Java中，有很多class的定义都符合这样的规范：
 * 1.若干private实例字段；2.通过public方法来读写实例字段。
 * 如果读写方法符合以下这种命名规范：
 * 1.读方法:public Type getXyz()2.写方法 public void setXyz(Type value)
 * 那么这种class被称为JavaBean
 * 上面的字段是xyz，那么读写方法名分别以get和set开头，并且后接大写字母开头的字段名Xyz，因此两个读写方法名分别是getXyz()和setXyz()。
 * boolean字段比较特殊，它的读方法一般命名为isXyz()：
 * 我们通常把一组对应的读方法（getter）和写方法（setter）称为属性（property）。例如，name属性：对应的读方法是String getName()，对应的写方法是setName(String)
 * 只有getter的属性称为只读属性（read-only），例如，定义一个age只读属性：1.对应的读方法是int getAge()2.无对应的写方法setAge(int)
 * 类似的，只有setter的属性称为只写属性（write-only）。很明显，只读属性很常见，只写属性不常见。
 * 注意注意：属性只需要定义getter和setter方法，不一定需要对应的字段。例如，child只读属性定义如下：
 * JavaBean是一种符合命名规范的class，它通过getter和setter来定义属性；使用Introspector.getBeanInfo()可以获取属性列表。
 * 属性是一种通用的叫法，并非Java语法规定；字段指的是成员变量，属性指的是getter和setter方法。
 * JavaBean主要用来传递数据，即把一组数据组合成一个JavaBean便于传输。
 * https://www.liaoxuefeng.com/wiki/1252599548343744/1260474416351680
 */
public class JavaBean {
	private String name;
	private int age;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	
	//这个没有set方法,只有is(get)方法，也会被Introspector认为这是一个属性
	public boolean isChild() {
		return age <=6;
	}
	
	public String getNcl() {
		return name;
	}
	
	public void setNcl(String ncl) {
		String temp = ncl;
	}
	
	//注意标准的getset方法,set方法必须没有返回值,get方法必须有返回值,否则Introspector.getBeanInfo就识别不出来了
//	public String setNcl(String ncl) {
//		String temp = ncl;
//		return temp;
//	}

	public static void main(String[] args) throws IntrospectionException {
		
		//要枚举一个JavaBean的所有属性，可以直接使用Java核心库提供的Introspector：
		BeanInfo info1 = Introspector.getBeanInfo(JavaBean.class);
        for (PropertyDescriptor pd : info1.getPropertyDescriptors()) {
            System.out.println(pd.getName());//注意class属性是从Object继承的getClass()方法带来的。
            System.out.println("  " + pd.getReadMethod());
            System.out.println("  " + pd.getWriteMethod());
        }
        
        System.out.println("---------------------------------");
        
		//要枚举一个JavaBean的所有属性，可以直接使用Java核心库提供的Introspector：
		BeanInfo info = Introspector.getBeanInfo(PersonDemo.class);
        for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
            System.out.println(pd.getName());//注意class属性是从Object继承的getClass()方法带来的。
            System.out.println("  " + pd.getReadMethod());
            System.out.println("  " + pd.getWriteMethod());
        }
	}
}
