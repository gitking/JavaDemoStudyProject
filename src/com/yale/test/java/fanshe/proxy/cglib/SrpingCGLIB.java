package com.yale.test.java.fanshe.proxy.cglib;

/*
 * 无论是使用AspectJ语法，还是配合Annotation，使用AOP，实际上就是让Spring自动为我们创建一个Proxy，使得调用方能无感知地调用指定方法，
 * 但运行期却动态“织入”了其他逻辑，因此，AOP本质上就是一个代理模式。
 * 因为Spring使用了CGLIB来实现运行期动态创建Proxy，如果我们没能深入理解其运行原理和实现机制，就极有可能遇到各种诡异的问题。
 * CGlib是基于继承重写实现的代理，因此要求Class必须是非final class 与此同时被代理的方法必须是非final方法，因此final方法无法被子类重写，因此就无法代理。
 * 基于JDK代理与基于CGLIB代理的代理类生成本质都是基于继承重写实现的（实现接口可以认为是一种特殊的继承）；对于static成员方法是无法子类重写的，static是归属于class所属。
 * 至此：由于Spring使用的是JDK与CGLIB这两种方式实现AOP，因此结论就是Spring无法支持static方法的代理增强。
 * Spring是否支持对静态方法进行Aop增强：https://blog.csdn.net/Dax1n/article/details/105684685
 * https://zhuanlan.zhihu.com/p/131584403
 * https://www.liaoxuefeng.com/wiki/1252599548343744/1281319432618017
 */
public class SrpingCGLIB {
	public static void main(String[] args) {

	}
}
