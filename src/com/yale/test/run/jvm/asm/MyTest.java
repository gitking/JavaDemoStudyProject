package com.yale.test.run.jvm.asm;

public class MyTest {
	public static void main(String[] args) {
		/*
		 * 先运行Generator再运行这里,
		 * 就能看到增强后的Base类
		 */
		Base base = new Base();
		base.process();
	}
}
