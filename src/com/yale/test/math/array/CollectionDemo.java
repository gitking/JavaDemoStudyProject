package com.yale.test.math.array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

/**
 * public interface Collection<E> extends Iterable<E> 
 * List(允许重复),Set(不允许重复)
 * List有三个非常重要的子类：ArrayList,Vector,LinkedList,有序表的接口是List，具体的实现类有ArrayList，LinkedList等
 * Set有俩个常用子类:HashSet(不允许重复,无序存储),TreeSet(不允许重复,有序存储)
 * Java的java.util包主要提供了以下三种类型的集合：
    List：一种有序列表的集合，例如，按索引排列的Student的List；
    Set：一种保证没有重复元素的集合，例如，所有无重复名称的Student的Set；
    Map：一种通过键值（key-value）查找的映射表集合，例如，根据Student的name查找对应Student的Map。
 * 最后，Java访问集合总是通过统一的方式——迭代器（Iterator）来实现，它最明显的好处在于无需知道集合内部元素是按什么方式存储的。
 * 由于Java的集合设计非常久远，中间经历过大规模改进，我们要注意到有一小部分集合类是遗留类，不应该继续使用：
	    Hashtable：一种线程安全的Map实现；
	    Vector：一种线程安全的List实现；
	    Stack：基于Vector实现的LIFO的栈。
	还有一小部分接口是遗留接口，也不应该继续使用：
    	Enumeration<E>：已被Iterator<E>取代。
 * @author dell
 */
public class CollectionDemo {
	public static void main(String[] args) {
		
		/*
		 * 我们还可以通过List接口提供的of()方法，根据给定元素快速创建List：
		 * 但是List.of()方法不接受null值，如果传入null，会抛出NullPointerException异常。
		 * List<Integer> list = List.of(1, 2, 5);
		 */
		
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
		
		/*
		 * 但是，实现List接口并非只能通过数组（即ArrayList的实现方式）来实现，另一种LinkedList通过“链表”也实现了List接口。在LinkedList中，它的内部每个元素都指向下一个元素：
			        ┌───┬───┐   ┌───┬───┐   ┌───┬───┐   ┌───┬───┐
			HEAD ──>│ A │ ●─┼──>│ B │ ●─┼──>│ C │ ●─┼──>│ D │   │
			        └───┴───┘   └───┴───┘   └───┴───┘   └───┴───┘
			        我们来比较一下ArrayList和LinkedList：
								ArrayList		LinkedList
				获取指定元素		速度很快		需要从头开始查找元素
				添加元素到末尾		速度很快		速度很快
				在指定位置添加/删除	需要移动元素	不需要移动元素
				内存占用			少			较大
		 */
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
	 * 编写equals
		如何正确编写equals()方法？equals()方法要求我们必须满足以下条件：
		    自反性（Reflexive）：对于非null的x来说，x.equals(x)必须返回true；
		    对称性（Symmetric）：对于非null的x和y来说，如果x.equals(y)为true，则y.equals(x)也必须为true；
		    传递性（Transitive）：对于非null的x、y和z来说，如果x.equals(y)为true，y.equals(z)也为true，那么x.equals(z)也必须为true；
		    一致性（Consistent）：对于非null的x和y来说，只要x和y状态不变，则x.equals(y)总是一致地返回true或者false；
		    对null的比较：即x.equals(null)永远返回false。
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
		//如果Person有好几个引用类型的字段，上面的写法就太复杂了。要简化引用类型的比较，我们使用Objects.equals()静态方法：
		boolean res = Objects.equals(this.name, per.name);
		System.out.println("Objects.equals 会帮我们自动判断null值情况");
		return this.name.equals(per.name) && this.age.equals(per.age);
	}
	
	/*
	 * 注意到String类已经正确实现了hashCode()方法，我们在计算Person的hashCode()时，反复使用31*h，这样做的目的是为了尽量把不同的Person实例的hashCode()均匀分布到整个int范围。
	 * 和实现equals()方法遇到的问题类似，如果firstName或lastName为null，上述代码工作起来就会抛NullPointerException。为了解决这个问题，我们在计算hashCode()的时候，经常借助Objects.hash()来计算：
	 * 所以，编写equals()和hashCode()遵循的原则是：
		equals()用到的用于比较的每一个字段，都必须在hashCode()中用于计算；equals()中没有使用到的字段，绝不可放在hashCode()中计算。
		另外注意，对于放入HashMap的value对象，没有任何要求。
		hashCode()方法编写得越好，HashMap工作的效率就越高。
	 * @see java.lang.Object#hashCode()
	 */
	@Override
    public int hashCode() {
//        int h = 0;
//        h = 31 * h + this.name.hashCode();
//        h = 31 * h + age;
//        return h;
		return Objects.hash(name, age);
    }
}
