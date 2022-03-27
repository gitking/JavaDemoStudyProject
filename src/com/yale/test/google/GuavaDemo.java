package com.yale.test.google;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;

/*
 * https://mp.weixin.qq.com/s/NuB40w6_y2wkOFoQSxiftg Java必会的工具库，让你的代码量减少90%
 * Google Guava 工具类库
 * Maven依赖：
 * <dependency>  
 *   <groupId>com.google.guava</groupId>  
 *   <artifactId>guava</artifactId>  
 *   <version>30.1.1-jre</version>  
 * </dependency>
 * https://mp.weixin.qq.com/s/9_CjECZiHWwIXu-Ru83Ofw 《干掉GuavaCache：Caffeine才是本地缓存的王 》
 */
public class GuavaDemo {
	public static void main(String[] args) {
		List<String> list = Lists.newArrayList();
		List<Integer> listNew = Lists.newArrayList(1, 2, 3);
		
		List<Integer> reverse = Lists.reverse(listNew);//反转List
		System.out.println(reverse);
		
		//List集合元素太多,可以分成若干个集合,每个集合10个元素
		List<List<Integer>> parttion = Lists.partition(reverse, 10);
		
		Map<String, String> map = Maps.newHashMap();
		Set<String> set = Sets.newHashSet();
		
		//Multimap 一个key可以映射多个value的HashMap
		Multimap<String, Integer> mapmult = ArrayListMultimap.create(); 
		mapmult.put("key", 1);
		mapmult.put("key", 2);
		System.out.println(mapmult);//输出{"key":[1,2]}
		
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
		
		Collection<Integer> values = mapmult.get("key");
		
		//还能返回你以前使用的臃肿的Map,多省事，多简洁，省得你再创建 Map<String, List>
		Map<String, Collection<Integer>> collectionMap = mapmult.asMap();
		
		//BiMap一种连value也不能重复的HashMap,如果value重复,put方法会抛异常,除非用forcePut方法
		BiMap<String, String> biMap = HashBiMap.create();
		biMap.put("key", "value");
		System.out.println(biMap);//输出{"key":"value"}
		
		//既然Value不能重复,何不实现个翻转key/value的方法,已经有了
		BiMap<String, String> inverse = biMap.inverse();
		System.out.println(inverse);//输出{"value":"key"},这其实是双向映射，在某些场景还是很实用的。
		
		//Table一种有俩个key的HashMap,一批用户,同时按年龄和性别分组
		Table<Integer, String, String> table = HashBasedTable.create();
		table.put(18, "男", "yideng");
		table.put(18, "女", "Lily");
		System.out.println(table.get(18, "男"));//输出yideng
		
		//这其实是一个二维的Map,可以查看行数据
		Map<String, String> row = table.row(18);
		System.out.println(row);//输出{"男":"yideng","女":"Lily"}
		
		Map<Integer, String> column = table.column("男");//查看列数据
		System.out.println(column);//输出{18:"yideng"}
		
		
		//Multiset一种用来计数的Set
		Multiset<String> multiset = HashMultiset.create();
		multiset.add("apple");
		multiset.add("apple");
		multiset.add("orange");
		System.out.println(multiset.count("apple"));//输出2
		
		//查看去重的元素
		Set<String> setquch = multiset.elementSet();
		System.out.println(setquch);//输出["orange","apple"]
		
		//还能查看没有去重的元素
		Iterator<String> iterator = multiset.iterator();
		while (iterator.hasNext()) {
			System.out.println(iterator.next());
		}
		multiset.setCount("apple", 2);//还能手动设置某个元素出现的次数
	}
	
}
