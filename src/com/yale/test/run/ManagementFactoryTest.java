package com.yale.test.run;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.ThreadMXBean;

/* *
 * https://cloud.tencent.com/developer/ask/43687
 * https://www.jianshu.com/p/2a8d6231d995
 */
public class ManagementFactoryTest {

	public static void main(String[] args) {
		MemoryMXBean mm = ManagementFactory.getMemoryMXBean();
		System.out.println("返回尚未完成终结处理的对象的大概数量：" + mm.getObjectPendingFinalizationCount());
		System.out.println("这个不知道是啥" + mm.getObjectName());
		
		MemoryUsage heapMemory = mm.getHeapMemoryUsage();
		System.out.println("返回用于对象分配的堆的当前内存使用情况:" + heapMemory);
		
		MemoryUsage mu = mm.getNonHeapMemoryUsage();
		System.out.println("返回Java虚拟机使用的非堆内存的当前内存使用情况:" + mu);
		
		System.out.println("这个俩个对象的地址也不一样:" + (mu == heapMemory));
		
		MemoryUsage mu1 = mm.getNonHeapMemoryUsage();

		System.out.println("每次返回的对象都不一样,这个对象不是单例的:" + (mu == mu1));

		ThreadMXBean tmx = ManagementFactory.getThreadMXBean();
		System.out.println(tmx.getTotalStartedThreadCount());
	}
}
