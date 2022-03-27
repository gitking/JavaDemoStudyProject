package com.yale.test.math.array;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

class PersonSec implements Comparable<PersonSec> {
	private String name;
	private Integer age;
	
	public PersonSec(String name, Integer age) {
		super();
		this.name = name;
		this.age = age;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	@Override
	public String toString() {
		return "name=" + this.name + ",aget = " + this.age + "\n";
	}
	@Override
	public int compareTo(PersonSec o) {
		if (this.age > o.getAge()) {
			return 1;
		} else if(this.age < o.getAge()) {
			return -1;
		} else {
			return this.name.compareTo(o.getName());
		}
	}
	
	/**
	 * 这里的HashCode是用eclipse自动生成的方法
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((age == null) ? 0 : age.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PersonSec other = (PersonSec) obj;
		if (age == null) {
			if (other.age != null)
				return false;
		} else if (!age.equals(other.age))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
/**
 * public interface Collection<E> extends Iterable<E> 
 * List(允许重复),Set(不允许重复)
 * List有三个非常重要的子类：ArrayList,Vector,LinkedList
 * Set有俩个常用子类:HashSet(不允许重复,无序存储),TreeSet(不允许重复,升序存储)
 * 
 * (六) 集合处理
 * 1. 【强制】关于hashCode和equals的处理，遵循如下规则： 
 * 	1） 只要覆写equals，就必须覆写hashCode。 
 *  2） 因为Set存储的是不重复的对象，依据hashCode和equals进行判断，所以Set存储的对象必须覆写这两种方法。 
 *  3） 如果自定义对象作为Map的键，那么必须覆写hashCode和equals。 说明：String因为覆写了hashCode和equals方法，所以可以愉快地将String对象作为key来使用。
 *  《阿里巴巴Java开发手册嵩山版2020.pdf》
 *  https://www.liaoxuefeng.com/wiki/1252599548343744/1265117217944672
 * @author dell
 */
public class SetDemo {
	public static void main(String[] args) {
		Set<String> set = new HashSet<String>();
		set.add("C");
		set.add("C");
		set.add("A");//重复元素
		set.add("D");//无序存储
		set.add("B");//无序存储

		/*
		 * 在聊天软件中，发送方发送消息时，遇到网络超时后就会自动重发，因此，接收方可能会收到重复的消息，在显示给用户看的时候，需要首先去重。请练习使用Set去除重复的消息：
    	 * HashSet是无序的，因为它实现了Set接口，并没有实现SortedSet接口；
    	 * TreeSet是有序的，因为它实现了SortedSet接口。
		 */
		System.out.println("注意输出的顺序既不是添加的顺序，也不是String排序的顺序，在不同版本的JDK中，这个顺序也可能是不同的。:");
		for (String s : set) {
            System.out.println(s);
        }
		
		Set<String> treeSet = new TreeSet<String>();
		treeSet.add("C");
		treeSet.add("C");
		treeSet.add("D");//重复元素
		treeSet.add("A");//升序存储
		treeSet.add("B");//无序存储
		System.out.println(treeSet);
		
		System.out.println("把HashSet换成TreeSet，在遍历TreeSet时，输出就是有序的，这个顺序是元素的排序顺序：:");
		for (String s : treeSet) {
            System.out.println(s);
        }
		
		/**
		 * TreeSet要求PersonSec类里面是所有属性都参与比较,否则比较会不准确
		 * 否则TreeSet就会认为name名字一样的就是同一个类了,就会去重.换句话说TreeSet判断重复元素是依靠Comparable接口来完成的
		 * TreeSet内部是一个TreeMap,HashSet是部是一个HashMap
		 * 使用TreeSet和使用TreeMap的要求一样，添加的元素必须正确实现Comparable接口，如果没有实现Comparable接口，那么创建TreeSet时必须传入一个Comparator对象。
		 */
		Set<PersonSec> treePerson = new TreeSet<PersonSec>();
		treePerson.add(new PersonSec("张三", 20));
		treePerson.add(new PersonSec("张三", 20));
		treePerson.add(new PersonSec("李四", 20));
		treePerson.add(new PersonSec("王五", 19));
		System.out.println("TreeSet要求所有属性都必须参与比较:" + treePerson);
		
		/**
		 * HashSet是利用equals和hashCode这俩个方法来判断重复的
		 * 俩个方法都返回true才认为是同一个对象
		 * equals()：判断两个instance是否逻辑相等；equals就是Object用来比较俩个对象逻辑上是否相等的方法
		 */
		Set<PersonSec> hashSetPerson = new HashSet<PersonSec>();
		hashSetPerson.add(new PersonSec("张三", 20));
		hashSetPerson.add(new PersonSec("张三", 20));
		hashSetPerson.add(new PersonSec("李四", 20));
		hashSetPerson.add(new PersonSec("王五", 19));
		System.out.println("HashSet是利用equals和hashCode这俩个方法来判断重复的:" + hashSetPerson);
		
		
		PersonSec stu = new PersonSec("HashSet测试引用", 20);
		stu.setAge(12);
		HashMap<String, PersonSec> hashMap = new HashMap<String, PersonSec>();
		hashMap.put("yy", stu);
		
		stu = new PersonSec("HashSet测试引用", 99);
		stu.setAge(20);
		
		PersonSec sd = hashMap.get("yy");
		System.out.println("hashSet内部有自己的引用:" + sd.getAge());
		
		//其实HashSet内部实现就是一个HashMap,只用了HashMap的键值
		HashSet sdf = new HashSet();
		sdf.add(1);
		
	}
}
