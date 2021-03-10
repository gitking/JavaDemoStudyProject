package com.yale.test.run;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServer;
import javax.management.ObjectName;

/*
 * 在所有的"程序中"使用的地址都是虚拟地址(在段式管理中也叫逻辑地址),这些地址在不同的进程之间是可以重复的,所以才叫虚拟地址(就像每个班级中学生的学号都可以从1开始那样),
 * 但是如何通过虚拟地址找到物理地址呢?
 * 在程序中为什么要使用虚拟地址呢?用过C代码的同学都知道,在编译后的指令中,许多调用的地址在编译阶段就得确定下来,许多方法入口和变量位置在编译时确定了虚拟地址,而真正运行时
 * 是要由OS来分配实际的地址给程序的。另外,使用虚拟地址后,地址是可以被复用的,程序并不关注其他的进程是否会用同一个地址(这一点由OS分配时来确保),
 * 而只关注自己的进程可以用这个地址.
 * 1KB=1024byte(字节)
 * 若一个进程访问的不是"本进程的内存",那么就会出现问题,在Windows上写C程序遇到这种情况会提示"内存不能read"错误,进程可能会被crash掉.
 * 现代CPU启动了分页模型,以支撑较大的内存,分页模型会将内存区域划分成较小的页,物理上大多将其划分为4KB/页,要管理这些页就需要一些管理者,下面来看看大概的情况。
 * 系统会为每个进程分配页目录,这个页目录也是一个页(大小也是4KB),这些页是由Kernel来管理的(因此Kernel需要单独的区域,程序所使用地址并不是从0开始的),当通过一个
 * 虚拟地址访问内存时,会寻找对应进程的页目录的地址,加载到CR3寄存器中.找到页目录后做什么呢?页目录是4KB大小,在32位系统中,每4字节可以存放一个地址,一个页目录是4KB大小,
 * 可以存放0-1023共1024个地址,因此传入的逻辑地址只需要10位(10bit刚好就是1023=1111111111)就可以定位到一个页目录中一个保存地址的单元(32位中的高10位),
 * 这个单元存储的地址就是"页表"的地址.页表本身也是一个"页",大小也是4KB(有1024个页表,所以页表总大小为4MB),每个也存储了1024个地址,因此逻辑地址中的中间10个二进制就可以标志也表中的
 * 对应单元(每4个字节为一个标志地址的单元)。页表中的这个单元也许不一定是物理页,可能还需要经过一层转换后才能得到物理页.也就是说,使用20个bit就可以定位到具体的页地址,在32位系统中
 * 最后12位就可以在页内通过偏移地址找到对应的字节(这种思想其实在JVM的8字节对齐中也有类似的用法,按照8字节为基本单位的内存管理,地址的最后3个字节始终是0,若利用好这3个字节就可以虚拟出8x4GB=32GB的空间)
 * 从这可以看出,这样的寻址方式对于每个进程来讲都有机会得到1024x1024x4KB=4GB的地址空间.
 * 反过来,根据虚拟地址,页目录可保存1024个单元,因此逻辑地址的前面10位的范围是0000000000~1111111111。同样的,通过页目录找到页表后,每个页表也是1024个单元,因此中间10位的范围也是
 * 0000000000~1111111111.因此,前面20位的地址都是连续的,而后面12位是内部偏移量,也是连续的,这样的地址也叫"线性地址",(通常程序要分配一个连续地址,也就是一个线性地址).
 * 线性地址最终要映射的物理位置,不仅仅可以是物理内存本身,还可以是网卡设备,磁盘配置的Swap空间等,也就是当物理内存不够用时,只要地址空间是够用的,那么就可以用其他的设备来代替,只是效率可能比较低.
 * 如果一个进行所需要的空间连地址空间也不够用了,那就悲剧了.
 * 当一个进程需要申请"一次"分配内存时,操作系统会分配一块连续的虚拟地址内存给它,当然,进程可以多次向操作系统申请内存空间,这些空间最终和进程所对应的页表映射起来,所以当进程退出后,这些资源都会被释放.
 * 在Java语言中,主要是看Heap区域,当系统参数设置为-Xms,-Xmx时,JVM通常是申请一个连续的虚拟地址,也就是Java申请一块打内存,通过OS预先分配的实际五路内存空间是-Xms的大小,
 * 但是OS未必会立即分配一个-Xmx大小的空间给JVM使用,许多空间也是到真正使用时才分配的(大家可以在64位系统中做个小实验,启动一个Java程序时,-Xmx设置为比物理内存大几十倍也没问题,但是-Xms不行).
 * 线性地址分配好以后,自己来虚拟分配内存空间,对象分配内存时由JVM与OS交互来完成,这个分配可能会比C语言在分配内存上更加简单。但也正因为如此,JVM所申请的内存空间需要由自己来释放,
 * 因为这些地址相对OS来讲都已经给JVM了,它并不知道JVM什么时候会释放这部分内存空间.
 * 进程向OS申请的内存空间首先会从Free状态到Reserved状态,这种状态仅仅是占用了一个坑,但是还没有真正用这个内存空间,而真正用的内存页的状态为Commited，
 * 这通过对许多操作系统的监控也能看到.
 * 而JVM这块大内存要求逻辑地址是连续的,所以会比较苛刻,通常在32位系统中,由于OS会被占用一部分内存空间,通常JVM的内存空间不会被设置的太大,当设置太大时,会提示无法分配内存的错误.
 * 在32位系统中,1.5GB的Heap区域是比较合适的,因为JVM除了这部分空间外还需要使用很多空间,必须为这些开销空间预留空间。在64位系统中,这个空间几乎不会受到限制,不过这时JVM也必须换成64位模式的。
 * 《Java特种兵》第57页
 * 
 */
public class OperatingSystemMXBeanTest {
	/**
	 * https://blog.csdn.net/jackyrongvip/article/details/83975506?depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromBaidu-5&utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromBaidu-5
	 * 在JAVA 7中，在监控方面，可以监视了系统和CPU负载，代码段如下：
import com.sun.management.OperatingSystemMXBean;
...
OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(
OperatingSystemMXBean.class);
// What % CPU load this current JVM is taking, from 0.0-1.0
System.out.println(osBean.getProcessCpuLoad());

// What % load the overall system is at, from 0.0-1.0
System.out.println(osBean.getSystemCpuLoad());

这里用数字0到1表示CPU的负载及系统的负载。
可惜的是，不知道为啥ORACLE没放到标准的java.lang中，而采用的是
com.sun.management.OperatingSystemMXBean。
要注意的中是，getSystemLoadAverage(),在JAVA 6也有，但
可惜的是在WINDOWS上运行的并不好。
	 */
	public static void main(String[] args) {
		OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
		System.out.println("操作系统最近一分钟的平均负载" + osBean.getSystemLoadAverage());
		System.out.println("操作系統架构:" + osBean.getArch());
		System.out.println("操作系統名字:" + osBean.getName());
		System.out.println("操作系統名字:" + System.getProperty("os.name"));
		System.out.println("操作系統版本:" + osBean.getVersion());
		
		System.out.println("操作系統版本:" + System.getProperty("sun.os.patch.level"));//得到操作系统版本
		
		System.out.println("PlatformManagedObject名字:" + osBean.getObjectName());
	}
	
	/**
	 * https://cloud.tencent.com/developer/ask/43687
	 * @return
	 * @throws Exception
	 */
	public static double getProcessCpuLoad() throws Exception {

	    MBeanServer mbs    = ManagementFactory.getPlatformMBeanServer();
	    ObjectName name    = ObjectName.getInstance("java.lang:type=OperatingSystem");
	    AttributeList list = mbs.getAttributes(name, new String[]{ "ProcessCpuLoad" });

	    if (list.isEmpty()) {
	    	return Double.NaN;
	    }

	    Attribute att = (Attribute)list.get(0);
	    Double value  = (Double)att.getValue();

	    // usually takes a couple of seconds before we get real values
	    if (value == -1.0)      return Double.NaN;
	    // returns a percentage value with 1 decimal point precision
	    return ((int)(value * 1000) / 10.0);
	}
}
