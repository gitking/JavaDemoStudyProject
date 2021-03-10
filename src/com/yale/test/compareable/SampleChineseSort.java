package com.yale.test.compareable;

import java.text.Collator;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/*
 * 中文拼音排序
 */
public class SampleChineseSort {
	@SuppressWarnings("rawtypes")
	private final static Comparator CHINA_COMPARE = Collator.getInstance(Locale.CHINA);
	private static void sortList() {
		List<String> list = Arrays.asList("张三", "李四", "王五");
		Collections.sort(list, CHINA_COMPARE);
		for (String str: list) {
			System.out.println("按照中文拼音排序:" + str);
		}
	}
	private static void sortArray() {
		String[] arr = {"张三", "李四", "王五"};
		Arrays.sort(arr, CHINA_COMPARE);
		for (String str: arr) {
			System.out.println("按照中文拼音排序:" + str);
		}
	}
	
	public static void main(String[] args) {
		sortArray();
		sortList();
	}
}
