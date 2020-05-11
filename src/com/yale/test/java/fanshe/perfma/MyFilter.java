package com.yale.test.java.fanshe.perfma;

import sun.jvm.hotspot.oops.InstanceKlass;
import sun.jvm.hotspot.tools.jcore.ClassFilter;

/**
 * 首先编写一个自定义的过滤器。只要实现sun.jvm.hotspot.tools.jcore.ClassFilter接口即可。
 * sun.jvm.hotspot.tools.jcore.ClassFilter 这个类在jdk的lib目录下的sa-jdi.jar包里面
 * @author dell
 */
public class MyFilter implements ClassFilter{

	/**
	 * InstanceKlass对应于HotSpot中表示Java类的内部对象。Sun JDK为反射调用生成的类的名字形如sun/reflect/GeneratedMethodAccessorN，其中N是一个整数；
	 * 所以只要看看类名是否以"sun/reflect/GeneratedMethodAccessor"开头就能找出来了。留意到这里包名的分隔符是“/”而不是“.”，
	 * 这是Java类在JVM中的“内部名称”形式，参考Java虚拟机规范第二版4.2小节(https://docs.oracle.com/javase/specs/#14757)。
	 * SA自带了一个能把当前在HotSpot中加载了的类dump成Class文件的工具，称为ClassDump。
	 * 它的全限定类名是sun.jvm.hotspot.tools.jcore.ClassDump，有main()方法，可以直接从命令行执行；
	 */
	@Override
	public boolean canInclude(InstanceKlass kls) {
		String klassName = kls.getName().asString();
		return klassName.startsWith("sun/reflect/GeneratedMethodAccessor");
	}
}
