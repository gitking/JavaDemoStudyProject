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
 * https://lucfzy.com/2020/04/jvm-zhiling/#load%E5%92%8Cstore
 * http://blog.sina.com.cn/s/blog_c42004c90102w5ya.html
 * JVM 信息获取 之 ManagementFactory
 * https://yq.aliyun.com/articles/617782?spm=a2c4e.11153940.0.0.5a0f5491IqPiKQ&type=2
 * https://yq.aliyun.com/articles/622667?spm=a2c4e.11155435.0.0.a53a7229tBmQD4
 * https://blog.csdn.net/xtayfjpk/article/details/41924283
 * JVM内存模型:java堆，java栈（即虚拟机栈），本地方法栈，方法区和程序计数器。其中方法区和堆区是线程共享的，虚拟机栈，本地方法栈和程序计数器是线程私有的，也称线程隔离的，
 * 每个区域存储不同的内容。这2个知识点必须牢记，是掌握JVM内存模型的基础。
 * 程序计数：
 *  JVM中的程序计数器是一块很小的内存区域，但是这块内存区域挺有意思的。主要特性有3个：
      1、存储内容：对于java普通方法（即没用native关键字修饰的方法），存储的是执行过程中当前指令的地址，而对于native方法，这里是空的（undefined），为啥呢？因为调用本地方法的时候可能已经超出了JVM虚拟机的内存地址了。
      2、线程私有的：为什么程序计数器是线程私有的？根据存储内容也好理解，假如是线程共享的，那多个线程执行的时候，都不知道自己当前线程执行的地址是哪个了，有的线程快，有的线程慢，快的执行完就进入下一步，等慢的线程执行完回来发现自己的地址都变了，岂不乱套？
      3、是JVM中唯一不会报内存溢出（OutOfMemoryError）的区域。
 * 虚拟机栈：
 * 虚拟机栈主要存储的是一个个栈帧，每个栈帧中存储的是局部变量表，操作数栈，动态链接和方法出口信息等。其中局部变量表中存储的是方法中定义的一些局部变量，
 * 对象的引用，参数，和方法的返回地址等。局部变量表所占用的空间大小在编译期就能确定，在方法运行的时候，并不会改变局部变量表的空间大小，这结合局部变量表
 * 存储的内容就很好理解。操作数栈可以理解为对当前操作的数据入出栈，对于64位长度的long和double类型，每个操作数占用2个字宽（slot），其他类型的操
 * 作数占用一个字宽（slot）。每个方法调用时都会创建一个栈帧，执行的过程对应的就是一个栈帧在虚拟机栈中从入栈到出栈的过程。有关栈帧的内容可以参考一个网友
 * 写的一篇博客：https://blog.csdn.net/xtayfjpk/article/details/41924283，讲的很好很详细。这里放个栈帧的图，看了一目了然
 * 关于虚拟机栈内存溢出有2种情况：
           1、线程请求的栈深度 超过了虚拟机允许的深度，会抛出StackOverflowError，所以当我们在代码中看到这个异常时，就应该想到可能是虚拟机栈出了问题。
           2、如果虚拟机栈可以动态扩展（当前大部分JVM都可以动态扩展，不过JVM也允许固定长度的虚拟机栈），当扩展时无法申请到足够的内存时，会抛出OutOfMemoryError异常。
 * 本地方法栈
           这块知识点比较简单，本地方法栈和虚拟机栈的功能类似，只不过是为JVM调用native方法时服务的，而且JVM对本地方法使用的语言（比如Java调用C语言实
           现的功能，就需要定义native方法来实现）、使用方式和数据结构都没有强制规定，因此不同的虚拟机可以自由实现。而且HotSpot虚拟机直接把本地方法栈和虚
           拟机栈合二为一。与虚拟机栈类似，本地方法栈也会抛出StackOverflowError和OutOfMemoryError。
 * 方法区
           方法区是一个比较重要的区域，java虚拟机规范中把方法区描述为堆的一个逻辑部分，但是为了和Heap（堆区）对应，也称Non-Heap（非堆区）。
           主要存储的是静态变量，常量（包括运行时常量），类的加载信息和java编译后的代码。这部分空间不需要连续，可以选择固定大小和可扩展，通常在这部分是没
           有GC的，因为GC回收的都是些静态变量，常量和类的加载信息，这些对象回收效果通常不尽人意，因此可以选择不实现垃圾回收。这块区域也称为持久代，
           当这块内存不足时，也会报OutOfMemoryError异常。
 * 堆区
    Java堆区是JVM内存中最胖的一块区域，因为这里存储的都是对象的实例和数组对象。这块区域是线程共享的，在JVM启动时就会创建，想想如果这么大的空间是线程私有的，那内存不得爆掉吗？
            按照java虚拟机规范，堆区的内容可以物理上不连续，只要逻辑上连续即可，在实现时可以是固定大小的，也可以是可扩展的，而且通常都是可扩展的，我们常用的内存参数-Xms和-Xmx就是用来调节堆大小的。
    java堆区按生命周期不同，分为新生代和老年代。新生代又可以细分为Eden和Survivor区，而Survivor又可以细分为Survivor1和Survivor2，这两者通常只使用其中一块，
            另一块用来GC时保留存活的对象。大部分的new出来的对象都是存放在Eden区，如果是大对象，比如一个很大的数组或者List对象，可以通过JVM参数-XX:PretenureSizeThreshold将超过指定大小的对象直接存入到老年代，需要注意的是，写程序时应该尽量避免朝生夕死的大对象进入老年代，因为相比年轻代的GC，老年代GC的成本更大。Eden和Survivor的默认大小比值的8:1:1，新生代默认的GC算法是复制算法。老年代的默认GC算法是标记整理法。关于这2种GC算法，会在下篇博客讲解。
	当堆中没有足够内存时，会抛出OutOfMemoryError异常。关于堆区的内存模型，可以参考下面的图片：
	Java的堆是一个运行时数据区，类的对象从堆中分配空间。这些对象通过new等指令建立，通过垃圾回收器来销毁。
	堆的优势是可以动态地分配内存空间，需要多少内存空间不必事先告诉编译器，因为它是在运行时动态分配的。但缺点是，由于需要在运行时动态分配内存，所以存取速度较慢。
      栈
  	栈中主要存放一些基本数据类型的变量（byte，short，int，long，float，double，boolean，char）和对象的引用。
  	栈的优势是，存取速度比堆快，栈数据可以共享。但缺点是，存放在栈中的数据占用多少内存空间需要在编译时确定下来，缺乏灵活性
  https://mp.weixin.qq.com/s?__biz=MzUxOTc4NjEyMw==&mid=2247488429&idx=2&sn=46ce9d9d650d9466fd128ac0a9a537ab&chksm=f9f50849ce82815f9906d7e6d15eda86df07135143ec2dc940b8eecbaee793d045920a63b60b&mpshare=1&scene=24&srcid=0607ywjCHOxmekZn2nAgZGKm&sharer_sharetime=1591542531977&sharer_shareid=06213cdd7bc440805a60c37708775b3b#rd
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
