package com.yale.test.lambda.strem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/*
 * 其他操作
 * 我们把Stream提供的操作分为两类：转换操作和聚合操作。除了前面介绍的常用操作外，Stream还提供了一系列非常有用的方法。
 */
public class StreamOther {
	public static void main(String[] args) {
		/*
		 * 排序
		 * 对Stream的元素进行排序十分简单，只需调用sorted()方法：
		 * 此方法要求Stream的每个元素必须实现Comparable接口。如果要自定义排序，传入指定的Comparator即可：
		 * 注意sorted()只是一个转换操作，它会返回一个新的Stream。
		 */
		List<String> list = new ArrayList<String>();
		Collections.addAll(list, "Orange", "apple", "Banana");
		list.stream().sorted().collect(Collectors.toList());
		System.out.println(list);
		List<String> listSort = list.stream().sorted(String::compareToIgnoreCase).collect(Collectors.toList());
		System.out.println(listSort);
		
		/**
		 * 去重
		 * 对一个Stream的元素进行去重，没必要先转换为Set，可以直接用distinct()：
		 */
		List<String> listRe = new ArrayList<String>();
		Collections.addAll(listRe, "A", "B", "A", "C", "B", "D");
		List<String> listDis = listRe.stream().distinct().collect(Collectors.toList());
		System.out.println(listDis);
		
		/*
		 * 截取
		 * 截取操作常用于把一个无限的Stream转换成有限的Stream，skip()用于跳过当前Stream的前N个元素，limit()用于截取当前Stream最多前N个元素：
		 * 截取操作也是一个转换操作，将返回新的Stream。
		 */
		List<String> listSub = new ArrayList<String>();
		Collections.addAll(listSub, "A","B","C","D","E","F");
		List<String> listSk = listSub.stream().skip(2)//跳过俩个A,B
		.limit(3)//截取C, D, E
		.collect(Collectors.toList());//返回[C, D, E]
		System.out.println(listSk);
		
		/*
		 * 合并
		 * 将俩个Stream合并为一个Stream可以使用Stream的静态方法concat()
		 */
		List<String> listS = new ArrayList<String>();
		listS.add("A");
		listS.add("B");
		listS.add("C");
		Stream<String> s1 = listS.stream();
		
		List<String> listS2 = new ArrayList<String>();
		listS2.add("D");
		listS2.add("E");
		Stream<String> s2 = listS2.stream();
		
		//合并:
		Stream<String> s = Stream.concat(s1, s2);
		System.out.println("合并:" + s.collect(Collectors.toList()));//[A, B, C, D, E]
		
		/*
		 * flatMap
		 * 如果Stream的元素是集合：
		 * 而我们希望把上述Stream转换为Stream<Integer>，就可以使用flatMap()：
		 * 因此，所谓flatMap()，是指把Stream的每个元素（这里是List）映射为Stream，然后合并成一个新的Stream：
		 *  ┌─────────────┬─────────────┬─────────────┐
			│┌───┬───┬───┐│┌───┬───┬───┐│┌───┬───┬───┐│
			││ 1 │ 2 │ 3 │││ 4 │ 5 │ 6 │││ 7 │ 8 │ 9 ││
			│└───┴───┴───┘│└───┴───┴───┘│└───┴───┴───┘│
			└─────────────┴─────────────┴─────────────┘
			                     │
			                     │flatMap(List -> Stream)
			                     │
			                     │
			                     ▼
			   ┌───┬───┬───┬───┬───┬───┬───┬───┬───┐
			   │ 1 │ 2 │ 3 │ 4 │ 5 │ 6 │ 7 │ 8 │ 9 │
			   └───┴───┴───┴───┴───┴───┴───┴───┴───┘
		 */
		
		Stream<List<Integer>> slit = Stream.of(Arrays.asList(1, 2, 3),
				Arrays.asList(4, 5, 6), Arrays.asList(7, 8, 9));
		Stream<Integer> i = slit.flatMap(list1 -> list1.stream());
		
		/*
		 * 并行
		 * 通常情况下,对Stream的元素进行处理是单线程的,即一个一个元素进行处理,但是很多时候,我们希望可以进行并行处理Stream的元素,因为在元素数量
		 * 非常大的情况下,并行处理可以大大加快处理速度。
		 * 把一个普通Stream转换为可以并行处理的Stream非常简单，只需要用parallel()进行转换：
		 */
		Stream<String> ss = Stream.of("1", "2", "3", "4");
		String[] result = ss.parallel()//变成一个可以并行处理的Stream
				.sorted()//可以并行排序
				.toArray(String[]::new);
		//经过parallel()转换后的Stream只要可能,就会对后续操作进行并行处理。我们不需要编写任何多线程代码就可以享受到并行处理带来的执行效率的提升。
		
		
		List<String> props = Arrays.asList("a=1","b=2","c=3","d=4");
		Map<String, String> map = props.stream().parallel()//加上parallel() 和不加结果不一样
			.map(kv->{
			String[] ssD = kv.split("\\=");
			//Map<String, String> re = new ConcurrentHashMap<String, String>();
			Map<String, String> re = new HashMap<String, String>();
			re.put(ssD[0], ssD[1]);
			return re;
		}).reduce(
			//new ConcurrentHashMap<String, String>(), (m, kv)->{
			new HashMap<String, String>(), (m, kv)->{
			m.putAll(kv);
			return m;
		});
		map.forEach((k,v)->{
			//不一样的原因是:hashmap是线程不安全的，改用concurrenthashmap或者使用Collections.synchronizedMap给hashmap加锁即可
			System.out.println("加上parallel() 和不加结果不一样:" + k + " = " + v);
		});
		
		/*
		 * 其他聚合方法
		 * 除了reduce()和collect()外，Stream还有一些常用的聚合方法：
		 * count()：用于返回元素个数；
		 * max(Comparator<? super T> cp)：找出最大元素；
		 * min(Comparator<? super T> cp)：找出最小元素。
		 * 针对IntStream、LongStream和DoubleStream，还额外提供了以下聚合方法：
		 * sum()：对所有元素求和；
		 * average()：对所有元素求平均数。
		 * 还有一些方法，用来测试Stream的元素是否满足以下条件：
		 * boolean allMatch(Predicate<? super T>)：测试是否所有元素均满足测试条件；
		 * boolean anyMatch(Predicate<? super T>)：测试是否至少有一个元素满足测试条件。
		 * 最后一个常用的方法是forEach()，它可以循环处理Stream的每个元素，我们经常传入System.out::println来打印Stream的元素：
		 */
		Stream<String> sf = Stream.of("1", "2", "3");
		sf.forEach(str->{
			System.out.println("Hello, " + str);
		});
		/*
		 * 小结
		 *  Stream提供的常用操作有：
	     *  转换操作：map()，filter()，sorted()，distinct()；
	     *	合并操作：concat()，flatMap()；
		 *	并行处理：parallel()；
		 *	聚合操作：reduce()，collect()，count()，max()，min()，sum()，average()；
		 *	其他操作：allMatch(), anyMatch(), forEach()。
		 */
		
		
		IntStream instream = Arrays.stream(new int[]{1,2,3,4,5,6,7,8,99});
		System.out.println("Max value:" + instream.max());
		System.out.println("Min value:" + instream.min());
		System.out.println("Sum value:" + instream.sum());
		System.out.println("average value:" + instream.average());
		boolean a = instream.allMatch(x->(x-20)<0);
		boolean b = instream.allMatch(x->(x-20)>=0);
		
		/*
		 * stream has already been operated upon or closed
		 * 报这个错误的原因是因为:
		 * 所有对流的终端操作都会遍历流最终导致流管道关闭，也就是不能多次终端操作。
		 * 参见：Stream operations and pipelines(https://docs.oracle.com/en/java/javase/13/docs/api/java.base/java/util/stream/package-summary.html#StreamOps)
		 * 可以用IntStream.summaryStatistics()一次性终端操作获得{count, sum, min ,average, max}。
		 * 参见：Find maximum, minimum, sum and average of a list in Java 8(https://stackoverflow.com/questions/25988707/find-maximum-minimum-sum-and-average-of-a-list-in-java-8/25988761)
		 * 后面intStream.allMatch(x -> (x-20)>=0)等操作替代方法你自己再想一下办法吧。
		 */
		
		//你可以每执行一次前加***try(){}***语句,就像下面这样：
		//不过这样操作感觉也累赘，因为每次都需要初始化，应该有其他更简单的方法才对
		try(IntStream intStream = Arrays.stream(new int[] { 1,2,4,3,6,9,8,5,10,7,10 });){
			//找最大值                               
			System.out.println("Max value: "+intStream.max());
		}     
	
		try(IntStream intStream = Arrays.stream(new int[] { 1,2,4,3,6,9,8,5,10,7,10 });){//找最小值                                 
			System.out.println("Min value: "+intStream.min());
		}
	}
}
