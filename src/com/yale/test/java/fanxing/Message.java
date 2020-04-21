package com.yale.test.java.fanxing;

/**
 * 在子类实现接口的时候也可以明确给出具体类型
 * @author dell
 *
 * @param <T>
 */
public class Message<T> implements IMessage<T> {
	@Override
	public void print(T t) {
		System.out.println(t.toString());
	}
	
	private T node;

	public T getNode() {
		return node;
	}

	public void setNode(T node) {
		this.node = node;
	}
}
