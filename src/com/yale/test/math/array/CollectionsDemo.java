package com.yale.test.math.array;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.DoubleSummaryStatistics;
import java.util.List;
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
		//emptyList.add("a");//这行代码会报错:java.lang.UnsupportedOperationException
		
		List<String> strList = new ArrayList<String>();
		Collections.addAll(strList, "A", "B", "C");//相当于调用了3次add方法
		System.out.println(strList);
		Collections.reverse(strList);//翻转集合
		System.out.println("翻转集合:" + strList);
		
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
		
		Collection<String> resultList = CollectionUtils.subtract(listOne, listTwo);
		System.out.println("结果是listOne减去listTwo的结果:" + resultList);
		
		Collection<String> listInter = CollectionUtils.intersection(listOne, listTwo);
		System.out.println("结果是俩个集合的交集:" + listInter);

	}
}
