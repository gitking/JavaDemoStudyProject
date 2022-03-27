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
			//https://blog.csdn.net/youanyyou/article/details/106464291
			//https://www.zhihu.com/question/48780091/answer/113063216
			//PS MarkSweep只是回收器的别名，他可以指代Serial Old和Parallel Old。这句话说的不对,详情见知乎R大说的
			//在GC日志里，如果看到Full GC里有"ParOldGen"就是选择了ParallelOldGC。
			//[Full GC [PSYoungGen: 480K->0K(3584K)] [ParOldGen: 4660K->4909K(12288K)] 5141K->4909K(15872K) [PSPermGen: 11202K->11198K(22528K)], 0.0515530 secs] [Times: user=0.08 sys=0.00, real=0.05 secs]
			System.out.println(String.format("垃圾收集器的名字=%s,垃圾收集次数=%s,垃圾收集时间=%s", e.getName(), e.getCollectionCount(),
					e.getCollectionTime()));
			System.out.println("jdk的版本:" + System.getProperty("java.version"));
			/**
			 * 下面的实验基于:windows java version "1.8.0_281"
			 * 1.java -XX:+UseParNewGC -XX:+PrintGCDetails MavenSpringApp 运行输出结果如下:
			 * Java HotSpot(TM) 64-Bit Server VM warning: Using the ParNew young collector with the Serial old collector is deprecated and will likely be removed in a future release
			 * 垃圾收收集器的名字:GC的名字:ParNew
			 * 垃圾收收集器的名字:GC的名字:MarkSweepCompact
			 * GC日志里面的关键字:tenured generation加上上面的警告,说明使用的Serial old collector
			 * 这说明年轻代使用的是ParNew,老年代使用的是Serial old又叫MarkSweepCompact
			 * 2.java -XX:+UseSerialGC -XX:+PrintGCDetails MavenSpringApp 运行输出结果如下:
			 * 垃圾收收集器的名字:GC的名字:Copy
			 * 垃圾收收集器的名字:GC的名字:MarkSweepCompact
			 * 这说明年轻代使用的是Serial又叫Copy,老年代使用的是Serial old又叫MarkSweepCompact
			 * GC日志里面的关键字:tenured generation,说明使用的Serial old collector
			 * 3.java -XX:+UseParallelGC -XX:+PrintGCDetails MavenSpringApp 
			 * 或者
			 * java -XX:+UseParallelGC -XX:+UseParallelOldGC -XX:+PrintGCDetails MavenSpringApp 
			 * https://www.zhihu.com/question/56344485/answer/149543993
			 * 运行输出结果如下:
			 * 垃圾收收集器的名字:GC的名字:PS Scavenge
			 * 垃圾收收集器的名字:GC的名字:PS MarkSweep,https://hllvm-group.iteye.com/group/topic/27629
			 * 这说明年轻代使用的是ParallelGC又叫PS Scavenge,老年代使用的是UseParallelOldGC又叫PS MarkSweep
			 * GC日志里面的关键字:ParOldGen,说明使用的就是UseParallelOldGC
			 * https://www.zhihu.com/question/48780091/answer/113063216
			 * 这个改进使得HotSpot VM在选择使用ParallelGC（-XX:+UseParallelGC 或者是ergonomics自动选择）的时候，会默认开启 -XX:+UseParallelOldGC 。
			 * 这个变更应该是在JDK7u4开始的JDK7u系列与JDK8系列开始生效。
			 * https://www.zhihu.com/question/56344485/answer/149543993
			 * 4.java -XX:+UseParallelGC -XX:-UseParallelOldGC -XX:+PrintGCDetails MavenSpringApp 
			 * 注意这里用-把UseParallelOldGC禁用了
			 * 垃圾收收集器的名字:GC的名字:PS Scavenge
			 * 垃圾收收集器的名字:GC的名字:PS MarkSweep
			 * GC日志里面的关键字:PSOldGen,说明使用不是UseParallelOldGC,注意不是UseParallelOldGC,但是PSOldGen指的UseParallelGC自己老年代的版本
			 * 最开始是是没有UseParallelOldGC这个老年的垃圾收集器的,没有UseParallelOldGC之前年轻代使用UseParallelGC的时候,老年代版本就是PSOldGen
			 * 5.java -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:+PrintGCDetails MavenSpringApp 运行输出结果如下:
			 * 垃圾收收集器的名字:GC的名字:ParNew
			 * 垃圾收收集器的名字:GC的名字:ConcurrentMarkSweep
			 * 这说明年轻代使用的是ParNew,老年代使用的是ConcurrentMarkSweep
			 * GC日志里面的关键字:concurrent mark-sweep,说明使用的ConcurrentMarkSweep
			 * 6.java -XX:+UseConcMarkSweepGC -XX:+PrintGCDetails MavenSpringApp 运行输出结果如下:
			 * 只指定老年代的GC：-XX:+UseConcMarkSweepGC
			 * 垃圾收收集器的名字:GC的名字:ParNew
			 * 垃圾收收集器的名字:GC的名字:ConcurrentMarkSweep
			 * 这说明年轻代使用的是ParNew,老年代使用的是ConcurrentMarkSweep
			 * GC日志里面的关键字:concurrent mark-sweep,说明使用的ConcurrentMarkSweep
			 * java -XX:+UseParallelGC -XX:+UseConcMarkSweepGC -XX:+PrintGCDetails MavenSpringApp 这是错误的，年轻代UseParallelGC不能和CMS搭配使用
			 * java -XX:+UseParNewGC -XX:+UseParallelOldGC -XX:+PrintGCDetails MavenSpringApp 这是错误的，年轻代UseParNewGC不能和UseParallelOldGC搭配使用
			 * java -XX:+UseSerialGC -XX:+UseParallelOldGC -XX:+PrintGCDetails MavenSpringApp 这是错误的，年轻代UseSerialGC不能和UseParallelOldGC搭配使用
			 * java -XX:+UseSerialGC -XX:+UseConcMarkSweepGC -XX:+PrintGCDetails MavenSpringApp 这是错误的，年轻代UseSerialGC不能和UseConcMarkSweepGC搭配使用
			 * 都会报错:Conflicting collector combinations in option list; please refer to the release notes for the combinations allowed
			 * 7.java -XX:+PrintGCDetails MavenSpringApp 运行输出结果如下:
			 * 垃圾收收集器的名字:GC的名字:PS Scavenge
			 * 垃圾收收集器的名字:GC的名字:PS MarkSweep
			 * GC日志里面的关键字:ParOldGen,说明使用的UseParallelOldGC
			 * 这说明,JDK8什么都不加,年轻代模式使用的是-XX:+UseParallelGC老年代模式使用的是-XX:+UseParallelOldGC
			 * 8.java -XX:+UseG1GC -XX:+PrintGCDetails MavenSpringApp 运行输出结果如下:
			 * 垃圾收收集器的名字:GC的名字:G1 Young Generation
			 * 垃圾收收集器的名字:GC的名字:G1 Old Generation
			 * GC日志里面的关键字:garbage-first,说明使用的G1
			 */
			
			/**
			 * 下面的实验基于:windows java version "1.6.0_45"
			 * 1.java -XX:+PrintGCDetails MavenSpringApp 运行输出结果如下:
			 * 垃圾收收集器的名字:GC的名字:PS Scavenge
			 * 垃圾收收集器的名字:GC的名字:PS MarkSweep
			 * GC日志里面的关键字:PSOldGen说明老年代使用的是UseParallelGC的老年版本
			 * 什么都不手工指定,说明jdk6默认的年轻代垃圾收集器使用的是UseParallelGC
			 * 2.java -XX:+UseSerialGC -XX:+PrintGCDetails MavenSpringApp 运行输出结果如下:
			 * 垃圾收收集器的名字:GC的名字:Copy
			 * 垃圾收收集器的名字:GC的名字:MarkSweepCompact
			 * GC日志里面的关键字:tenured generation说明老年代使用的是Serial Old
			 * 3.java -XX:+UseParNewGC -XX:+PrintGCDetails MavenSpringApp 运行输出结果如下:
			 * 注意JDK6,只指定-XX:+UseParNewGC的时候,JVM不会发出警告
			 * 垃圾收收集器的名字:GC的名字:ParNew
			 * 垃圾收收集器的名字:GC的名字:MarkSweepCompact
			 * GC日志里面的关键字:par new generation说明年轻代使用的是UseParNewGC
			 * GC日志里面的关键字:tenured generation说明老年代使用的是Serial Old
			 * 4.java -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:+PrintGCDetails MavenSpringApp 运行输出结果如下:
			 * 垃圾收收集器的名字:GC的名字:ParNew
			 * 垃圾收收集器的名字:GC的名字:ConcurrentMarkSweep
			 * GC日志里面的关键字:par new generation说明年轻代使用的是UseParNewGC
			 * GC日志里面的关键字:concurrent mark-sweep说明老年代使用的是CMS
			 * 5.java -XX:+UseConcMarkSweepGC -XX:+PrintGCDetails MavenSpringApp 运行输出结果如下:
			 * 不指定年轻代使用什么GC,只指定老年代使用CMS
			 * 垃圾收收集器的名字:GC的名字:ParNew
			 * 垃圾收收集器的名字:GC的名字:ConcurrentMarkSweep
			 * GC日志里面的关键字:par new generation说明年轻代使用的是UseParNewGC
			 * GC日志里面的关键字:concurrent mark-sweep说明老年代使用的是CMS
			 * 6.java -XX:+UseParallelGC -XX:+PrintGCDetails MavenSpringApp 运行输出结果如下:
			 * 垃圾收收集器的名字:GC的名字:PS Scavenge
			 * 垃圾收收集器的名字:GC的名字:PS MarkSweep
			 * GC日志里面的关键字:PSYoungGen说明年轻代使用的是UseParallelGC
			 * GC日志里面的关键字:PSOldGen说明老年代使用的是UseParallelGC的老年版本
			 * 这个跟第一个什么都不知道JDK默认的版本是一样的.
			 * 7.java -XX:+UseParallelGC -XX:+UseParallelOldGC -XX:+PrintGCDetails MavenSpringApp 运行输出结果如下:
			 * 垃圾收收集器的名字:GC的名字:PS Scavenge
			 * 垃圾收收集器的名字:GC的名字:PS MarkSweep
			 * GC日志里面的关键字:PSYoungGen说明年轻代使用的是UseParallelGC
			 * GC日志里面的关键字:ParOldGen说明老年代使用的是UseParallelOldGC
			 * 这个改进使得HotSpot VM在选择使用ParallelGC（-XX:+UseParallelGC 或者是ergonomics自动选择）的时候，会默认开启 -XX:+UseParallelOldGC 。
			 * 这个变更应该是在JDK7u4开始的JDK7u系列与JDK8系列开始生效。
			 * https://www.zhihu.com/question/56344485/answer/149543993
			 * https://www.zhihu.com/question/41922036/answer/93079526
			 * 8.java -XX:+UseG1GC -XX:+PrintGCDetails MavenSpringApp 运行输出结果如下:
			 * 垃圾收收集器的名字:GC的名字:G1 Young Generation
			 * 垃圾收收集器的名字:GC的名字:G1 Old Generation
			 * GC日志里面的关键字:garbage-first说明年轻代使用的是G1
			 * GC日志里面的关键字:garbage-first说明老年代使用的是G1
			 * https://www.zhihu.com/question/56344485/answer/149543993
			 * https://www.zhihu.com/question/48780091/answer/113063216
			 * java -XX:+UseParallelGC -XX:+UseConcMarkSweepGC -XX:+PrintGCDetails MavenSpringApp 这是错误的，年轻代UseParallelGC不能和CMS搭配使用
			 * java -XX:+UseParNewGC -XX:+UseParallelOldGC -XX:+PrintGCDetails MavenSpringApp 这是错误的，年轻代UseParNewGC不能和UseParallelOldGC搭配使用
			 * java -XX:+UseSerialGC -XX:+UseParallelOldGC -XX:+PrintGCDetails MavenSpringApp 这是错误的，年轻代UseSerialGC不能和UseParallelOldGC搭配使用
			 * java -XX:+UseSerialGC -XX:+UseConcMarkSweepGC -XX:+PrintGCDetails MavenSpringApp 这是错误的，年轻代UseSerialGC不能和UseConcMarkSweepGC搭配使用
			 * 都会报错:Conflicting collector combinations in option list; please refer to the release notes for the combinations allowed
			 */
		}
	}
}