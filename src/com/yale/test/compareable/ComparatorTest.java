package com.yale.test.compareable;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;

/**
 * PersonSec类的比较助手
 * Comparator是属于挽救的比较器
 * Comparable这种比较器的核心价值在于:当一个类定义的时候就已经明确好了这个类的数据保存是需要排序的,但是很多情况下一个类
 * 设计的时候有可能没有考虑到排序的需求。这个时候需要Comparator类了
 * 注意Comparator是一个函数式接口,但是这个接口里面有很多抽象方法,这点跟我们之前学的函数式接口只能有一个方法,不太一样。
 * @author lenovo
 */
public class ComparatorTest implements Comparator<PersonSec>{
	public static void main(String[] args) {
		PersonSec perSec = new PersonSec(10,"张三");
		PersonSec per01 = new PersonSec(99,"李四");
		PersonSec per02 = new PersonSec(65,"王五");
		PersonSec [] perArr = new PersonSec[] {perSec, per01, per02};
		Arrays.sort(perArr,new ComparatorTest());
		System.out.println(Arrays.toString(perArr));
		
		System.out.println("当前的语言环境" + Locale.getDefault());
	}

	/**
	 * 15. 【强制】在JDK7版本及以上，Comparator实现类要满足如下三个条件，不然Arrays.sort，Collections.sort会抛IllegalArgumentException异常。 
	 * 说明：三个条件如下 
	 * 		1） x，y的比较结果和y，x的比较结果相反。 
	 * 		2） x>y，y>z，则x>z。 
	 * 		3） x=y，则x，z比较结果和y，z比较结果相同。 
	 * 反例：下例中没有处理相等的情况，交换两个对象判断结果并不互反，不符合第一个条件，在实际使用中 可能会出现异常。
	 * new Comparator<Student>() {
		@Override
		public int compare(Student o1, Student o2) {
			return o1.getId() > o2.getId() ? 1 : -1;
			}
		};
	 * 《阿里巴巴Java开发手册嵩山版2020.pdf》
	 */
	@Override
	public int compare(PersonSec o1, PersonSec o2) {
		if (o1.getAge() > o2.getAge()) {
			return 1;
		} else if (o1.getAge() < o2.getAge() ) {
			return -1;
		} else {
			return 0; 
		}
	}
}
