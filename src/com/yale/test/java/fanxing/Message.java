package com.yale.test.java.fanxing;

public class Message<T> implements IMessage<T> {
	@Override
	public void print(T t) {
		System.out.println(t.toString());
	}
}
