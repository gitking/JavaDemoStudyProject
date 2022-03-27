package com.yale.test.math.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

import org.apache.commons.collections.MapUtils;

import com.google.common.collect.ArrayListMultimap;

/**
 * 本类和com.yale.test.math.map.PersonHashCode.java一起看
 * 本类和com.yale.test.math.array.ListDemo.java一起看
 * 本类和com.yale.test.math.array.SetDemo.java一起看
 * https://www.liaoxuefeng.com/wiki/1252599548343744/1265117217944672
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
 * 虽然指定容量是10000，但HashMap内部的数组长度总是2的n次方，因此，实际数组长度被初始化为比10000大的16384（2的14次方）。
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
 * https://www.liaoxuefeng.com/wiki/1252599548343744/1265117217944672
 * 关于Map的key为什么要同时重写equals、hashCode的理解。
 * 答: hashmap中依据key的hash值来确定value存储位置，所以一定要重写hashCode方法，而重写equals方法，是为了解决hash冲突，如果两个key的hash值相同，就会调用equals方法，比较key值是否相同，在存储时：如果equals结果相同就覆盖更新value值，如果不同就用List他们都存储起来。
 * 在取出来是：如果equals结果相同就返回当前value值，如果不同就遍历List中下一个元素。即要key与hash同时匹配才会认为是同一个key。
 * JDK中源码:if(e.hash == hash && ((k = e.key) == key || (key != null && key.equals(k)))){ops;}
 * @author dell
 */
public class MapDemo {
	public static void main(String[] args) {
		
		String key1 = "a";
		Map<String, Integer> mapKey = new HashMap<>();
		mapKey.put(key1, 123);
		String key2 = new String("a");
		/**
		 * 因为在Map的内部，对key做比较是通过equals()实现的，这一点和List查找元素需要正确覆写equals()是一样的，即正确使用Map必须保证：作为key的对象必须正确覆写equals()方法。
		 * 我们经常使用String作为key，因为String已经正确覆写了equals()方法。但如果我们放入的key是一个自己写的类，就必须保证正确覆写了equals()方法。
		 * 我们再思考一下HashMap为什么能通过key直接计算出value存储的索引。相同的key对象（使用equals()判断时返回true）必须要计算出相同的索引，否则，相同的key每次取出的value就不一定对。
		 * 通过key计算索引的方式就是调用key对象的hashCode()方法，它返回一个int整数。HashMap正是通过这个方法直接定位key对应的value的索引，继而直接返回value。
		 * 因此，正确使用Map必须保证：
		    作为key的对象必须正确覆写equals()方法，相等的两个key实例调用equals()必须返回true；
		    作为key的对象还必须正确覆写hashCode()方法，且hashCode()方法要严格遵循以下规范：
		    如果两个对象相等，则两个对象的hashCode()必须相等；
		    如果两个对象不相等，则两个对象的hashCode()尽量不要相等。
		 * 即对应两个实例a和b：
    		如果a和b相等，那么a.equals(b)一定为true，则a.hashCode()必须等于b.hashCode()；
    		如果a和b不相等，那么a.equals(b)一定为false，则a.hashCode()和b.hashCode()尽量不要相等。
    	 * 上述第一条规范是正确性，必须保证实现，否则HashMap不能正常工作。
    	 * 而第二条如果尽量满足，则可以保证查询效率，因为不同的对象，如果返回相同的hashCode()，会造成Map内部存储冲突，使存取的效率下降。
		 * (六) 集合处理
		 *  1. 【强制】关于hashCode和equals的处理，遵循如下规则： 
		 * 	1） 只要覆写equals，就必须覆写hashCode。 
		 *  2） 因为Set存储的是不重复的对象，依据hashCode和equals进行判断，所以Set存储的对象必须覆写这两种方法。 
		 *  3） 如果自定义对象作为Map的键，那么必须覆写hashCode和equals。 说明：String因为覆写了hashCode和equals方法，所以可以愉快地将String对象作为key来使用。
		 *  《阿里巴巴Java开发手册嵩山版2020.pdf》
		 */
		System.out.println("能获取到值吗?" + mapKey.get(key2));
		System.out.println("结果为:" + (key1 == key2));
		System.out.println("结果为:" + (key1.equals(key2)));

		Map<Integer, String> map = new HashMap<Integer, String>();
		map.put(1, "Hello");
		map.put(1, "Hello");//key重复了,重复会覆盖,key值不能重复
		map.put(2, "Word");
		map.put(null, "HashMap的key值可以存放null值");
		map.put(null, null);//HashMap的key和value都可以存放null值
		System.out.println(map);
		
		//遍历Map
		Set<Integer> mapSet = map.keySet();
		Iterator<Integer> it = mapSet.iterator();
		while (it.hasNext()) {
			Integer key = it.next();
			System.out.println(key + " = " + map.get(key));
		}
		
		for (Integer in: map.keySet()) {
			String val = map.get(in);
			System.out.println("遍历Map:" + val);
		}
		
		/**
		 * 同时遍历key和value可以使用for each循环遍历Map对象的entrySet()集合，它包含每一个key-value映射：
		 * Map.Entry是Map接口 类里面的一个内部接口类
		 * 用static 定义的内部接口类,就相当于外部接口类
		 * Map和List不同的是，Map存储的是key-value的映射关系，并且，它不保证顺序。在遍历的时候，遍历的顺序既不一定是put()时放入的key的顺序，也不一定是key的排序顺序。使用Map时，任何依赖顺序的逻辑都是不可靠的。
		 * 以HashMap为例，假设我们放入"A"，"B"，"C"这3个key，遍历的时候，每个key会保证被遍历一次且仅遍历一次，但顺序完全没有保证，甚至对于不同的JDK版本，相同的代码遍历的输出顺序都是不同的！
		 * 遍历Map时，不可假设输出的key是有序的！ 
		 */
		System.out.println("使用Entry输出Map");
		Set<Map.Entry<Integer,String>> setMap = map.entrySet();
		Iterator<Map.Entry<Integer,String>> mapIt = setMap.iterator();
		while(mapIt.hasNext()) {
			Map.Entry<Integer, String> entry = mapIt.next();
			System.out.println(entry.getKey() + " = " + entry.getValue());
		}
		
		/**
		 * 6. 【强制】使用Map的方法keySet()/values()/entrySet()返回集合对象时，不可以对其进行添加元素操作，否则会抛出UnsupportedOperationException异常。
		 * 《阿里巴巴Java开发手册嵩山版2020.pdf》
		 * 18. 【推荐】使用entrySet遍历Map类集合KV，而不是keySet方式进行遍历。 
		 * 说明：keySet其实是遍历了2次，一次是转为Iterator对象，另一次是从hashMap中取出key所对应的value。而entrySet只是遍历了一次就把key和value都放到了entry中，效率更高。
		 * 如果是JDK8，使用Map.forEach方法。 
		 * 正例：values()返回的是V值集合，是一个list集合对象；keySet()返回的是K值集合，是一个Set集合对象；entrySet()返回的是K-V值组合集合。
		 */
		for (Map.Entry<Integer, String> entry : map.entrySet()) {
			Integer key = entry.getKey();
			String val = entry.getValue();
			System.out.println("map的key为:" + key + ",map的Val为:" + val);
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
		
		/**
		 * 2. 【强制】判断所有集合内部的元素是否为空，使用isEmpty()方法，而不是size()==0的方式。 
		 * 说明：在某些集合中，前者的时间复杂度为O(1)，而且可读性更好。 正例：
			Map<String, Object> map = new HashMap<>(16);
			if(map.isEmpty()) {
			System.out.println("no element in this map.");
			}
		 * 《阿里巴巴Java开发手册嵩山版2020.pdf》
		 */
		if (map.isEmpty()) {
			System.out.println("空Map,no element in this map.");
		}
		/**
		 * 17. 【推荐】集合初始化时，指定集合初始值大小。 说明：HashMap使用HashMap(int initialCapacity) 初始化，如果暂时无法确定集合大小，那么指定默认值（16）即可。
		 * 正例：initialCapacity = (需要存储的元素个数 / 负载因子) + 1。注意负载因子（即loader factor）默认为0.75，如果暂时无法确定初始值大小，请设置为16（即默认值）。
		 * 反例： HashMap需要放置1024个元素，由于没有设置容量初始大小，随着元素增加而被迫不断扩容，resize()方法总共会调用8次，反复重建哈希表和数据迁移。当放置的集合元素个数达千万级时会影响程序性能。
		 * 19. 【推荐】高度注意Map类集合K/V能不能存储null值的情况，如下表格：
				集合类 					Key 		Value 		Super 		说明
				Hashtable 			不允许为null 	 不允许为null   Dictionary 	线程安全
				ConcurrentHashMap 	不允许为null 	 不允许为null 	  AbstractMap 	锁分段技术（JDK8:CAS）
				TreeMap 			不允许为null 	 允许为null 	  AbstractMap 	线程不安全
				HashMap 			允许为null 	 允许为null 	  AbstractMap 	线程不安全
		 * 反例：由于HashMap的干扰，很多人认为ConcurrentHashMap是可以置入null值，而事实上，存储null值时会抛出NPE异常。
		 * 21. 【参考】利用Set元素唯一的特性，可以快速对一个集合进行去重操作，避免使用List的contains()进行遍历去重或者判断包含操作。
		 */
		
		Map<String, String> mapJDK8 = new HashMap<String, String>();
		mapJDK8.put("公号", "小黑十一点半");
		mapJDK8.put("主理人", "楼下小黑哥");
		mapJDK8.put("键为null", null);
		//https://juejin.cn/post/6932977382315786254
		String val = mapJDK8.getOrDefault("键为null", "为空的时候给一个默认值");
		System.out.println("如果当时往map里面存的val为null那么取出来的值依然为null:并不会给默认值" + val);
		String valNot = mapJDK8.getOrDefault("键不存在", "为空的时候给一个默认值");
		System.out.println("键不存在的时候才有默认值:" + valNot);
		
		/**
		 * commons-collections-3.2.2.jar
		 * org.apache.commons.collections.MapUtils
		 * MapUtils这个工具类相对于Map#getOrDefault有一个好处，针对传入 Map为 null 的情况，可以设置默认值。
		 * 假设我们是从 POJO对象获取 Map 参数，这个时候为了防止空指针，我们就需要提前对map做一个空指针的判断。
		 * 不过如果使用 MapUtils，那我们就不需要判断是否为 null，方法内部已经封装这个逻辑。
		 * https://juejin.cn/post/6932977382315786254
		 */
		String value = MapUtils.getString(mapJDK8, "将为null", "给个默认值");
		
		/**
		 * 巧用 computeIfAbsent
		 * 日常开发中，我们会碰到这类场景，需要一个键需要映射到多个值，这个时候我们可以使用 Map<K, List<V>>这个结构。
		 * 此时添加元素的时候，我们需要做一些判断，当内部元素不存在时候主动创建一个集合对象，示例代码如下
		 */
		Map<String, List<String>> mapIf = new HashMap();

		List<String> classify = mapIf.get("java框架");
		if (Objects.isNull(classify)) {
		    classify = new ArrayList<>();
		    classify.add("Spring");
		    mapIf.put("java框架", classify);
		} else {
		    classify.add("Spring");
		}
		/**
		 * 上面的代码比较繁琐，到了 JDK8，Map新增一个 computeIfAbsent方法：
		 * default V computeIfAbsent(K key,Function<? super K, ? extends V> mappingFunction) {
		 * 如果 Map中 key 对应的 value 不存在，则会将 mappingFunction 计算产生的值作为保存为该 key 的 value，并且返回该值。否则不作任何计算，将会直接返回 key 对应的 value。
		 * 利用这个特性，我们可以直接使用 Map#computeIfAbsent一行代码完成上面的场景，示例代码如下：
		 */
		mapIf.computeIfAbsent("java框架", key-> new ArrayList<>()).add("Spring");
		/**
		 * 那其实 Map 中还有一个方法 putIfAbsent，功能跟 computeIfAbsent比较类似。
		 * 那刚开始使用的时候，误以为可以使用 putIfAbsent完成上面的需求：
		 * // ERROR:会有 NPE 问题
		 * map.putIfAbsent("java框架", new ArrayList<>()).add("Spring");
		 * 那其实这是错误的，当 Map 中 key 对应 value 不存在的时候，putIfAbsent将会直接返回 null。
		 * 而 computeIfAbsent将会返回 mappingFunction计算之后的值，像上面的场景直接返回就是 new ArrayList。
		 * 这一点需要注意一下，切勿用错方法，导致空指针。
		 * 最后针对上面这种一个键需要映射到多个值，其实还有一个更优秀的解决办法，使用 Google Guava 提供的新集合类型 Multiset，以此快速完成一个键需要映射到多个值的场景。
		 * 示例代码如下：
		 * guava-18.0.jar
		 * com.google.common.collect.ArrayListMultimap.java
		 * https://juejin.cn/post/6932977382315786254
		 */
		ArrayListMultimap<Object, Object> multiset = ArrayListMultimap.create();
		// java框架--->Spring,Mybatis
		multiset.put("java框架", "Spring");
		multiset.put("java框架", "MyBatis");
		
		/**
		 * 单词统计
		 * 假设有如下需求，我们需要统计一段文字中相关单词出现的次数。那实现方式其实很简单，使用 Map存储相关单词的次数即可，示例代码如下：
		 */
		Map<String, Integer> countMap = new HashMap<>();
		countMap.put("java", 1);
		Integer count = countMap.get("java");
		if (Objects.isNull(count)) {
			countMap.put("java", 1);
		} else {
			//countMap.put("java", count++);count++是不对的
			countMap.put("java", ++count);//++count才对
			/**
			 * 还有i++，++i的意思，相信大部分初学者都被这个i++,++i的值困惑过，接下来让大哥我给你们解释清楚。
			 * 咳咳，首先如果i++,++i如果不参与运算,做为单独的一行代码执行的话，他们的结果都是自身的值加1之后的值。例如: 
			 * int i = 0; 
			 * i++;
			 * System.out.println(i);//输出1
			 * int j = 0;
			 * ++j;
			 * System.out.println(j);//输出1
			 * 接下来说说参与运算的情况:首先你们要明白读和写的概念，也就是运算和赋值的概念。JAVA中"="号的含义:把等号右边的值赋给左边。
			 * =号右边是一个运算表达式,=号左边是接收右边的运算表达式运算完后的结果。你们先把上面的汉语看明白，再看下面的例子。
			 */
			int numI,numQ= 0;//初始值都是0
	        numI = numQ++;//这里是先赋值,先把numQ(0)的值赋给numI,再运算numQ++。
	        System.out.println(numI);//numI输入0,因为他先把numQ(0)的值赋给numI了。
	        System.out.println(numQ);//numQ输入1,numI = numQ++;这行代码numQ++已经运算过了,所以numQ的值为1。
	        int numJ,numK= 0;//初始值都是0
	        numJ = ++numK;//这里是先运算,先把numK的值加1,再赋值，再把numK的值赋给numJ。
	        System.out.println(numJ);//numJ输入1
	        System.out.println(numK);//numK输入1,这里不用说了，numK的值肯定是1,因为numJ= ++numK;这里是先运算,先把numK的值加1,再赋值
	        //你们要注意i++,++i是否参与运算表达式，懂吗？
	        //下面这种其实是一样的。
	        int arrI = 0;
	        int [] arr = new int[]{1,2,3,4};
	        arr[arrI++]++;//这里的arr[arrI++]等于arr[0],先把arrI的值赋给数组的下标索引,等于arr[0]取出的是1,然后1++编程2了，数组的第一个值就变成2了。
	        System.out.println("数组"+ arr[0]+"数组"+arr[1]+"数组"+arr[2]+"数组"+arr[3]);//数组2数组2数组3
	        System.out.println("arrI的值为:" + arrI);//arrI等于1
	        //下面这个跟上面的效果一样
	        int arrK = 0;
	        int [] arrSec = new int[]{1,2,3,4};
	        arrSec[arrK++] = arrSec[arrK++]++;
	        System.out.println("数组"+ arrSec[0]+"数组"+arrSec[1]+"数组"+arrSec[2]+"数组"+arrSec[3]);//数组2数组3数组3
	        System.out.println("arrK的值为:" + arrK);
	        
	        int arrK1 = 0;
	        int [] arrSec1 = new int[]{1, 10, 3, 4};
	        arrSec1[arrK1++] = arrSec1[arrK1++]++;
	        System.out.println("数组"+ arrSec1[0]+"数组"+arrSec1[1]+"数组"+arrSec1[2]+"数组"+arrSec1[3]);//数组2数组3数组3
	        System.out.println("arrK1的值为:" + arrK1);
	        /**
	         * 代码的执行顺序为从左到右，从上到下的,所以上面的代码执行顺序为:
	         * arrSec1[arrK1++]等价于arrSec1[0]意思就是数组下标为0的那个元素,然后 arrK1++之后变成1。
	         * arrSec1[arrK1++]++;等价于arrSec1[1],注意此时arrK1的值变成1了,所以arrSec1[1]取出的值为:10,然后arrK1++之后arrK1的值变成2,然后
	         * arrSec1[1]++的意思就是arrSec1[1] = arrSec1[1] + 1 = 10 + 1,所以数组下标为1的元素就变成11了。懂了波。
	         * Java的解释器始终从左至右的顺序计算操作数。如果操作数是有副作用的表达式，这种顺序就很重要了。例如下面的代码:
	         * int a =2;
	         * int v = ++a + ++a * ++a;
	         * 虽然乘法的优先级比加法高,但是会先计算+运算符的俩个操作数。因为这俩个操作数都是++a,所以得到的计算的结果分别是3和4,因此这个表达式计算的是
	         * 3 + 4 * 5,结果为23。
	         * 来自《Java技术手册(第6版)》安道 译,第28页,第30页,第35页。
	         */
	        arrSec1[1]++;
	        System.out.println("arrSec1的值为:" + arrSec1[1]);
		}
		
		/**
		 * 上面这类代码是不是很熟悉？同样比较繁琐。
		 * 接下来我们可以使用 JDK8 Map 新增方法进行改造，这次使用上面用过的 getOrDefault 再加 put 方法快速解决，示例代码如下：
		 */
		count = countMap.getOrDefault("java", 0);
		countMap.put("java", count + 1);
		//那其实我们还有一种办法，这次我们使用 Map#merge这个新方法，一句代码完成上述需求，示例代码如下：
		countMap.merge("java", 1, Integer::sum);
		/**
		 * 说真的，刚看到 merge这个方法的时候还是有点懵，尤其后面直接使用 lambda 函数，让人不是很好理解。
		 * 这里先将lambda 函数还原成正常类，给大家着重解释一下这个方法：
		 * 用上面代码说明一下merge方法，如果 java这个值在 countMap中不存在，那么将会其对应的 value 设置为 1。
		 * 那如果 java 在 countMap 中存在，则会调用第三个参数 remappingFunction 函数方法进行计算。
		 * remappingFunction 函数中，oldValue代表原先 countMap 中 java 的值，newValue代表我们设置第二个参数 1，这里我们将两者相加，刚好完成累加的需求。
		 * 最后
		 * 这次主要从个人日常碰到三个场景出发，给大家对比了一下使用 JDK8  Map 新增方法只会，两者代码区别。
		 * 从上面可以很明显看出，使用新增方法之后，我们可以用很少的代码可以完成，整体看起来变得非常简洁。
		 * 不过 JDK8 之后很多方法都会用到 lambda 函数，不熟悉的话，其实比较难以理解代码。
		 * 不过也还好，我们只要在日常编码过程中，刻意去练习使用，很快就能上手。
		 * 最后，JDK8 还有许多好用方法，刻意简化代码开发，你可以在留言区推荐几个吗？
		 * https://juejin.cn/post/6932977382315786254
		 */
		countMap.merge("java", 1, new BiFunction<Integer, Integer, Integer>(){
			@Override
			public Integer apply(Integer oldValue, Integer newValue) {
				return Integer.sum(oldValue, newValue);
			}
		});
	}
}
