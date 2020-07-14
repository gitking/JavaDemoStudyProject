package com.yale.test.java.fanxing;

public class Person {
	/**
	 * 泛型方法可以定义在普通的类中
	 * 编写泛型类时，要特别注意，泛型类型<T>不能用于静态方法。例如：
	 * @param t
	 * @return
	 */
	public <T> T[] age (T[] t){
		return t;
	}
}
