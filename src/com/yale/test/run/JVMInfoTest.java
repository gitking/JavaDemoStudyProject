package com.yale.test.run;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;
import java.util.List;
import java.util.Map;

/**
 * http://blog.sina.com.cn/s/blog_c42004c90102w5ya.html
 * JVM 信息获取 之 ManagementFactory
 * @author dell
 */
public class JVMInfoTest {
	public static void main(String[] args) {     
		getClassLoadInfo();//类加载情况      

		System.out.println("---------------------");
		
		getMemoryInfo();//获取内存信息    

		System.out.println("---------------------");

		getGCInfo();//获取GC情况       
		
		System.out.println("---------------------");

		getSysInfo();//获取操作系统信息         

		System.out.println("---------------------");

		getRuntimeInfo();//获取运行时信息        

		System.out.println("---------------------");

		getThreadInfo();//获取线程信息    
	}

	public static void getMemoryInfo() {
		// 返回 Java 虚拟机的内存系统的管理 Bean
		MemoryMXBean bean = ManagementFactory.getMemoryMXBean(); // 对象分配的堆的当前内存使用量

		MemoryUsage hmu = bean.getHeapMemoryUsage();

		long init = hmu.getInit();// 内存初始容量

		long used = hmu.getUsed();// 已经使用的内存量(以字节为单位)

		long committed = hmu.getCommitted();// 保证可以由 Java 虚拟机使用的内存量 
		long max = hmu.getMax();//内存管理的最大内存量（以字节为单位）
		System.out.println("堆内存 初始容量："+init + " 已使用:" +used + "JVM专享:" + committed + " 最大内存:" + max); //返回 Java 虚拟机使用的非堆内存的当前内存使用量

		MemoryUsage nhmu = bean.getNonHeapMemoryUsage();

		long ninit = nhmu.getInit();// 内存初始容量

		long nused = nhmu.getUsed();// 已经使用的内存量(以字节为单位)

		long ncommitted = nhmu.getCommitted();// 保证可以由 Java 虚拟机使用的内存量 
		long nmax = nhmu.getMax();//内存管理的最大内存量（以字节为单位）
		System.out.println("非堆内存初始容量："+ninit + " 已使用:" +nused  + " JVM专享:" + ncommitted + " 最大内存:" + nmax);
	}

	public static void getClassLoadInfo() {
		ClassLoadingMXBean bean = ManagementFactory.getClassLoadingMXBean(); // 当前加载到  Java  虚拟机中的类的数量

		long loadedClassCount = bean.getLoadedClassCount(); // 返回自 Java 虚拟机开始执行到目前已经加载的类的总数。

		long totalLoadedClassCount = bean.getTotalLoadedClassCount(); // 返回自  Java  虚拟机开始执行到目前已经卸载的类的总数。

		long unloadedClassCount = bean.getUnloadedClassCount();

		System.out.println( "类  当前加载数:" + loadedClassCount + " 加载总数:" + totalLoadedClassCount + " 卸载数:" + unloadedClassCount);
	}

	public static void getGCInfo() {
		// Java 虚拟机的垃圾回收
		List<GarbageCollectorMXBean> beans = ManagementFactory.getGarbageCollectorMXBeans();

		for (GarbageCollectorMXBean bean : beans) {

			String gcName = bean.getName(); // 返回已发生的回收的总次数。 
			long loadedClassCount = bean.getCollectionCount(); // 返回近似的累积回收时间（以毫秒为单位）。

			long totalLoadedClassCount = bean.getCollectionTime();
			System.out.println("GC :" + gcName + "  回收的总次数:" + loadedClassCount + " 累积回收时间:" + totalLoadedClassCount);
		}
	}

	public static void getSysInfo() {

		OperatingSystemMXBean bean = ManagementFactory.getOperatingSystemMXBean();

		String arch = bean.getArch();// 返回操作系统

		int cpuNum = bean.getAvailableProcessors(); // 返回 Java 虚拟机可以使用的处理器数目。

		String name = bean.getName();// 返回操作系统名称

		String version = bean.getVersion();// 返回操作系统的版本
		System.out.println("系统架构："+ arch +" 系统名称：" + name + "版本：" + version + "CPU：" + cpuNum);
	}

	public static void getThreadInfo() {

		ThreadMXBean bean = ManagementFactory.getThreadMXBean();

		long[] deadlockedThreads = bean.findDeadlockedThreads();// 死锁状态的线程 
		long threadCount = bean.getThreadCount();//返回活动线程的当前数目，包括守护线程和非守护线程。

		long peakThreadCount = bean.getPeakThreadCount();// 返回自从 Java  虚拟机启动或峰值重置以来峰值活动线程计数

		long totalStartedThreadCount = bean.getTotalStartedThreadCount();// 返回自从  Java  虚拟机启动以来创建和启动的线程总数目。
		System.out.println("线程  线程数："+threadCount+" 峰值活动线程数："+peakThreadCount+"  总线程数:"+totalStartedThreadCount);
		// dumpAllThreads(boolean  lockedMonitors,  boolean  lockedSynchronizers) //打印线程信息

	}

	public static void getRuntimeInfo() {     

		 System.out.println("运行时信息----------------");       

		 RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();        

		String name = bean.getName();// 虚拟机名称       

		 String specName = bean.getSpecName();// 返回 Java 虚拟机规范名称。        

		String specVendor = bean.getSpecVendor();// 返回 Java 虚拟机规范供应商。        
		String specVersion = bean.getSpecVersion();// 返回 Java 虚拟机规范版本。            
		String vmVersion = bean.getVmVersion();// Java 虚拟机实现版本        

		String vmVendor = bean.getVmVendor(); // Java 虚拟机实现供应商。        

		String vmName = bean.getVmName();// Java 虚拟机实现名称。        
		System.out.println("虚拟机规范名称"+specName+" 虚拟机规范供应商"+specVendor+" 虚拟机规范版本"+specVersion    +" 虚拟机实现版本"+vmVersion+" 虚拟机实现供应商"+vmVendor+" 虚拟机实现名称"+vmName);                

		long startTime = bean.getStartTime();// Java 虚拟机的启动时间（以毫秒为单位）。       // Java 虚拟机的正常运行时间（以毫秒为单位）。

		long uptime = bean.getUptime();        

		System.out.println("虚拟机名称："+name+" 启动时间："+startTime+" 运行时间"+uptime);        

		Map atts = bean.getSystemProperties();// 所有系统属性 名称和值的映射。        

		System.out.println("系统属性:" + atts.toString());       

		 System.out.println("运行时信息---------------- over");    

		}
}
