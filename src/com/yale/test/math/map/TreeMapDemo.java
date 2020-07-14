package com.yale.test.math.map;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/*
 * 我们已经知道，HashMap是一种以空间换时间的映射表，它的实现原理决定了内部的Key是无序的，即遍历HashMap的Key时，其顺序是不可预测的（但每个Key都会遍历一次且仅遍历一次）。
 * 还有一种Map，它在内部会对Key进行排序，这种Map就是SortedMap。注意到SortedMap是接口，它的实现类是TreeMap。
       ┌───┐
       │Map│
       └───┘
         ▲
    ┌────┴─────┐
    │          │
┌───────┐ ┌─────────┐
│HashMap│ │SortedMap│
└───────┘ └─────────┘
               ▲
               │
          ┌─────────┐
          │ TreeMap │
          └─────────┘
 * SortedMap保证遍历时以Key的顺序来进行排序。例如，放入的Key是"apple"、"pear"、"orange"，遍历的顺序一定是"apple"、"orange"、"pear"，因为String默认按字母排序：
 */
public class TreeMapDemo {

	public static void main(String[] args) {
		Map<String, Integer> map = new TreeMap<>();
        map.put("orange", 1);
        map.put("apple", 2);
        map.put("pear", 3);
        for (String key : map.keySet()) {
            System.out.println(key);
        }
        
        /**
		 * TreeMap表示可以排序的Map子类,他是按照key的内容来进行排序的。key值要实现Compareable接口
		 * 如果作为Key的class没有实现Comparable接口，那么，必须在创建TreeMap时同时指定一个自定义排序算法：
		 * 另外，注意到Person类并未覆写equals()和hashCode()，因为TreeMap不使用equals()和hashCode()。
		 */
		Map<Integer, String> treeMap = new TreeMap<Integer, String>();
		treeMap.put(2, "C");
		treeMap.put(0, "X");
		treeMap.put(1, "B");
		System.out.println(treeMap);
		
		Map<Person, Integer> treeMapPer = new TreeMap<>(new Comparator<Person>(){
			//TreeMap在比较两个Key是否相等时，依赖Key的compareTo()方法或者Comparator.compare()方法。在两个Key相等时，必须返回0。
			public int compare(Person p1, Person p2){
				return p1.name.compareTo(p2.name);
			}
		});
		
		treeMapPer.put(new Person("Tom"), 1);
		treeMapPer.put(new Person("Bom"), 2);
		treeMapPer.put(new Person("Lily"), 3);
		for (Person key: treeMapPer.keySet()) {
			System.out.println(key);
		}
		System.out.println(treeMapPer.get(new Person("Bom")));
	}
}
