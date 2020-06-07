package com.yale.test.math.array;

import java.util.Arrays;
import java.util.HashMap;

public class ArraysTest {
	public static void main(String[] args) {
		int [] dataA = new int[]{1,2,3,4,5};
		int [] dataB = new int[]{1,2,3,4,5};
		System.out.println("把数组变成字符串输出:" + Arrays.toString(dataA));
		System.out.println("比较俩个数组是否相等(如果数组里面的元素一样,位置不一样也是不相等的):" + Arrays.equals(dataA, dataB));
		System.out.println("二分查找法(元素不在数组里面就是找不到的话,返回负数):" + Arrays.binarySearch(dataA, 5));
		
		
		System.out.println("fill填充:");
		int[] dataFill = new int[5];
		Arrays.fill(dataFill, 5);
		System.out.println("fill就是用一个指定的数字把数组填满:" + Arrays.toString(dataFill));

		int [] dataC = new int[]{6,9,10,22,1,2,3,4,5};
		Arrays.sort(dataC);
		System.out.println("排序后的数组:" + Arrays.toString(dataC));
		
		HashMap<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("a", "A");
		paramMap.put("b", "B");
		paramMap.put("c", "C");
		String []paramNames = new String[paramMap.size()];
		paramMap.keySet().toArray(paramNames);//软通框架JAR包里面看到的
		System.out.println("数组转成String" + Arrays.toString(paramNames));
	}
}
