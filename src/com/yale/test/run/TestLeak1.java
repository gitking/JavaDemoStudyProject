package com.yale.test.run;

import java.util.concurrent.ConcurrentLinkedQueue;

/*
 * ConcurrentLinkedQueue，这个队列。memory leak，内存泄漏。链表内存泄露
 * https://club.perfma.com/article/2041676?type=sub&last=2049632
 * https://bugs.java.com/bugdatabase/view_bug.do?bug_id=8137185
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=477817
 * https://bugs.eclipse.org/bugs/attachment.cgi?id=256704
 * JDK官方修复方法:http://hg.openjdk.java.net/jdk8u/jdk8u-dev/jdk/rev/8efe549f3c87
 * jconsole、VisualVM、jmc
 * 
 * https://cloud.tencent.com/developer/article/1129904 今咱们来聊聊JVM 堆外内存泄露的BUG是如何查找的
 * https://developer.aliyun.com/article/657160 一次堆外内存泄露的排查过程
 * https://club.perfma.com/article/1835499?from=groupmessage#/article/1835499?from=groupmessage
 * 难道是linux glibc 中经典的 64M 内存问题?一次 Java 进程 OOM 的排查分析（glibc 篇） https://club.perfma.com/article/1709425?last=1714757&type=parent#/article/1709425?last=1714757&type=parent
 * JAVA堆外内存排查小结 https://zhuanlan.zhihu.com/p/60976273
 * netty 堆外内存泄露排查盛宴 https://mp.weixin.qq.com/s/fxx_AxH2mbtsr5BgdZ6uFA  https://github.com/mrniko/netty-socketio
 */
public class TestLeak1 {
	public static void main(String[] args) {
		
		/*
		 * 这段代码在jdk1.8.0_251上运行,30秒就运行完了
		 * 在jdk1.6.0_45上运行,要运行24个小时，都运行不完.
		 */
		ConcurrentLinkedQueue<Object> queue = new ConcurrentLinkedQueue<Object>();
		queue.add(new Object());//jdk1.6.0_45把这行注释掉运行的也很快,不知道啥原因
		queue.add(new Object());//jdk1.6.0_45把这行注释掉运行的也很快,不知道啥原因
		queue.add(new Object());//jdk1.6.0_45把这行注释掉运行的也很快,不知道啥原因
		queue.add(new Object());//jdk1.6.0_45把这行注释掉运行的也很快,不知道啥原因
		queue.add(new Object());//jdk1.6.0_45把这行注释掉运行的也很快,不知道啥原因
		/*
		 * jdk1.6.0_4,上会发生内存泄露是因为,你要删除队列里面的最后一个元素时才会这样,但是列表里面的元素最少要有俩个,然后就你去删除最后一个
		 * 上面的queue.add(new Object());这行代码最少有一个,然后再循环里面循环添加和删除同一个元素,也就是循环添加和删除最后一个元素
		 */
		Object object = new Object();
		int loops = 0;
		Runtime rt = Runtime.getRuntime();
		long last = System.currentTimeMillis();
		while (loops < 1000000000) {
			if (loops % 10000 ==0) {
				long now = System.currentTimeMillis();
				long duration = now - last;
				last = now;
				System.err.printf("duration=%d,q.size=%d, memory max = %d, free=%d, totol=%d%n", duration, queue.size(), rt.maxMemory(), rt.freeMemory(), rt.totalMemory());
			}
			queue.add(object);
			queue.remove(object);
			loops++;
		}
		System.out.println("循环了:" + loops);
		System.out.println("链表的长度应该为1:" + queue.size());
		System.out.println(System.getProperty("java.version"));
		System.out.println(System.getProperty("java.vm.version"));
		System.out.println(System.getProperty("java.vm.vendor"));
		System.out.println(System.getProperty("java.vm.name"));
		System.out.println(System.getProperty("java.runtime.version"));
		System.out.println(System.getProperty("java.vm.specification.vendor"));
	}
}
