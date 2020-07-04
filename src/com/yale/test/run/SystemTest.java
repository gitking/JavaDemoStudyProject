package com.yale.test.run;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class SystemTest {

	public static void main(String[] args) {
		//System.arraycopy(src, srcPos, dest, destPos, length);数组复制
		System.out.println(System.currentTimeMillis());
		
		/**
		 * 常见的编码有下面这些
		 * GBK,GB2312:表示的是国标编码,GBK包含的有简体中文和繁体中文,而GB2312只包含 有简体中文,也就是说这俩个编码都是描述中文的编码;
		 * UNICODE编码:是java提供的十六进制编码,可以描述世界上任意的文字信息,但是如果每个字符都用十六进制编码太浪费了。UTF-8应运而生
		 * UTF-8:是可变的编码
		 * ISO8859-1:是国际通用编码,但是所有的编码都需要进行转换。
		 * https://edu.aliyun.com/lesson_36_484#_484  阿里云 课程《【名师课堂】Java高级开发 》MLDN魔乐科技 课时82 字符编码（常用字符编码）
		 * 2020年4月19日16:37:33
		 */
		System.out.println("java文件编码：" + System.getProperty("file.encoding"));
		System.out.println("" + System.getProperty("sun.reflect.noInflation"));
		System.out.println("" + System.getProperty("sun.reflect.inflationThreshold"));
		
		System.out.println("System.getProperty获取的是JVM启动时通过-D指定的属性:" + System.getProperty("fuckoff"));

		
		
		System.out.println("打印JAVA所有的配置信息,打印JVM所有的配置信息,打印JVM所有的环境变量信息");
		System.getProperties().list(System.out);
		
		System.out.println("--------------------------------------------");
		
		String procCmd = System.getenv("windir");
		System.out.println("windows的默认目录为:" + procCmd);
		
		System.out.println("--------------------------------------------");

		Set<Entry<String, String>> envEntry = System.getenv().entrySet();
		Iterator iterator = envEntry.iterator();
		while(iterator.hasNext()) {
			Entry<String, String> entry = (Entry<String, String>) iterator.next();
			System.out.println("windows环境变量:" + entry.getKey() + ":" + entry.getValue());
		}
		
		System.out.println("--------------------------------------------***");
		System.out.println();
		for(Map.Entry<String, String> me: System.getenv().entrySet()) {
			System.out.println("windows环境变量:" + me.getKey() + ":" + me.getValue());
		}
		
		
 		System.out.println(System.identityHashCode(20)); 

	}
}
