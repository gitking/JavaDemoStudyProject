package com.yale.test.java.fanxing;

public class MesNumber<T extends Number> {//这里不能用super
	private T node;
	
	public MesNumber(Class<T> clazz) throws InstantiationException, IllegalAccessException{
		/*
		 * 局限四：不能实例化T类型：
		 * 要实例化T类型，我们必须借助额外的Class<T>参数：
		 * 代码借助Class<T>参数并通过反射来实例化T类型，使用的时候，也必须传入Class<T>。例如：
		 * MesNumber<Integer> pair = new MesNumber<>(Integer.class);
		 */
		//node = new T();
		node = clazz.newInstance();
	}

	
	public MesNumber() {
	}
	public T getNode() {
		return node;
	}

	public void setNode(T node) {
		this.node = node;
	}
}
