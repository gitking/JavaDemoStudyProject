package com.yale.test.run;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.List;

import javax.management.MBeanServer;

import com.sun.management.HotSpotDiagnosticMXBean;
/**
 * https://blog.csdn.net/lsm135/article/details/78818128
 * @author dell
 */
public class MavenSpringApp {
	public static void main(String[] args) {
		testManagementFactory();
	}

	private static void testManagementFactory() {
		// 1获取JVM输入参数
		List<String> list = ManagementFactory.getRuntimeMXBean().getInputArguments();
		// 2.获取当前JVM进程的PID
		String name = ManagementFactory.getRuntimeMXBean().getName();
		String pid = name.split("@")[0];
		// 3.获取当前系统的负载
		ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage();
		// 4.获取内存相关的
		ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();// 堆内存
		//https://cloud.tencent.com/developer/article/1129904 今咱们来聊聊JVM 堆外内存泄露的BUG是如何查找的
		//https://club.perfma.com/article/1835499?from=groupmessage#/article/1835499?from=groupmessage
		//难道是linux glibc 中经典的 64M 内存问题?一次 Java 进程 OOM 的排查分析（glibc 篇） https://club.perfma.com/article/1709425?last=1714757&type=parent#/article/1709425?last=1714757&type=parent
		//JAVA堆外内存排查小结 https://zhuanlan.zhihu.com/p/60976273
		//https://developer.aliyun.com/article/657160 一次堆外内存泄露的排查过程
		ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage();// 堆外内存
		// 5.获取堆栈信息相当于jstack
		ManagementFactory.getThreadMXBean().dumpAllThreads(false, false);
		// ThreadInfo 里有线程的信息

		// 进行垃圾回收监控
		// MavenSpringApp.main(new String[]{"-gcutil", "-h5",pid,"1s"});

		// 如何获取HotSpotDiagnosticMXBean ？
		MBeanServer server = ManagementFactory.getPlatformMBeanServer();
		// HotSpotDiagnosticMXBean hotspotDiagnosticMXBean = new
		// PlatformMXBeanProxy(server,
		// "com.sun.management:type=HotSpotDiagnostic",
		// HotSpotDiagnosticMXBean.class);

		// 获取young GC 和full GC 次数
		List<GarbageCollectorMXBean> list1 = ManagementFactory.getGarbageCollectorMXBeans();
		for (GarbageCollectorMXBean e : list1) {
			System.out.println(String.format("name=%s,count=%s,time=%s", e.getName(), e.getCollectionCount(),
					e.getCollectionTime()));
		}
	}
}