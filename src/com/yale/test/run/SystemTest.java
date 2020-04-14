package com.yale.test.run;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class SystemTest {

	public static void main(String[] args) {
		//System.arraycopy(src, srcPos, dest, destPos, length);数组复制
		System.out.println(System.currentTimeMillis());
		
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
	}
}
