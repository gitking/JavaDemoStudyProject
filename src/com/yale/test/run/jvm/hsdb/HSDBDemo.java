package com.yale.test.run.jvm.hsdb;

import java.io.IOException;

/**
 * 第一次使用HSDB
 * HSDB（Hotspot Debugger），JDK自带的工具，用于查看JVM运行时的状态。
 * HSDB位于C:\Program Files\Java\jdk1.8.0_212\lib里面，接下来启动HSDB：
 * D:\Program Files\Java\jdk1.8.0_281\lib>java -cp ./sa-jdi.jar sun.jvm.hotspot.HSDB
 * https://www.cnblogs.com/alinainai/p/11070923.html
 * 借HSDB来探索HotSpot VM的运行时数据
 * https://www.iteye.com/blog/rednaxelafx-1847971
 * 通过HSDB来了解String值的真身在哪里
 * http://lovestblog.cn/blog/2014/06/28/hsdb-string/
 * @author issuser
 */
public class HSDBDemo {
	public static void main(String[] args) {
		try {
			TestA ta = new TestB();
			System.out.println(ta);
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
