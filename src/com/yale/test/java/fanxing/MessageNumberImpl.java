package com.yale.test.java.fanxing;

public class MessageNumberImpl<T extends Number> implements IMessage<T> {
	@Override
	public void print(T t) {
		System.out.println(t);
	}
}
