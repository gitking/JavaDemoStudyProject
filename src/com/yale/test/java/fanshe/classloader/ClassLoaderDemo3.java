package com.yale.test.java.fanshe.classloader;

/**
 * https://mp.weixin.qq.com/s?__biz=MzU2NzAzMjQyOA==&mid=2247483988&idx=1&sn=5b34b96a5312f2687dd28bce39d542f4&chksm=fca22d57cbd5a441db81097d265556c18cdb386e1ea456e607e78f9a629a4ce9033947d19721&scene=21#wechat_redirect
 * 本题考查的知识点为【类加载的顺序】。一个类从被加载至 JVM 到卸载出内存的整个生命周期为：
 * ClassLoaderDemo3原理跟ClassLoaderDemo2的原理是一样的,唯一的区别就是代码是从上到下依次执行的。
 * @author issuser
 */
public class ClassLoaderDemo3 {
	private static int a = 0;
	private static int b;
	private ClassLoaderDemo3() {
		a++;
		b++;
	}
	
	private static ClassLoaderDemo3 cld = new ClassLoaderDemo3();
	public static ClassLoaderDemo3 getInstance() {
		return cld;
	}
	
	public int getA() {
		return a;
	}
	
	public int getB() {
		return b;
	}
	
	public static void main(String[] args) {
		ClassLoaderDemo3 cld = ClassLoaderDemo3.getInstance();
		System.out.println("类加载原理，A的值为:" + cld.getA());
		System.out.println("类加载原理，B的值为:" + cld.getB());
	}
}
