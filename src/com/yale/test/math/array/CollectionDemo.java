package com.yale.test.math.array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

/**
 * public interface Collection<E> extends Iterable<E> 
 * List(允许重复),Set(不允许重复)
 * List有三个非常重要的子类：ArrayList,Vector,LinkedList
 * Set有俩个常用子类:HashSet(不允许重复,无序存储),TreeSet(不允许重复,有序存储)
 * @author dell
 */
public class CollectionDemo {
	public static void main(String[] args) {
		List<String> strList = new ArrayList<String>();
		strList.add("list可以变成数组");
		strList.add("list可以变成数组");
		strList.add("list可以变成数组");
		
		System.out.println("从List中取出数据:" + strList.get(0));
		
		String temp = strList.set(1, "set方法可以修改List中的数据");
		System.out.println("修改前的数据:" + temp);
		
		String[] strArr = new String[1];
		strArr = strList.toArray(strArr);
		System.out.println(Arrays.toString(strArr));
		
		strList.remove("list可以变成数组");//remove只会删除一个元素,即使List里面有重复的元素
		System.out.println(strList);
		
		List<Person> perList = new ArrayList<Person>();
		perList.add(new Person("张三", 10));
		perList.add(new Person("李四", 10));
		perList.add(new Person("王五", 10));
		System.out.println("List中的remove和contains方法必须需要类中的equals支持,否则list不知道俩个对象一样不一样");
		System.out.println("这里删不掉,因为这个俩个不同的张三对象" + perList.remove(new Person("张三", 10)));
		System.out.println("这里会判断为不存在,因为这个俩个不同的张三对象" + perList.contains(new Person("张三", 10)));
		
		
		List<Person> vector = new Vector<Person>();
		vector.add(new Person("张三", 10));
		vector.add(new Person("李四", 10));
		vector.add(new Person("王五", 10));
		System.out.println("Vector跟ArrrayList几乎是一摸一样的,Vector的大部分方法上面都加了synchronized方法,是线程安全的。Vector几乎不用");
		System.out.println("List中Vector的remove和contains方法必须需要类中的equals支持,否则list不知道俩个对象一样不一样");
		System.out.println("Vector这里删不掉,因为这个俩个不同的张三对象" + vector.remove(new Person("张三", 10)));
		System.out.println("Vector这里会判断为不存在,因为这个俩个不同的张三对象" + vector.contains(new Person("张三", 10)));
		
		List<Person> linkedList = new LinkedList<Person>();
		linkedList.add(new Person("张三", 10));
		linkedList.add(new Person("李四", 10));
		linkedList.add(new Person("王五", 10));
		System.out.println("linkedList跟ArrrayList几乎是一摸一样的,LinkedList是一个链表");
		System.out.println("List中linkedList的remove和contains方法必须需要类中的equals支持,否则list不知道俩个对象一样不一样");
		System.out.println("linkedList这里删不掉,因为这个俩个不同的张三对象" + linkedList.remove(new Person("张三", 10)));
		System.out.println("linkedList这里会判断为不存在,因为这个俩个不同的张三对象" + linkedList.contains(new Person("张三", 10)));
	}
}

class Person {
	private String name;
	private Integer age;
	public Person(String name, Integer age) {
		super();
		this.name = name;
		this.age = age;
	}
	@Override
	public String toString() {
		return "Person [name=" + name + ", age=" + age + "]";
	}
	
	/**
	 * 如果不重现equals方法,List就没办法删除Person元素
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (null == obj) {
			return false;
		}
		if (!(obj instanceof Person)) {
			return false;
		}
		Person per = (Person)obj;
		return this.name.equals(per.name) && this.age.equals(per.age);
	}
}
