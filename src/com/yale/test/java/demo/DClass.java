package com.yale.test.java.demo;

/*
 * 这个类只能在com.yale.test.java.demo这个包下面访问,出了这个包你就访问不到这个类了。
 * 在别的包访问这个类直接编译报错.见ProtectedTest.java里面的例子
 * 一个Java源文件可以包含多个类的定义，但只能定义一个public类，且public类名必须与文件名一致。如果要定义多个public类，必须拆到多个Java源文件中。 
 */
class DClass {
	int defaultField;
	
	public int defaultAndPublicField;
	
	public void testDC() {
		System.out.println(this.defaultField);
		System.out.println(this.defaultAndPublicField);
	}
}
