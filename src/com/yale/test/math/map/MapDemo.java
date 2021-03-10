package com.yale.test.math.map;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Map接口有如下几个子类:HashMap,HashTable,TreeMap,ConcurrentHashMap
 * JDK1.0提供有三大主要类:Vector,Enumeration, Hashtable.Hashtable是最早实现这种二元偶对象数据结构
 * HashMap之所以能根据key直接拿到value，原因是它内部通过空间换时间的方法，用一个大数组存储所有value，并根据key直接计算出value应该存储在哪个索引：
 * 通过key计算索引的方式就是调用key对象的hashCode()方法，它返回一个int整数。HashMap正是通过这个方法直接定位key对应的value的索引，继而直接返回value。
 * 
 * 延伸阅读
	既然HashMap内部使用了数组，通过计算key的hashCode()直接定位value所在的索引，那么第一个问题来了：hashCode()返回的int范围高达±21亿，先不考虑负数，HashMap内部使用的数组得有多大？
	实际上HashMap初始化时默认的数组大小只有16，任何key，无论它的hashCode()有多大，都可以简单地通过：
 * 由于扩容会导致重新分布已有的key-value，所以，频繁扩容对HashMap的性能影响很大。如果我们确定要使用一个容量为10000个key-value的HashMap，更好的方式是创建HashMap时就指定容量：
 * int index = key.hashCode() & 0xf; // 0xf = 15
 * 把索引确定在0～15，即永远不会超出数组范围，上述算法只是一种最简单的实现。
 * 第二个问题：如果添加超过16个key-value到HashMap，数组不够用了怎么办？
 * 添加超过一定数量的key-value时，HashMap会在内部自动扩容，每次扩容一倍，即长度为16的数组扩展为长度32，相应地，需要重新确定hashCode()计算的索引位置。
 * 例如，对长度为32的数组计算hashCode()对应的索引，计算方式要改为：
 * int index = key.hashCode() & 0x1f; // 0x1f = 31
 * 由于扩容会导致重新分布已有的key-value，所以，频繁扩容对HashMap的性能影响很大。如果我们确定要使用一个容量为10000个key-value的HashMap，更好的方式是创建HashMap时就指定容量：
 * Map<String, Integer> map = new HashMap<>(10000);
 * 虽然指定容量是10000，但HashMap内部的数组长度总是2n，因此，实际数组长度被初始化为比10000大的16384（214）。
 * 最后一个问题：如果不同的两个key，例如"a"和"b"，它们的hashCode()恰好是相同的（这种情况是完全可能的，因为不相等的两个实例，只要求hashCode()尽量不相等），那么，当我们放入：
 * map.put("a", new Person("Xiao Ming"));
 * map.put("b", new Person("Xiao Hong"));
 * 时，由于计算出的数组索引相同，后面放入的"Xiao Hong"会不会把"Xiao Ming"覆盖了？
 * 当然不会！使用Map的时候，只要key不相同，它们映射的value就互不干扰。但是，在HashMap内部，确实可能存在不同的key，映射到相同的hashCode()，即相同的数组索引上，肿么办？
 * 我们就假设"a"和"b"这两个key最终计算出的索引都是5，那么，在HashMap的数组中，实际存储的不是一个Person实例，而是一个List，它包含两个Entry，一个是"a"的映射，一个是"b"的映射：
 *    ┌───┐
	0 │   │
	  ├───┤
	1 │   │
	  ├───┤
	2 │   │
	  ├───┤
	3 │   │
	  ├───┤
	4 │   │
	  ├───┤
	5 │ ●─┼───> List<Entry<String, Person>>
	  ├───┤
	6 │   │
	  ├───┤
	7 │   │
  	  └───┘
  	  在查找的时候，例如：
  	HashMap内部通过"a"找到的实际上是List<Entry<String, Person>>，它还需要遍历这个List，并找到一个Entry，它的key字段是"a"，才能返回对应的Person实例。
  	我们把不同的key具有相同的hashCode()的情况称之为哈希冲突。在冲突的时候，一种最简单的解决办法是用List存储hashCode()相同的key-value。显然，如果冲突的概率越大，
  	这个List就越长，Map的get()方法效率就越低，这就是为什么要尽量满足条件二：
  	 如果两个对象不相等，则两个对象的hashCode()尽量不要相等。 
  	 hashCode()方法编写得越好，HashMap工作的效率就越高。
 * ConcurrentHashMap内部默认分解为16个Segment,数据都是先查找Segment,再在内部加锁,因此在理想情况下,锁粒度可以降低16倍,那么自然的应该允许16个并行,当然并不排除
 * 由于热点问题,导致某些Segment上的请求更多,而某些Segment上的没有争用。最坏的情况就是所有的请求分布到了同一个Segment上
 * @author dell
 */
public class MapDemo {
	public static void main(String[] args) {
		Map<Integer, String> map = new HashMap<Integer, String>();
		map.put(1, "Hello");
		map.put(1, "Hello");//key重复了,重复会覆盖,key值不能重复
		map.put(2, "Word");
		map.put(null, "HashMap的key值可以存放null值");
		map.put(null, null);//HashMap的key和value都可以存放null值
		System.out.println(map);
		
		Set<Integer> mapSet = map.keySet();
		Iterator<Integer> it = mapSet.iterator();
		while (it.hasNext()) {
			Integer key = it.next();
			System.out.println(key + " = " + map.get(key));
		}
		
		/**
		 * Map.Entry是Map接口 类里面的一个内部接口类
		 * 用static 定义的内部接口类,就相当于外部接口类
		 */
		System.out.println("使用Entry输出Map");
		Set<Map.Entry<Integer,String>> setMap = map.entrySet();
		Iterator<Map.Entry<Integer,String>> mapIt = setMap.iterator();
		while(mapIt.hasNext()) {
			Map.Entry<Integer, String> entry = mapIt.next();
			System.out.println(entry.getKey() + " = " + entry.getValue());
		}
		
		/*
		 * 因为在Map的内部，对key做比较是通过equals()实现的，这一点和List查找元素需要正确覆写equals()是一样的，即正确使用Map必须保证：作为key的对象必须正确覆写equals()方法。
		 * 通过key计算索引的方式就是调用key对象的hashCode()方法，它返回一个int整数。HashMap正是通过这个方法直接定位key对应的value的索引，继而直接返回value。
		 * 因此，正确使用Map必须保证：
			    作为key的对象必须正确覆写equals()方法，相等的两个key实例调用equals()必须返回true；
			    作为key的对象还必须正确覆写hashCode()方法，且hashCode()方法要严格遵循以下规范：
				    如果两个对象相等，则两个对象的hashCode()必须相等；
				    如果两个对象不相等，则两个对象的hashCode()尽量不要相等。
				即对应两个实例a和b：
				    如果a和b相等，那么a.equals(b)一定为true，则a.hashCode()必须等于b.hashCode()；
				    如果a和b不相等，那么a.equals(b)一定为false，则a.hashCode()和b.hashCode()尽量不要相等。
			上述第一条规范是正确性，必须保证实现，否则HashMap不能正常工作。
			而第二条如果尽量满足，则可以保证查询效率，因为不同的对象，如果返回相同的hashCode()，会造成Map内部存储冲突，使存取的效率下降。
		 */
		Map<String, String> strMap = new HashMap<String, String>();
		strMap.put(new String("zs"), "张三");
		System.out.println("这里能取出来吗?:" + strMap.get(new String("zs")));
		
		//Hashtable是线程安全的每个方法都用synchronized修饰了
		Map<Integer, String> hashtable = new Hashtable<Integer, String>();
		hashtable.put(1, "Hello");
		hashtable.put(1, "Hello");//key重复了,重复会覆盖,key值不能重复
		hashtable.put(2, "Word");
		//hashtable的key和value值都不能为null
		//hashtable.put(null, "hashtable的key值可以存放null值");
		System.out.println(hashtable);
		
		/**
		 * ConcurrentHashMap的特点 = Hashtable的线程安全性 + HashMap的高性能
		 * ConcurrentHashMap是将数据分桶存放的
		 */
		Map<Integer, String> conHashMap = new ConcurrentHashMap<Integer, String>();
		conHashMap.put(1, "Hello");
		conHashMap.put(1, "Hello");//key重复了,重复会覆盖,key值不能重复
		conHashMap.put(2, "Word");
		//ConcurrentHashMap的key和value值都不能为null,看ConcurrentHashMap源码的put方法就只知道了
		System.out.println(conHashMap);
		
		for (int x =0; x<10; x++) {
			/**
			 * 这个是伪代码,模拟ConcurrentHashMap的分桶存放数据的逻辑
			 */
			new Thread(()->{
				Random ran = new Random();
				int temp = ran.nextInt(9999);
				int reslut = temp %3;//将数据分3桶存放
				switch (reslut) {
					case 0:
						System.out.println("第[0]桶" + temp);
						break;
					case 1:
						System.out.println("第[1]桶" + temp);
						break;
					case 2:
						System.out.println("第[2]桶" + temp);
						break;
				}
			}).start();
		}
	}
}
