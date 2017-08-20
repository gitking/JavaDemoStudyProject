package com.yale.test.math.array;

import java.util.Arrays;

public class ArraysTest {

	public static void main(String[] args) {
		int [] dataA = new int[]{1,2,3,4,5};
		int [] dataB = new int[]{1,2,3,4,5};
		System.out.println("把数组变成字符串输出:" + Arrays.toString(dataA));
		System.out.println("比较俩个数组是否相等(如果数组里面的元素一样,位置不一样也是不相等的):" + Arrays.equals(dataA, dataB));
		System.out.println("二分查找法:" + Arrays.binarySearch(dataA, 5));
		
		int [] dataC = new int[]{6,9,10,22,1,2,3,4,5};
		Arrays.sort(dataC);
		System.out.println("排序后的数组:" + Arrays.toString(dataC));
	}

}
