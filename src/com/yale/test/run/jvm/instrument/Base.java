package com.yale.test.run.jvm.instrument;

import java.lang.management.ManagementFactory;

public class Base {
	public void process() {
		System.out.println("instrument 插桩, 可以在 JVM运行中的时候 增强方法,process");
	}
	
	public static void main(String[] args) {
		String name = ManagementFactory.getRuntimeMXBean().getName();
		String s = name.split("@")[0];
		System.out.println("获取JVM的进程pid:" + s);//打印当前pid
		Base b = new Base();
		while (true) {
			try {
				Thread.sleep(5000L);
				b.process();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
