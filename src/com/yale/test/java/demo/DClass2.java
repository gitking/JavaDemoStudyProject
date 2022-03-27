package com.yale.test.java.demo;

/*
 * 这个类DClass2的defaultField属性只能在com.yale.test.java.demo这个包下面访问,出了这个包你就访问不到这个属性了。
 * 在别的包访问这个属性直接编译报错.见ProtectedTest.java里面的例子
 */
public class DClass2 {
	int defaultField;//
	
	public int defaultAndPublicField;
	
	public void testDC() {
		System.out.println(this.defaultField);
		System.out.println(this.defaultAndPublicField);
	}
}
