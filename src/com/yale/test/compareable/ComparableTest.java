package com.yale.test.compareable;

import java.util.Arrays;

public class ComparableTest {

	public static void main(String[] args) {
		
		int [] dataArr = new int []{1,2,3,4,5};
		Arrays.sort(dataArr);
		System.out.println("排序收的数组:" + Arrays.toString(dataArr));
		
		Person [] perArr = new Person[]{new Person(10,"张三"),new Person(22,"李四"),new Person(9,"李四")};
		Arrays.sort(perArr);
		System.out.println("排序收的数组(对象要想排序,必须实现Comparable这个泛型接口):" + Arrays.toString(perArr));

	}

}
