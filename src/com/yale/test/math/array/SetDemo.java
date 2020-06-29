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

		System.out.println(set);
		
		Set<String> treeSet = new TreeSet<String>();
		treeSet.add("C");
		treeSet.add("C");
		treeSet.add("D");//重复元素
		treeSet.add("A");//升序存储
		treeSet.add("B");//无序存储
		System.out.println(treeSet);
		
		
		/**
		 * TreeSet要求PersonSec类里面是所有属性都参与比较,否则比较会不准确
		 * 否则TreeSet就会认为name名字一样的就是同一个类了,就会去重.换句话说TreeSet判断重复元素是依靠Comparable接口来完成的
		 * TreeSet内部是一个TreeMap,HashSet是部是一个HashMap
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
