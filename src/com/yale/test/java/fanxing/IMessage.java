package com.yale.test.java.fanxing;

/**
 * 泛型接口
 * @author dell
 *
 * @param <T>
 */
public interface IMessage<T> {
	public void print(T t);
}
