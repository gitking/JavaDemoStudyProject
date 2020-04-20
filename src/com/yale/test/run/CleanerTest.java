package com.yale.test.run;


/**
 * 但是从JDK1.9开始,这一操作已经不建议使用了,而对于对象回收释放.从JDK1.9开始建议开发者使用AutoCloseable
 * 或者使用java.lang.ref.Cleaner类进行回收(Cleaner也支持有AutoCloseable处理).
 * Cleaner实际上是重新启用一个线程来做回收的
 * 在新一代的清除回收处理的过程之中,更过的情况下考虑的是多线程的使用,即:为了防止有可能造成的延迟,
 * 所以许多对象回收前的处理都是单独通过一个线程完成的。
 */
public class CleanerTest {
	public static void main(String[] args) {
		Cleaner cleaner =  Cleaner.create();
	}
}
