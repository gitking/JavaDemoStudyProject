package com.yale.test.design.interpreter.strategy;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;

import com.yale.test.design.interpreter.strategy.impl.OverDiscountStrategy;
import com.yale.test.design.interpreter.strategy.impl.PrimeDiscountStrategy;

/*
 * 策略
 * 定义一系列的算法，把它们一个个封装起来，并且使它们可相互替换。本模式使得算法可独立于使用它的客户而变化。
 * 策略模式：Strategy，是指，定义一组算法，并把其封装到一个对象中。然后在运行时，可以灵活的使用其中的一个算法。
 * 策略模式在Java标准库中应用非常广泛，我们以排序为例，看看如何通过Arrays.sort()实现忽略大小写排序：
 * 如果我们想忽略大小写排序，就传入String::compareToIgnoreCase，如果我们想倒序排序，就传入(s1, s2) -> -s1.compareTo(s2)，这个比较两个元素大小的算法就是策略。
 * 我们观察Arrays.sort(T[] a, Comparator<? super T> c)这个排序方法，它在内部实现了TimSort排序，但是，排序算法在比较两个元素大小的时候，
 * 需要借助我们传入的Comparator对象，才能完成比较。因此，这里的策略是指比较两个元素大小的策略，可以是忽略大小写比较，可以是倒序比较，也可以根据字符串长度比较。
 * 因此，上述排序使用到了策略模式，它实际上指，在一个方法中，流程是确定的，但是，某些关键步骤的算法依赖调用方传入的策略，这样，传入不同的策略，即可获得不同的结果，大大增强了系统的灵活性。
 * 如果我们自己实现策略模式的排序，用冒泡法编写如下：
 * 一个完整的策略模式要定义策略以及使用策略的上下文。我们以购物车结算为例，假设网站针对普通会员、Prime会员有不同的折扣，同时活动期间还有一个满100减20的活动，这些就可以作为策略实现。先定义打折策略接口：
 */
public class Test {
	public static void main(String[] args) {
		String[] array = new String[]{"apple", "Pear", "Banana", "orange"};
		Arrays.sort(array, String::compareToIgnoreCase);
		System.out.println(Arrays.toString(array));
		
		Arrays.sort(array, (s1, s2) ->-s1.compareTo(s2));
		System.out.println(Arrays.toString(array));
		
		Arrays.sort(array, (s1, s2) ->s1.compareTo(s2));
		System.out.println(Arrays.toString(array));
		
		sort(array, String::compareToIgnoreCase);
		System.out.println(Arrays.toString(array));
		
		
		DiscountContext ctx = new DiscountContext();
		//默认使用普通会员折扣:
		BigDecimal pay1 = ctx.calculatePrice(BigDecimal.valueOf(105));
		System.out.println(pay1);
		
		//使用满减折扣
		ctx.setStrategy(new OverDiscountStrategy());
		BigDecimal pay2 = ctx.calculatePrice(BigDecimal.valueOf(105));
		System.out.println(pay2);
		
		//使用Prime会员折扣
		ctx.setStrategy(new PrimeDiscountStrategy());
		BigDecimal pay3 = ctx.calculatePrice(BigDecimal.valueOf(105));
		System.out.println(pay3);
		
		//使用枚举类实现策略模式
		DiscountContext ctx1 = new DiscountContext();
		ctx1.setStrategyEnum(EnumDiscountStrategy.OverDiscountStrategy);
	}
	
	//如果我们自己实现策略模式的排序，用冒泡法编写如下：
	static <T> void sort(T[] a, Comparator<? super T> c) {
		for (int i=0; i<a.length-1; i++) {
			for(int j=0; j<a.length-1-i; j++) {
				if (c.compare(a[j], a[j+1]) > 0) {//注意这里比较俩个元素的大小依赖传入的策略
					T temp = a[j];
					a[j] = a[j+1];
					a[j+1] = temp;
				}
			}
		}
	}
}
