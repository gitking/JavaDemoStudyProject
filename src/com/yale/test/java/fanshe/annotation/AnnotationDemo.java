package com.yale.test.java.fanshe.annotation;

import java.io.File;
import java.util.ArrayList;

/**
 * Annotation要想学习Annotation需要利用反射技术,
 * Annotation可以说是整个JDK发展的一项重要技术,因为从现在的开发来讲,Annotation的使用已经变得广谱化,只要有项目基本上都会有Annotation的出现。
 * 阿里云 魔乐科技  课时18 Annotation（代码开发逻辑）
 * 注释会被编译器直接忽略，注解则可以被编译器打包进入class文件，因此，注解是一种用作标注的“元数据”。
 * 从JVM的角度看，注解本身对代码逻辑没有任何影响，如何使用注解完全由工具决定。
 * Java的注解可以分为三类：
	第一类是由编译器使用的注解，例如：
	    @Override：让编译器检查该方法是否正确地实现了覆写；
	    @SuppressWarnings：告诉编译器忽略此处代码产生的警告。
	这类注解不会被编译进入.class文件，它们在编译后就被编译器扔掉了。
	第二类是由工具处理.class文件使用的注解，比如有些工具会在加载class的时候，对class做动态修改，实现一些特殊的功能。
	这类注解会被编译进入.class文件，但加载结束后并不会存在于内存中。这类注解只被一些底层库使用，一般我们不必自己处理。
	第三类是在程序运行期能够读取的注解，它们在加载后一直存在于JVM中，这也是最常用的注解。例如，一个配置了@PostConstruct的方法会在调用
	构造方法后自动被调用（这是Java代码读取该注解实现的功能，JVM并不会识别该注解）。
 *	反射工具类 MethodUtils  org.apache.commons.beanutils.MethodUtils    commons-beanutils-1.8.3.jar 
 *	反射工具类 MethodUtils  org.apache.commons.lang.reflect.MethodUtils commons-lang-2.6.jar
 * @author dell
 */
public class AnnotationDemo {
	public static void main(String[] args) {
		System.out.println("方法覆写(重写):发生继承关系之中,子类定义了与父类的方法名称相同,参数类型及个数,返回值类型相同放的时候称为方法"
				+ "的覆写,被覆写的方法不能够拥有比父类更为严格的访问控制权限。");
		System.out.println("这里的期望结果是调用AnnotationDemo覆写后的toString方法(但是没有执行):" + new AnnotationDemo());
		@SuppressWarnings("unused")//unused的意思就是没使用的啥意思
		File file = new File("");//这里我创建了一个对象,但是我没有用,eclipse会出现黄线警告,按ctrl+1 可以使用@SuppressWarnings注解压制警告
		
		@SuppressWarnings({ "rawtypes", "unused" })//rawtypes原生类型,因为我没有使用泛型,unused是压制未使用警告
		ArrayList sdf = new ArrayList();
		
		System.out.println("@SuppressWarnings也可以使用到main方法上面,压制方法内的所有警告");
	}
	
	/**
	 * 现在是希望进行toString()的覆写,但遗憾的是由于你的输入错误,导致方法覆写错误，
	 * 这个问题在程序编译的时候根本就无法显示出来,编译不会提示你(加上Annotation才会提示错误)
	 */
	@Deprecated//@Deprecated是过期的意思,不建议后来人使用了,但是注意这个方法,仍然可以使用.但是最好提供替代方法
	public String tostring(){//现在希望可以进行toString()方法覆写
		return "是一个人";
	}
	
	@Override//有了这个注解,能保证方法一定覆写成功,写错一个字母都会编译报错
	public String toString() {
		return super.toString();
	}
}
