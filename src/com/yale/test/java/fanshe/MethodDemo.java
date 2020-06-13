package com.yale.test.java.fanshe;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class Per {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
/**
 * 【强制】避免用Apache Beanutils进行属性的copy。 
 * 说明：Apache BeanUtils性能较差，可以使用其他方案比如Spring BeanUtils, Cglib BeanCopier，注意均是浅拷贝。《阿里巴巴Java开发手册（泰山版）.
 * @author dell
 */
public class MethodDemo {

	public static void main(String[] args) throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException {
		//Class<?> cls = Per.class;这个更下面这个是一样的
		Class<?> cls = Class.forName("com.yale.test.java.fanshe.Per");
		Method[] met = cls.getMethods();
		for (int i=0;i<met.length;i++) {
			System.out.println("这里会把Object父类的方法也打印出来:" + met[i]);
		}
		String attribute = "name";
		String value = "MLDN";
		/**
		 * 取得setName这个方法的实例化对象,设置方法的名称和参数类型
		 * setName()是方法名称,但是这个方法名称是我根据给定的属性信息拼凑得来的,同时该方法
		 * 需要接收一个String类型的参数
		 */
		Method setMethod = cls.getMethod("set" + initcap(attribute), String.class);
		Object obj = cls.newInstance();//任何情况下调用类中的普通方法都必须有实例化对象
		//随后需要通过Method类对象调用指定的方法,调用方法必须有实例化对象,同时要传入一个参数
		setMethod.invoke(obj, value);//相当于:Person对象.setName(value)
		
		Method getMethod = cls.getMethod("get" + initcap(attribute));
		Object ret = getMethod.invoke(obj);//相当于Person对象.getName()
		System.out.println("通过反射调用对象的方法得到的返回值" + ret);
	}
	public static String initcap(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1);
		
	}
}
