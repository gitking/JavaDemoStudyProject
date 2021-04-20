package com.yale.test.math;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import sun.misc.Unsafe;

/*
 * Java的java.util.concurrent包除了提供底层锁、并发集合外，还提供了一组原子操作的封装类，它们位于java.util.concurrent.atomic包。
 * 我们以AtomicInteger为例，它提供的主要操作有：
 * 增加值并返回新值：int addAndGet(int delta)
 * 加1后返回新值：int incrementAndGet()
 * 获取当前值：int get()
 * 用CAS方式设置：int compareAndSet(int expect, int update)
 * Atomic类是通过无锁（lock-free）的方式实现的线程安全（thread-safe）访问。它的主要原理是利用了CAS：Compare and Set。
 * 如果我们自己通过CAS编写incrementAndGet()，它大概长这样：
 * 小结
 * 使用java.util.concurrent.atomic提供的原子操作可以简化多线程编程：
 * 原子操作实现了无锁的线程安全；
 * 适用于计数器，累加器等。
 */
public class AtomicIntegerDemo {
	
	/*
	 * CAS是指，在这个操作中，如果AtomicInteger的当前值是prev，那么就更新为next，返回true。如果AtomicInteger的当前值不是prev，就什么也不干，返回false。
	 * 通过CAS操作并配合do ... while循环，即使其他线程修改了AtomicInteger的值，最终的结果也是正确的。
	 * 通常情况下，我们并不需要直接用do ... while循环调用compareAndSet实现复杂的并发操作，而是用incrementAndGet()这样的封装好的方法，因此，使用起来非常简单。
	 * 在高度竞争的情况下，还可以使用Java 8提供的LongAdder和LongAccumulator。
	 */
	public int incrementAndGet(AtomicInteger ai) {
		int prev, next;
		do {
			prev = ai.get();//每次循环都把ai对象的最新值取出来
			next = prev + 1;
		} while(!ai.compareAndSet(prev, next));//然后放在这里面对比,compareAndSet方法的第一个参数如果设置的不得当,容易造成死循环。比如这样ai.compareAndSet(10, next)就会死循环
		return next;
	}
	
	public static void main(String[] args) {
		/*
		 * Exception in thread "main" java.lang.SecurityException: Unsafe
		 * Unsafe不提供给我们普通开发人员用,我们自己知直接执行下面这行代码会报错:
		 * Unsafe unsafe = Unsafe.getUnsafe();
		 * https://www.v2ex.com/t/730686  V2EX偏向锁,Java 轻量级锁使用在无竞争场景，那么为什么还要用它？
		 * https://tech.meituan.com/2018/11/15/java-lock.html 美团《不可不说的Java“锁”事》
		 * https://mp.weixin.qq.com/s/h3MB8p0sEA7VnrMXFq9NBA 美团《【基本功】Java魔法类：Unsafe应用解析》
		 * https://www.cnblogs.com/throwable/p/9139947.html JAVA中神奇的双刃剑--Unsafe 
		 * https://www.cnblogs.com/suxuan/p/4948608.html  Java魔法类：sun.misc.Unsafe
		 * https://www.cnblogs.com/dennyzhangdd/p/7230012.html 在openjdk8下看Unsafe源码
		 * https://club.perfma.com/article/2062595#/article/2062595
		 * https://docs.oracle.com/javase/specs/jls/se7/html/jls-10.html 首先，在Java中，数组也是对象
		 * 那么既然是对象，就会有object header，占用一部分空间，那么理解数组的base offset也就不难了
		 * 比如下面的一段JOL输出，实际上对象的属性数据是从OFFSET 16的位置开始的，0-12的空间被header所占用
		 * Unsafe源码:https://github.com/openjdk/jdk/blob/jdk8-b120/jdk/src/share/classes/sun/misc/Unsafe.java
		 */
		//AtomicIntegerDemo aid = new AtomicIntegerDemo();
		AtomicInteger ai = new AtomicInteger();//new AtomicInteger()默认值是0
		//aid.incrementAndGet(ai);
		
		/*
		 * ai.incrementAndGet()方法内部调用的是unsafe.getAndAddInt(this, valueOffset, 1) + 1
		 * ai.getAndIncrement()方法内部调用的是unsafe.getAndAddInt(this, valueOffset, 1)
		 * 这说明unsafe.getAndAddInt()这个方法的意思是先返回当前的值,然后再加一
		 * AtomicInteger里面的compareAndSet和weakCompareAndSet方法的源码是一样的,为什么是一样的？
		 * 但是真的是这样么？并不是，JDK 源码很博大精深，才不会设计一个重复的方法，你想想 JDK 团队也不是会犯这种低级团队，但是原因是什么呢？
		 * 《Java 高并发详解》这本书给出了我们一个答案:通过源码我们不难发现俩个方法的实现完全一样,那么为什么要有这俩个方法呢？其实在JDK1.6版本以前双方的实现是存在差异的,
		 * compareAndSet方法的底层主要是针对Intelx86架构下的CPU指令CAS:cmpxchg(sparc-TSO,ia64的CPU也支持),但是ARM CUP架构下的类似指令为LL/SC：ldrex/strex
		 * (ARM架构下的CPU主要应用于当下的移动互联网设备,比如在智能手机终端设备中,高通骁龙,华为麒麟等系列都是基于ARM架构和指令集下的CPU产品),或许在运行Android的JVM设备上这俩个方法
		 * 底层存在着差异。
		 * https://mp.weixin.qq.com/s/vbXAgNH9PyL16PmjgnGKZA
		 * 这里我有一个疑问,unsafe这个类里面的方法都是原子性的吗？CAS方法unsafe.compareAndSwapInt肯定是原子性的,那么别的方法呢？unsafe.getAndAddInt这个方法呢？
		 * 答:去github上面看jdk的源码发现,unsafe.getAndAddInt这个方法内部调的也是unsafe.compareAndSwapInt方法,所以说肯定是原子性的
		 * https://github.com/openjdk/jdk/blob/jdk8-b120/jdk/src/share/classes/sun/misc/Unsafe.java
		 * 如果是JDK8，推荐使用LongAdder对象，比AtomicLong性能更好（减少乐观锁的重试次数）。《阿里巴巴Java开发手册（泰山版）.  VolatileDemo.java
		 * JDKBug清单:
		 * 1,https://bugs.java.com/bugdatabase/view_bug.do?bug_id=JDK-8168628
		 * 2,https://bugs.java.com/bugdatabase/view_bug.do?bug_id=6209663 JConsole连接不上问题
		 * 3,http://java.sun.com/j2se/1.5.0/docs/guide/management/jconsole.html JConsole连接不上问题
		 * 在Windows版JDK里带上SA的相关bug是：
		 * 4,Bug 6743339: Enable building sa-jdi.jar and sawindbg.dll on Windows with hotspot build(http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6743339)
	     * 5,Bug 6755621: Include SA binaries into Windows JDK(https://bugs.java.com/bugdatabase/view_bug.do?bug_id=6755621)
	     * 6,https://bugs.java.com/bugdatabase/view_bug.do?bug_id=8137185
		 * 知乎:https://www.zhihu.com/answer/170264788
		 * 问:为什么OpenJDK只有share,solaris,windows下有源码而bsd和Linux没有呢?
		 * RednaxelFX(R大)答:因为OpenJDK里,Java标准库和部分工具的源码repo(jdk目录)里,BSD和Linux的平台相关源码都是在solaris目录里的。原本SunJDK的源码里
		 * 平台相关的目录就是从solaris和windows这俩个目录开始的,后来Unix系的平台相关代码全都放在solaris目录下了,共用大部分代码.
		 * 另外:OpenJDK里的HotSpot VM的目录结构(hotspot目录)跟jdk目录的就不太一样 ,请区分开来讨论。
		 */
		System.out.println("incrementAndGet的方法意思是:返回当前值加1之后的值:" + ai.incrementAndGet());
		System.out.println("得到当前值:" + ai.get());
		System.out.println("getAndIncrement的方法意思是:返回当前值之后再加1:" + ai.getAndIncrement());
		System.out.println("得到当前值:(这里得到的是2)" + ai.get());
	}
}

class IdGenerator {
	//我们利用AtomicLong可以编写一个多线程安全的全局唯一ID生成器：
	AtomicLong al = new AtomicLong(0);
	public long getNextId() {
		return al.incrementAndGet();
	}
}
