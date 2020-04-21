package com.yale.test.java.fanxing;

/**
 * 泛型类型只允许引用类型(类和接口,不能使用基本数据类型)
 * @author dell
 *
 * @param <T>
 */
public class MessageNumberImpl<T extends Number> implements IMessage<T> {
	@Override
	public void print(T t) {
		System.out.println(t);
	}
}
