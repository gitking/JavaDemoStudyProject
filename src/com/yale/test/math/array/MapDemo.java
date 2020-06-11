package com.yale.test.math.array;

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
		 * TreeMap表示可以排序的Map子类,他是按照key的内容来进行排序的。key值要实现Compareable接口
		 */
		Map<Integer, String> treeMap = new TreeMap<Integer, String>();
		treeMap.put(2, "C");
		treeMap.put(0, "X");
		treeMap.put(1, "B");
		System.out.println(treeMap);
		
	}
}
