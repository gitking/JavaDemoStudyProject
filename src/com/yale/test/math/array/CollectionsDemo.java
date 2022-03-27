package com.yale.test.math.array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.DoubleSummaryStatistics;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections.CollectionUtils;
class Order {
	private String title;
	private double price;
	private int amount;
	public Order(String title, double price, int amount) {
		super();
		this.title = title;
		this.price = price;
		this.amount = amount;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
}
public class CollectionsDemo {
	public static void main(String[] args) {
		//创建一个空集合,空集合不能操作
		List<String> emptyList = Collections.EMPTY_LIST;
		//List<String> list1 = List.of();这俩个方法是等价的
		//emptyList.add("a");//这行代码会报错:java.lang.UnsupportedOperationException
		
		//创建一个空Map
		Map<String,String> emptyMap = Collections.EMPTY_MAP;
		
		//创建一个空Set
		Set<String> emptySet = Collections.EMPTY_SET;
		
		//Collections提供了一系列方法来创建一个单元素集合：
		/*
		 * 7. 【强制】Collections类返回的对象，如：emptyList()/singletonList()等都是immutable list，不可对其进行添加或者删除元素的操作。 
		 * 反例：如果查询无结果，返回Collections.emptyList()空集合对象，调用方一旦进行了添加元素的操作，就会触发UnsupportedOperationException异常。
		 */
		List<String> singleList = Collections.singletonList("");
		//List<String> list1 = List.of("apple");这俩个方法是等价的
		/*
		 * 实际上，使用List.of(T...)更方便，因为它既可以创建空集合，也可以创建单元素集合，还可以创建任意个元素的集合：
		 * List<String> list1 = List.of(); // empty list
		 *	List<String> list2 = List.of("apple"); // 1 element
		 *	List<String> list3 = List.of("apple", "pear"); // 2 elements
		 *	List<String> list4 = List.of("apple", "pear", "orange"); // 3 elements
		 */
		Map<String, String> singleMap = Collections.singletonMap("", "");
		Set<String> singleSet = Collections.singleton("");
		
		List<String> listTest = new ArrayList<String>();
		listTest.add("不变结合");
		//Collections还提供了一组方法把可变集合封装成不可变集合：
		List<String> listFinal = Collections.unmodifiableList(listTest);
		//listFinal.add("添加补进去");//报错java.lang.UnsupportedOperationException
		//因此，如果我们希望把一个可变List封装成不可变List，那么，返回不可变List后，最好立刻扔掉可变List的引用，
		//这样可以保证后续操作不会意外改变原始对象，从而造成“不可变”List变化了：
		listFinal = null;
		System.out.println(listFinal);
		
		
		//Collections还提供了一组方法，可以把线程不安全的集合变为线程安全的集合：
		//因为从Java 5开始，引入了更高效的并发集合类，所以下面这几个同步方法已经没有什么用了。
		//https://www.liaoxuefeng.com/wiki/1252599548343744/1299919855943714
		List<String> listSafe = Collections.synchronizedList(listTest);
		Set<String> setSafe = Collections.synchronizedSet(singleSet);
		Map<String,String> mapSafe = Collections.synchronizedMap(singleMap);
		//但是它实际上synchronizedMap是用一个包装类包装了非线程安全的Map，然后对所有读写方法都用synchronized加锁，这样获得的线程安全集合的性能比java.util.concurrent集合要低很多，所以不推荐使用。
		
		List<String> strList = new ArrayList<String>();
		Collections.addAll(strList, "A", "B", "C");//相当于调用了3次add方法
		System.out.println(strList);
		
        Collections.sort(strList);//对一个结合排序
        
		Collections.reverse(strList);//翻转集合
		System.out.println("翻转集合:" + strList);
		
		//洗牌算法shuffle可以随机交换List中的元素位置:
        Collections.shuffle(strList);
        
		
		/**
		 * MapReduce属于数据的俩个操作阶段
		 * Map:处理数据
		 * Reduce:分析数据
		 */
		List<String> all = new ArrayList<String>();
		all.add("1-java");
		all.add("2-python");
		all.add("3-javascript");
		all.add("4-jsp");
		all.add("5-redis");
		all.add("6-nginx");
		all.add("7-sso");
		
		/**
		 * forEach的参数是一个Consumer消费型接口,Consumer消费型接口特点是无返回值,但是有参数接收
		 */
		all.forEach(System.out::println);//方法引用
		all.forEach((x)->{
			System.out.println("forEach的参数是一个消费型接口:" + x);
		});//方法引用
		
		
		Stream<String> stream = all.stream();
		//System.out.println("个数:" + stream.count());
		
		/**
		 * filter的参数是一个Predicate断言型的函数式消费接口
		 */
		//System.out.println("过滤后的个数:" + stream.filter((e)->e.contains("java")).count());//数据过滤,直输出带java关键字的数据
		
		/**
		 * collect是一个收集器
		 */
		//List<String> collList = stream.filter((e)->e.contains("java")).collect(Collectors.toList());
		//System.out.println("收集的数据为::" + collList);
		
		/**
		 * 设置取出最大内容:Stream<T> limit(long maxSize);
		 * 跳过的数据量：Stream<T> skip(long n);
		 */
		//List<String> collList = stream.skip(4).limit(2).collect(Collectors.toList());
		//System.out.println("收集的数据为::" + collList);
		
		/**
		 * map方法接收的是Function函数型的接口,
		 * map方法是处理数据阶段
		 */
//		List<String> collList = stream.skip(4).limit(2).map((s)->s.toUpperCase())
//				.collect(Collectors.toList());
//		System.out.println("收集的数据为::" + collList);
		
		/**
		 * 这里的map也可以放在前面,放在前面的意思是先把所有的数据变成大写,再跳过4个数据,再取出俩个数据,不要这么做
		 * map应该只处理它该处理的数据
		 */
		List<String> collList = stream.map((s)->s.toUpperCase()).skip(4).limit(2)
				.collect(Collectors.toList());
		System.out.println("收集的数据为::" + collList);
		
		/**
		 * MapReduce是整个Stream核心所在,可以这么说,之前的所有操作都只是做了一个MapReduce衬托,
		 * Map:处理数据
		 * Reduce:分析数据
		 */
		List<Order> orderList = new ArrayList<Order>();
		orderList.add(new Order("手机", 1999.00, 20));
		orderList.add(new Order("笔记本电脑", 7099.00, 50));
		orderList.add(new Order("Java开发", 79.00, 20000));
		orderList.add(new Order("铅笔", 1.80, 200000));
		/**
		 * 计算以上所有商品花费的总费用
		 */
		double allprice =orderList.stream().map((obj)->obj.getPrice()*obj.getAmount()).reduce((sum, x)->sum+x).get();
		System.out.println("购买以上所有商品需要花费:" + allprice + "元");
		
		/**
		 * 统计分析： DoubleStream mapToDouble(ToDoubleFunction<? super T> mapper);
		 */
		DoubleSummaryStatistics dss = orderList.stream().mapToDouble((obj)->obj.getPrice()*obj.getAmount()).summaryStatistics();
		System.out.println("总量:" + dss.getCount());
		System.out.println("平均值:" + dss.getAverage());
		System.out.println("最大:" + dss.getMax());
		System.out.println("最小:" + dss.getMin());
		System.out.println("总和:" + dss.getSum());
		
		/**
		 * subtract减去
		 * intersection是交集
		 * CollectionUtils是Apache的一个jar包
		 * 【强制】避免用Apache Beanutils进行属性的copy。 
		 * 说明：Apache BeanUtils性能较差，可以使用其他方案比如Spring BeanUtils, Cglib BeanCopier，注意均是浅拷贝。《阿里巴巴Java开发手册（泰山版）.
		 */
		List<String> listOne = new ArrayList<String>(); 
		listOne.add("1");
		listOne.add("2");
		listOne.add("3");
		
		List<String> listTwo = new ArrayList<String>(); 
		listTwo.add("3");
		listTwo.add("4");
		listTwo.add("5");
		listTwo.add("1");
		//遍历List
		for (int i=0; i<listTwo.size(); i++) {
			String s = listTwo.get(i);
			System.out.println("for循环这种方式并不推荐：" + s);
		}
		/*
		 * for循环这种方式并不推荐，一是代码复杂，二是因为get(int)方法只有ArrayList的实现是高效的，换成LinkedList后，索引越大，访问速度越慢。
		 * 所以我们要始终坚持使用迭代器Iterator来访问List。Iterator本身也是一个对象，但它是由List的实例调用iterator()方法的时候创建的。
		 * Iterator对象知道如何遍历一个List，并且不同的List类型，返回的Iterator对象实现也是不同的，但总是具有最高的访问效率。
		 * Iterator对象有两个方法：boolean hasNext()判断是否有下一个元素，E next()返回下一个元素。因此，使用Iterator遍历List代码如下：
		 * 有童鞋可能觉得使用Iterator访问List的代码比使用索引更复杂。但是，要记住，通过Iterator遍历List永远是最高效的方式。并且，由于Iterator遍历是如此常用，
		 * 所以，Java的for each循环本身就可以帮我们使用Iterator遍历。
		 * 实际上，只要实现了Iterable接口的集合类都可以直接用for each循环来遍历，Java编译器本身并不知道如何遍历集合对象，
		 * 但它会自动把for each循环变成Iterator的调用，原因就在于Iterable接口定义了一个Iterator<E> iterator()方法，强迫集合类必须返回一个Iterator实例。
		 */
		for (Iterator<String> it = listTwo.iterator();it.hasNext();) {
			String s = it.next();
			System.out.println("遍历集合必须使用Iterator:" + s);
		}
		
		Collection<String> resultList = CollectionUtils.subtract(listOne, listTwo);
		System.out.println("结果是listOne减去listTwo的结果:" + resultList);
		
		Collection<String> listInter = CollectionUtils.intersection(listOne, listTwo);
		System.out.println("结果是俩个集合的交集:" + listInter);
		
		CollectionUtils.retainAll(all, listInter);//俩个集合取交集
		
		CollectionUtils.union(all, listInter);//俩个集合取并集
		
		
		
		//两个List集合取交集
		//List<String> listStr = Arrays.asList("a","b","c");//java.lang.UnsupportedOperationException
		//List<String> listStr2 = Arrays.asList("a","b","d");//java.lang.UnsupportedOperationException
		List<String> listStr = new ArrayList<String>();
		listStr.add("a");
		listStr.add("b");
		listStr.add("c");
		List<String> listStr2 = new ArrayList<String>();
		listStr2.add("a");
		listStr2.add("b");
		listStr2.add("d");
		listStr.retainAll(listStr2);//不能使用Arrays.asList("a","b","c")否则会报错java.lang.UnsupportedOperationException
		System.out.println("取俩个list结合的交集:" + listStr);
		
		/**
		 * 5. 【强制】ArrayList的subList结果不可强转成ArrayList，否则会抛出 ClassCastException异常：java.util.RandomAccessSubList cannot be cast to java.util.ArrayList。 
		 * 说明：subList()返回的是ArrayList的内部类SubList，并不是 ArrayList本身，而是ArrayList 的一个视图，对于SubList的所有操作最终会反映到原列表上。
		 * 8. 【强制】在subList场景中，高度注意对父集合元素的增加或删除，均会导致子列表的遍历、增加、删除产生ConcurrentModificationException 异常。
		 */
		List<String> listStrSub = new ArrayList<String>();
		listStrSub.add("A");
		listStrSub.add("B");
		listStrSub.add("C");
		//Exception in thread "main" java.lang.ClassCastException: java.util.ArrayList$SubList cannot be cast to java.util.ArrayList
		ArrayList<String> subArrList = (ArrayList<String>)listStrSub.subList(0, 1);
		/**
		 * 10. 【强制】在使用Collection接口任何实现类的addAll()方法时，都要对输入的集合参数进行NPE判断。 
		 * 说明：在ArrayList#addAll方法的第一行代码即Object[] a = c.toArray(); 其中c为输入集合参数，如果为null，则直接抛出异常。
		 * 《阿里巴巴Java开发手册嵩山版2020.pdf》
		 */
		listStrSub.addAll(collList);
	}
}
