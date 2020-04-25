package com.yale.test.java.fanshe.util;

public class StringUtils {
	private StringUtils(){}
	/**
	 * 首字母变成大写
	 * @param str
	 * @return
	 */
	public static String initcap(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}
}
