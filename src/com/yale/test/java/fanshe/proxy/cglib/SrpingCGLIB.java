package com.yale.test.java.fanshe.proxy.cglib;

/*
 * 无论是使用AspectJ语法，还是配合Annotation，使用AOP，实际上就是让Spring自动为我们创建一个Proxy，使得调用方能无感知地调用指定方法，
 * 但运行期却动态“织入”了其他逻辑，因此，AOP本质上就是一个代理模式。
 * 因为Spring使用了CGLIB来实现运行期动态创建Proxy，如果我们没能深入理解其运行原理和实现机制，就极有可能遇到各种诡异的问题。
 * https://zhuanlan.zhihu.com/p/131584403
 * https://www.liaoxuefeng.com/wiki/1252599548343744/1281319432618017
 */
public class SrpingCGLIB {
	public static void main(String[] args) {

	}
}
