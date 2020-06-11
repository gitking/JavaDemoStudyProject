package com.yale.test.run;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * 作者：拉莫斯之舞 链接：https://www.imooc.com/article/34605
 * https://www.jianshu.com/p/2a8d6231d995 
 引用计数法
         引用计数法是一种比较早的GC回收算法，目前一般不采用，其主要思想是：每个对象都维持一个引用计数器，初始值为0，当一个对象被引用的时候，该对象的引用计数器
         就加1，当不被引用的时候，该对象的引用计数器就减1，如果一个对象的引用计数器变为了0，则该对象被认为是可以回收的。采用这种方式的优缺点都很明显，
         优点是实现简单，效率高，缺点是可能存在循环引用，导致内存溢出。
   标记-清除法 
         标记-清除法按名字分为“标记”和“清除”2个阶段，其基本思想是：首先标记出所有存活的对象，标记完成后，统一清除所有需要被回收的对象。那怎么判断某个对象是可以回收的呢？GC时，
         从一系列GC Roots根节点开始遍历，遍历时走过的路径即称为引用链，如果一个对象和GC Roots没有任何引用链相关，那么这个对象就不可用，就会被判定为可回收，这种算法也叫根搜索算法。
         那么哪些对象可以成为GC Roots对象呢？在java语言里，可以作为GC Roots的对象包括下面4种： 
            虚拟机栈中的引用变量 
            方法区中的类静态属性引用的对象 
           方法区中的常量引用的对象 
           本地方法栈中JNI（即native方法）的引用的对象
           标记-清除法的算法示意图如下：
   新生代的复制法
           复制法的基本思想是：将内存分为大小相等的2块，每次只使用其中一块，GC时每次将所有存活的对象复制到另一块区域，然后清理该内存。
           这几种都是方法区和栈中的引用对象。复制法的优点是：实现简单，回收速度快，且不会产生内存碎片。但由于每次只使用其中一块，导致内存利用率较低。复制算法的示意图如下：
          现在的商业虚拟机都采用复制法来回收新生代，由于新生代的对象98%以上都是朝生夕死的，所以并不需要按照1：1来分配，而是将内存分为较大的Eden区和2块较小的Survivor区
          （通常Eden和Survivor区大小的比值为8:1:1，可以根据SurvivorRationJVM内存参数来设置比值），每次使用Eden区和其中一块Survivor区类分配对象，GC时，将Eden区和Survivor区中
          的存活对象复制到另一块Survivor区域，这样一来，内存利用率就高了，而且运行速度也很快。
  老年代的标记-整理法
           复制法在对象存活率较高时，回收效率就变低了，而在老年代中，大部分的对象都是存活期较高的对象，因此就不适宜采用复制法进行老年代的GC。根据老年代的特点，并结合标记-清除法的思路，
           于是提出了标记-整理法。其主要思路是：标记过程与标记-清除法一致，只是标记完成后，不直接对未存活进行清除，而是将所有存活的对象都向一端移动，然后清理掉端边界以外的所有内存区域。
           这种方法的优点是不会产生内存碎片。标记-整理法的算法示意图如下：  
   https://yq.aliyun.com/articles/622667?spm=a2c4e.11155435.0.0.a53a7229tBmQD4
   http://www.360doc.com/content/12/1219/15/495229_255087879.shtml
 * @author dell
 */
public class GarbageCollectorInfo {
	private long m_lastGcCount = 0;
	private long m_lastGcTime = 0;
	private long m_lastFullgcTime = 0;
	private long m_lastFullgcCount = 0;
	private long m_lastYounggcTime = 0;
	private long m_lastYounggcCount = 0;

	public long getM_lastGcCount() {
		return this.m_lastGcCount;
	}

	public long getM_lastGcTime() {
		return this.m_lastGcTime;
	}

	public long getM_lastFullgcTime() {
		return this.m_lastFullgcTime;
	}

	public long getM_lastFullgcCount() {
		return this.m_lastFullgcCount;
	}

	public long getM_lastYounggcTime() {
		return this.m_lastYounggcTime;
	}

	public long getM_lastYounggcCount() {
		return this.m_lastYounggcCount;
	}

	private Set<String> younggcAlgorithm = new LinkedHashSet<String>() {
		{
			add("Copy");
			add("ParNew");
			add("PS Scavenge");
			add("G1 Young Generation");
		}
	};
	private Set<String> oldgcAlgorithm = new LinkedHashSet<String>() {
		{
			add("MarkSweepCompact");
			add("PS MarkSweep");
			add("ConcurrentMarkSweep");
			add("G1 Old Generation");
		}
	};

	private Map<String, Number> collectGC() {
		long gcCount = 0;
		long gcTime = 0;
		long oldGCount = 0;
		long oldGcTime = 0;
		long youngGcCount = 0;
		long youngGcTime = 0;
		Map<String, Number> map = new LinkedHashMap<>();
		for (final GarbageCollectorMXBean garbageCollector : ManagementFactory.getGarbageCollectorMXBeans()) {

			gcTime += garbageCollector.getCollectionTime();
			gcCount += garbageCollector.getCollectionCount();
			String gcAlgorithm = garbageCollector.getName();
			if (younggcAlgorithm.contains(gcAlgorithm)) {
				youngGcTime += garbageCollector.getCollectionTime();
				youngGcCount += garbageCollector.getCollectionCount();
			} else if (oldgcAlgorithm.contains(gcAlgorithm)) {
				oldGcTime += garbageCollector.getCollectionTime();
				oldGCount += garbageCollector.getCollectionCount();
			}
		}
		//
		// GC实时统计信息
		//
		map.put("jvm.gc.count", gcCount - m_lastGcCount);
		map.put("jvm.gc.time", gcTime - m_lastGcTime);
		final long fullGcCount = oldGCount - m_lastFullgcCount;
		map.put("jvm.fullgc.count", fullGcCount);
		map.put("jvm.fullgc.time", oldGcTime - m_lastFullgcTime);
		map.put("jvm.younggc.count", youngGcCount - m_lastYounggcCount);
		map.put("jvm.younggc.time", youngGcTime - m_lastYounggcTime);
		if (youngGcCount > m_lastYounggcCount) {
			map.put("jvm.younggc.meantime", (youngGcTime - m_lastYounggcTime) / (youngGcCount - m_lastYounggcCount));
		} else {
			map.put("jvm.younggc.meantime", 0);
		} //
		// GC增量统计信息
		//
		m_lastGcCount = gcCount;
		m_lastGcTime = gcTime;
		m_lastYounggcCount = youngGcCount;
		m_lastYounggcTime = youngGcTime;
		m_lastFullgcCount = oldGCount;
		m_lastFullgcTime = oldGcTime;
		return map;
	}
}
