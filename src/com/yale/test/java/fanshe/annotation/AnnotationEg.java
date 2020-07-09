package com.yale.test.java.fanshe.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

public class AnnotationEg {

	@Range(min=1, max=20)
	public String name;
	
	@Range(max=10)
	public String city;
	
	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException {
		/*
		 * 注解如何使用，完全由程序自己决定。例如，JUnit是一个测试框架，它会自动运行所有标记为@Test的方法。
		 * 我们来看一个@Range注解，我们希望用它来定义一个String字段的规则：字段长度满足@Range的参数定义：
		 * 但是，定义了注解，本身对程序逻辑没有任何影响。我们必须自己编写代码来使用注解。这里，我们编写一个Person实例的检查方法，
		 * 它可以检查Person实例的String字段长度是否满足@Range的定义：
		 * 这样一来，我们通过@Range注解，配合check()方法，就可以完成Person实例的检查。
		 * 注意检查逻辑完全是我们自己编写的，JVM不会自动给注解添加任何额外的逻辑。
		 */
		AnnotationEg ae = new AnnotationEg();
		ae.name="";
		ae.check(ae);
	}
	
	void check(AnnotationEg ae) throws IllegalArgumentException, IllegalAccessException {
		for (Field field : ae.getClass().getFields()) {
			Range range = field.getAnnotation(Range.class);
			if (range != null) {
				Object value = field.get(ae);
				if (value instanceof String) {
					int len = ((String) value).length();
					if (len < range.min() || len > range.max()) {
						throw new IllegalArgumentException("Invalid field:" + field.getName());
					}
				}
			}
		}
	}

}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface Range {
	int min() default 0;
	int max() default 255;
}
