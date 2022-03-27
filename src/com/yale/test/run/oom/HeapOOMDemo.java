package com.yale.test.run.oom;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * https://www.kaikeba.com/open/item?c=721&appid=wx5046bc7413796142&openid=o7xuUuAfuktQNsftBtdEknN8RC8w&courseCode=3maiblkjxa&channelCode=lxzd8fi0be
 * @author issuser
 */
public class HeapOOMDemo {
	public static void main(String[] args) {
		directBufferOOMDemo();
		try {
			touchOOM_Metaspace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		//int[] arr = new int[Integer.MAX_VALUE - 2];//Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
		int[] arr1 = new int[Integer.MAX_VALUE];//Exception in thread "main" java.lang.OutOfMemoryError: Requested array size exceeds VM limit
	}
	
	/**
	 * java -XX:+HeapDumpOnOutOfMemoryError -XX:-UseCompressedOops -XX:MaxMetaspaceSize=9m HeapOOMDemo
	 * 触发java.lang.OutOfMemoryError: Metaspace这个错误
	 * 去掉-XX:-UseCompressedOops(压缩指针) 这个参数之后 java -XX:+HeapDumpOnOutOfMemoryError -XX:MaxMetaspaceSize=9m HeapOOMDemo
	 * 会触发java.lang.OutOfMemoryError: Compressed class space 这个错误
	 * UseCompressedOops(压缩指针)是在64位操作系统上独有的东西,JAVA是为了在一定程度上面节约内存空间的使用。所以JAVA在64位的操作系统上面去映射了一块32位地址的地址映射空间。
	 * -XX:-UseCompressedOops(压缩指针)关闭压缩指针之后，JVM不需要去开辟一块特殊的区域来存储。当启用压缩指针之后，JVM就一定会开辟一块特殊的连续区域来做指针映射。这其实是
	 * 跟操作系统的寻址有关的。
	 * UseCompressedClassPointers(是否压缩类指针),在JAVA8里面当堆空间小于32G的时候，默认都是打开的。当堆空间小于4G的时候，只有UseCompressedOops会被默认打开。
	 * JVM认为这样做整体上会节约更多的内存空间。如果压缩指针被使用的时候，实际上JVM会单独开辟一块内存空间专门用来存放32位的指针所映射的一块区域。因为在这种情况下对象指针和类指针
	 * 其实32位和64位是混用的。所以JVM需要一个统一的规则，来让JAVA通过32位的指针来找到这个类所在的具体位置。
	 * 可以通过设置CompressedClassSpaceSize这样一个参数来增加他的区域。CompressedClassSpaceSize的默认大小是1G。
	 * @throws MalformedURLException
	 */
	public static void touchOOM_Metaspace() throws MalformedURLException {
		URL[] jar = new URL[] {new File("D:\\GitWorkSpace\\JavaDemoStudyProject\\WebContent\\WEB-INF\\lib\\fastjson-1.2.74.jar").toURI().toURL()};
		//file:/D:/GitWorkSpace/JavaDemoStudyProject/WebContent/WEB-INF/lib/commons-logging-1.2.jar
		System.out.println(jar[0]);
		for (;;) {
			try {
				Thread.sleep(1000000000000l);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			/**
			 * 类加载器如果没有被垃圾回收,那么这个类加载器加载的类就也无法被垃圾回收。这样就容易导致java.lang.OutOfMemoryError: Metaspace这个错误
			 */
//			DemoClassLoader loader = new DemoClassLoader(jar);//自定义的类加载器
//			Class clazz = loader.loadClass("com.alibaba.fastjson.JSONArray");
//			Object o = clazz.newInstance();
//			loaders.add(o);
		}
	}
	
	public static final List<ByteBuffer> buffers = new ArrayList<>();
	/**
	 * Exception in thread "main" java.lang.OutOfMemoryError: Direct buffer memory
	 * java -XX:MaxDirectMemorySize=64m HeapOOMDemo
	 * 注意这种报错是不会触发HeapDump的，因为这不是JAVA的堆内存泄漏了,是堆外内存满了,-XX:+HeapDumpOnOutOfMemoryError 这个参数在这种报错情况下是没有用的.
	 */
	public static void directBufferOOMDemo() {
		for(;;) {
			buffers.add(ByteBuffer.allocateDirect(64 * 1024));
		}
	}
}
