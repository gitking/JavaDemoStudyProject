package com.yale.test.java.fanshe.annotation;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation这项技术可以说是颠覆性的开发技术
 * annotation这些注解可以定义在类活方法上,而且现在也知道有反射的概念了,可以通过反射取得所定义的annotation信息
 * 在java.lang.reflect.AccessibleObject类和java.lang.Class中都提供了有如下的与annotation有关的方法:
 * 取得全部的annotation: public Annotation[] getAnnotation();
 * 取得指定的annotation: public <T extends Annotation> T getAnnotation(Class<T> annotationClass)
 * jdk8的官方文档:https://docs.oracle.com/javase/8/docs/api/index.html
 * 阿里云 mldn 课时113：反射与Annotation（反射取得Annotation）
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
		
		/**
		 * 想自定义Annotation,那么首先要解决的就是Annotation的作用范围,这些范围就在一个枚举类(RetentionPolicy)定义:
		 * public static final RetentionPolicy CLASS:是在类定义的时候出现的Annotation
		 * public static final RetentionPolicy SOURCE:在源代码中出现的Annotation
		 * public static final RetentionPolicy RUNTIME:是在类执行的时候允许出现的annotation
		 */

		//取一个类上面某一个具体的Annotation
		MyAnnotation myAnt = Member.class.getDeclaredAnnotation(MyAnnotation.class);
		System.out.println("自定义annotation:" + myAnt.name());
		System.out.println("自定义annotation:" + myAnt.age());
		/**
		 * 你可以在Annotation里面编写许多的属性信息,同时也可以定义许多有意义的Annotation,但是要请千万要记住一点:
		 * Annotation的使用需要特殊环境,不是随便编写的。
		 */

	}
}
@SuppressWarnings("serial")
@Deprecated
@MyAnnotation(name="mldn", age = 10)
class Member implements Serializable {
	@Override
	@Deprecated
	public String toString() {
		return super.toString();
	}
}
/**
 * @interface用来定义自己的Annotation
 * @author dell
 *
 */
@Retention(RetentionPolicy.RUNTIME)//表示此Annotation在运行时生效
@interface MyAnnotation{
	public String name() default "mldn";//定义属性
	public int age();//这里如果不用default的话,使用MyAnnotation这个自定义的Annotation的时候,就必须给age赋值,不赋值会报错
}