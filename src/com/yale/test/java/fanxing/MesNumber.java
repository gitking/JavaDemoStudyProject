package com.yale.test.java.fanxing;

public class MesNumber<T extends Number> {//这里不能用super
	private T node;

	public T getNode() {
		return node;
	}

	public void setNode(T node) {
		this.node = node;
	}
}
