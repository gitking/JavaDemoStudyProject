package com.yale.test.java.fanshe.perfma.zhihu;

/*
 * https://www.zhihu.com/question/51244545/answer/126055789
 * Java 中, 为什么一个对象的实例方法在执行完成之前其对象可以被 GC 回收?
 * RednaxelaFX 的回答
 */
public class A {
	private B b = new B();
	
	@Override
	public void finalize() {
		System.out.println("这个方法被调用了吗?????,这个方法被调用了,说明A这个对象被回收了");
		this.b.clearC();
	}
	
	public int val() {
		return this.b.val();
	}
}
