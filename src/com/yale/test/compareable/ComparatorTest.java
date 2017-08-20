package com.yale.test.compareable;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;

/**
 * PersonSec类的比较助手
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
