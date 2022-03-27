package com.yale.test.java.fanshe.perfma.zhihu;
/*
 * https://www.zhihu.com/question/51244545/answer/126055789
 * Java 中, 为什么一个对象的实例方法在执行完成之前其对象可以被 GC 回收?
 * RednaxelaFX 的回答
 */
public class B {
	private C c = new C();
	void clearC() {
		this.c = null;
	}
	
	public int val() {
		System.out.println("GC开始垃圾回收了");
		System.gc();// the A instance is queued into finalizer queue
		System.runFinalization(); // A's finalize() is ensured to run
		System.out.println("上面GC垃圾回收完成之后,this.c的对象就变成了null。因为clearC的方法在A的finalize()里面被调用了");
		C cc = this.c;
		return cc.val();// now this.c is null
	}
}
