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
			Method method = System.out.getClass().getMethod("println", String.class);
			for (int i=0; i<16; i++) {
				method.invoke(System.out, "demo");
			}
			System.in.read();//把线程阻塞在这里
			/**
			 * 让Demo跑起来，然后先不要让它结束。通过jps工具看看它的进程ID是多少：

			 * 接下来执行ClassDump，指定上面自定义的过滤器（过滤器的类要在classpath上，本例中它在./bin）：
			 * [sajia@sajia class_dump]$ java -classpath ".:./bin:$JAVA_HOME/lib/sa-jdi.jar" -Dsun.jvm.hotspot.tools.jcore.filter=MyFilter sun.jvm.hotspot.tools.jcore.ClassDump 20542 
			 * 执行结束后，可以看到dump出了一个Class文件，在./sun/reflect/GeneratedMethodAccessor1.class；.是默认的输出目录，后面的目录结构对应包名。
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
