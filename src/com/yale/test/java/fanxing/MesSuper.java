package com.yale.test.java.fanxing;

public class MesSuper<T> {
	private T node;

	public T getNode() {
		return node;
	}

	public void setNode(T node) {
		this.node = node;
	}
	
	/*
	 * 不恰当的覆写方法
	 * 有些时候，一个看似正确定义的方法会无法通过编译。例如：
	 * 这是因为，定义的equals(T t)方法实际上会被擦拭成equals(Object t)，而这个方法是继承自Object的，编译器会阻止一个实际上会变成覆写的泛型方法定义。
	 * 换个方法名，避开与Object.equals(Object)的冲突就可以成功编译：
	 */
//	public boolean equals(T t) {
//        return this == t;
//    }
	public boolean same (T t) {
	  return this == t;
	}
}
