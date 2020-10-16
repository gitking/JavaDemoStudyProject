package com.yale.test.lambda.strem;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/*
 * 使用Map
 * Stream.map()是Stream最常用的一个转换方法，它把一个Stream转换为另一个Stream。
 * 所谓map操作，就是把一种操作运算，映射到一个序列的每一个元素上。例如，对x计算它的平方，可以使用函数f(x) = x * x。我们把这个函数映射到一个序列1，2，3，4，5上，就得到了另一个序列1，4，9，16，25：
 *             f(x) = x * x
 *                    │
                  	  │
	  ┌───┬───┬───┬───┼───┬───┬───┬───┐
	  │   │   │   │   │   │   │   │   │
	  ▼   ▼   ▼   ▼   ▼   ▼   ▼   ▼   ▼
	
	[ 1   2   3   4   5   6   7   8   9 ]
	
	  │   │   │   │   │   │   │   │   │
	  │   │   │   │   │   │   │   │   │
	  ▼   ▼   ▼   ▼   ▼   ▼   ▼   ▼   ▼
	
	[ 1   4   9  16  25  36  49  64  81 ]
 * 可见，map操作，把一个Stream的每个元素一一对应到应用了目标函数的结果上。
 * Stream<Integer> s = Stream.of(1, 2, 3, 4, 5);
 * Stream<Integer> s2 = s.map(n -> n * n);
 * 如果我们查看Stream的源码，会发现map()方法接收的对象是Function接口对象，它定义了一个apply()方法，负责把一个T类型转换成R类型：
 * <R> Stream<R> map(Function<? super T, ? extends R> mapper);
 * 其中，Function的定义是：
 * @FunctionalInterface
	public interface Function<T, R> {
	    // 将T类型转换为R:
	    R apply(T t);
	}
 * 利用map()，不但能完成数学计算，对于字符串操作，以及任何Java对象都是非常有用的。例如：
 */
public class StreamOfMap {
	public static void main(String[] args) {
		List<String> streamMap = new ArrayList<String>();
		Collections.addAll(streamMap, "   Apple ", " pear ", "ORANGE", "BaNaNa");
		//通过若干步map转换，可以写出逻辑简单、清晰的代码。
		streamMap.stream().map(String::trim)//去空格
		.map(String::toLowerCase)//变小写
		.forEach(System.out::println);//打印
		//使用map()把一组String转换为LocalDate并打印。
		String[] array = new String[] {" 2019-12-31 ", "2020 - 01-09 ", "2020- 05 - 01 ", "2022 - 02 - 01", " 2025-01 -01"};
		Arrays.stream(array).map(s->s.replaceAll("\\s+", ""))//去除空格
		.map(LocalDate::parse)//字符串转换为LocalDate
		.forEach(System.out::println);
		
		/**
		 * 使用filter
		 * Stream.filter()是Stream的另一个常用转换方法。
		 * 所谓filter()操作，就是对一个Stream的所有元素一一进行测试，不满足条件的就被“滤掉”了，剩下的满足条件的元素就构成了一个新的Stream。
		 * 例如，我们对1，2，3，4，5这个Stream调用filter()，传入的测试函数f(x) = x % 2 != 0用来判断元素是否是奇数，这样就过滤掉偶数，只剩下奇数，因此我们得到了另一个序列1，3，5：
		 * 		f(x) = x % 2 != 0
		
			                  │
			                  │
			  ┌───┬───┬───┬───┼───┬───┬───┬───┐
			  │   │   │   │   │   │   │   │   │
			  ▼   ▼   ▼   ▼   ▼   ▼   ▼   ▼   ▼
			
			[ 1   2   3   4   5   6   7   8   9 ]
			
			  │   X   │   X   │   X   │   X   │
			  │       │       │       │       │
			  ▼       ▼       ▼       ▼       ▼
			
			[ 1       3       5       7       9 ]
		 * 用IntStream写出上述逻辑，代码如下：
		 * 从结果可知，经过filter()后生成的Stream元素可能变少。
		 * filter()方法接收的对象是Predicate接口对象，它定义了一个test()方法，负责判断元素是否符合条件：
		 * @FunctionalInterface
			public interface Predicate<T> {
			    // 判断元素t是否符合条件:
			    boolean test(T t);
			}
		 * filter()除了常用于数值外，也可应用于任何Java对象。例如，从一组给定的LocalDate中过滤掉工作日，以便得到休息日：
		 */
		IntStream.of(1, 2, 3, 4, 5, 6, 7, 8, 9).filter(n -> n%2 != 0).forEach(System.out::println);
		
		//从一组给定的LocalDate中过滤掉工作日，以便得到休息日：
		System.out.println("从一组给定的LocalDate中过滤掉工作日，以便得到休息日：");
		Stream.generate(new LocalDateSupplier()).limit(31).filter(ldt -> ldt.getDayOfWeek() == DayOfWeek.SATURDAY || ldt.getDayOfWeek() == DayOfWeek.SUNDAY)
		.forEach(System.out::println);
		//小结:使用filter()方法可以对一个Stream的每个元素进行测试，通过测试的元素被过滤后生成一个新的Stream。
		
		//请使用filter过滤出及格的同学，然后打印名字:
		List<Person> persons = new ArrayList<Person>();
		persons.add(new Person("小米", 88));
		persons.add(new Person("小黑", 62));
		persons.add(new Person("小白", 45));
		persons.add(new Person("小黄", 78));
		persons.add(new Person("小红", 99));
		persons.add(new Person("小林", 58));
		persons.stream().filter(p -> p.score>=60).map(s -> s.name).forEach(System.out::println);;
	}
}

class LocalDateSupplier implements Supplier<LocalDate> {
	LocalDate start = LocalDate.of(2020, 1, 1);
	int n = -1;
	@Override
	public LocalDate get() {
		n++;
		return start.plusDays(n);
	}
}

class Person {
	String name;
	int score;
	
	Person(String name, int score) {
		this.name = name;
		this.score = score;
	}
}
