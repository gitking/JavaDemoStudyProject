package com.yale.test.java.fanshe.perfma;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
/**
 * 如何dump出一个Java进程里的类对应的Class文件？
 * https://www.iteye.com/blog/rednaxelafx-727938
 * https://www.iteye.com/blog/rednaxelafx-548536
 * https://club.perfma.com/question/507734?from=timeline#/question/507734?from=timeline
 * @author dell
 */
public class Demo {
	public static void main(String[] args) {
		try {
			//D:\GitWorkSpace\JavaDemoStudyProject\bin>java -classpath ".;%JAVA_HOME%/lib/sa-jdi.jar" -Dsun.jvm.hotspot.tools.jcore.filter=com.yale.test.java.fanshe.perfma.MyFilter sun.jvm.hotspot.tools.jcore.ClassDump 11972
			Method method = System.out.getClass().getMethod("println", String.class);
			for (int i=0; i<16; i++) {
				method.invoke(System.out, "demo" + i);
			}
			System.in.read();//把线程阻塞在这里
			/**
			 * 让Demo跑起来，然后先不要让它结束。通过jps工具看看它的进程ID是多少：
			 * 接下来执行ClassDump，指定上面自定义的过滤器（过滤器的类要在classpath上，本例中它在./bin）：
			 * [sajia@sajia class_dump]$ java -classpath ".:./bin:$JAVA_HOME/lib/sa-jdi.jar" -Dsun.jvm.hotspot.tools.jcore.filter=MyFilter sun.jvm.hotspot.tools.jcore.ClassDump 20542 
			 * 执行结束后，可以看到dump出了一个Class文件，在./sun/reflect/GeneratedMethodAccessor1.class；.是默认的输出目录，后面的目录结构对应包名。
			 * 
			 * 本来想顺带演示一下用Java反编译器把例子里的Class文件反编译为Java源码的，但用了JD(http://java.decompiler.free.fr/)和Jad(https://varaneckas.com/jad/)
			 * 都无法正确识别这里比较特别的Exceptions属性表，只好人肉反编译写出来……识别不出来也正常，毕竟Java 7之前在Java源码这层是没办法对同一个异常处理器处理指定多个异常类型。
			 * 要深究的话，上面人肉反编译的Java文件跟实际Class文件还有些细节差异。
			 * 例如说JDK在生成Class文件时为了方便所以把一大堆“很可能会用到”的常量都写到常量池里了，但在代码里可能并没有用到常量池里的所有项；如果用javac编译Java源码就不会出现这种状况。
			 * 又例如生成的Class文件里一个局部变量也没用，locals=3之中三个都是参数：第一个是this，第二个是obj，第三个是args。求值的中间结果全部都直接在操作数栈上用掉了。而在Java源码里无法写出这样的代码，像是说try块不能从一个表达式的中间开始之类的。
			 * 这次就写到这里吧～
			 * A. Sundararajan有篇不错的文章也是讲如何从Java进程dump出Class文件的，使用的是JVMTI系的API：
			 * Retrieving .class files from a running app(http://blogs.sun.com/sundararajan/entry/retrieving_class_files_from_a)
			 * 然后也有一篇使用SA从core dump文件中dump出Class文件的文章：
			 * Retrieving .class files from a Java core dump(http://blogs.sun.com/sundararajan/entry/retrieving_class_files_from_a1)
			 */
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
