package com.yale.test.java.fanxing;

/**
 * 描述一个位置的类
 * 泛型的出现时为了解决类型转换不安全的问题,使用泛型必须提前指定类型
 * @author lenovo
 * @param <T>
 */
public class Point<T> {//<>里面不是说只能写T,可以随便写,T,RP,ABC,都可以,也可以设置多个泛型<A,B,C >都可以
	public T x;
	public T y;
	public T getX() {
		return x;
	}
	public void setX(T x) {
		this.x = x;
	}
	public T getY() {
		return y;
	}
	public void setY(T y) {
		this.y = y;
	}
	@Override
	public String toString() {
		return this.getX() + "," + this.getY();
	}
}
