package com.yale.test.run;

/*
 * https://club.perfma.com/article/1835499?from=groupmessage#/article/1835499?from=groupmessage
 * JVM finalize实现原理与由此引发的血案 https://sq.163yun.com/blog/article/198141339137806336
 * java.util.zip.ZipFile$ZipFileInflaterInputStream，赶紧Google发现还是有许多小伙伴碰到相同的问题，例如：Java压缩流GZIPStream导致的内存泄露 。
 * Java压缩流GZIPStream导致的内存泄 https://www.jianshu.com/p/5841df465eb9
 */
public class FinalizeTest {
	private static FinalizeTest test;
	/**
	 * VM参数：-XX: +PrintGCDetails -Xmx=1M -Xms=1M
	 * https://yq.aliyun.com/articles/622667?spm=a2c4e.11155435.0.0.a53a7229tBmQD4
	 * @param args
	 */
	public static void main(String[] args) {
		test = new FinalizeTest();
		int _1m = 1024 * 1024;
		test = null;//设置为NULL便于回收
		System.gc();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		if (test != null) {
			System.out.println("首先,我还活着");
		} else {
			System.out.println("首先,我已经死了");
		}
		
		test = null;//由于test在finalize方法里面复活了,再次将test设置为null
		System.gc();//第二次GC不会调用finalize()方法
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		if (test != null) {
			System.out.println("第二,我还活着");
		} else {
			System.out.println("第二,我已经死了");
		}
	}
	
	/**
	 * finailize方法是Object类中定义的方法，意味着任何一个对象都有这个方法。但这个方法只会调用一次，如果把这个对象复活后再次让这个对象死亡，那第2次回收该对象的时候是
	 * 不会调用finailize方法的，而且优先级比较低，并不能保证一定会被执行，因此不建议使用finalize方法。总结起来就是3个特性： ①、GC之前被调用 。②、只会被调用一次。
	 * ③、不可靠，不能保证被执行，不建议使用
	 */
	@Override
	protected void finalize() throws Throwable {
		test = this;
		System.out.println("垃圾回收时会自动调用此方法,此方法只会被调用一次");
		super.finalize();
	}
}
