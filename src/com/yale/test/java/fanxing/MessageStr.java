package com.yale.test.java.fanxing;

public class MessageStr implements IMessage<String> {
	@Override
	public void print(String t) {
		System.out.println(t);
	}
}
