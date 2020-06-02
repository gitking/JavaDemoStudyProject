package com.yale.test.java.fanshe.annotation;

import java.io.Serializable;
import java.lang.annotation.Annotation;

/**
 * Annotation这项技术可以说是颠覆性的开发技术
 * annotation这些注解可以定义在类活方法上,而且现在也知道有反射的概念了,可以通过反射取得所定义的annotation信息
 * 在java.lang.reflect.AccessibleObject类和java.lang.Class中都提供了有如下的与annotation有关的方法:
 * 取得全部的annotation: public Annotation[] getAnnotation();
 * 取得指定的annotation: public <T extends Annotation> T getAnnotation(Class<T> annotationClass)
 * jdk8的官方文档:https://docs.oracle.com/javase/8/docs/api/index.html
 * @author dell
 */
public class AnnotationFanshe {
	public static void main(String[] args) throws NoSuchMethodException, SecurityException {
		Annotation ant[] = Member.class.getAnnotations();
		for (int x=0;x<ant.length; x++) {
			System.out.println("这里只会出现一个annotation,Member类上定义了来个annotation,这其实跟annotation的范围有关系" + ant[x]);
		}
		System.out.println("********************");
		System.out.println("Annotation本身有自己的保存范围,不同的Annotation范围也不同。所以上面只出现了一个annotation信息");
		System.out.println("取得方法上面的annotation");
		
		System.out.println("********************");

		Annotation methodAnt[] = Member.class.getMethod("toString").getAnnotations();
		for (int x=0;x<methodAnt.length; x++) {
			System.out.println("这里只会出现一个annotation,Member类上定义了来个annotation,这其实跟annotation的范围有关系" + methodAnt[x]);
		}
		
		System.out.println("********************");

		System.out.println("反射可以取得结构上定义的Annotation,所以所谓的Annotation的设计是不可能离开反射的");

	}
}
@SuppressWarnings("serial")
@Deprecated
class Member implements Serializable {
	@Override
	@Deprecated
	public String toString() {
		return super.toString();
	}
}
