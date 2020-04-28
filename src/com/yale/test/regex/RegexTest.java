package com.yale.test.regex;

public class RegexTest {
	public static void main(String[] args) {
		System.out.println("abc是数字吗? == " + isNumber("abc"));
		System.out.println("123是数字吗? == " + isNumber("123"));
		
		System.out.println("用正则表达来校验字符串是否是纯数字,一行代码就解决了,不用自己再写一个方法了。");
		System.out.println("abc".matches("\\d+"));
		System.out.println("1230".matches("\\d+"));
		
		System.out.println("java.util是JAVA的一个工具包,可以研究一下");
	}
	
	public static boolean isNumber (String str) {
		System.out.println("char跟int是可以相互转换的");
		char c = '你';
		System.out.println("直接输出字符:" + c);
		int ic = c;
		System.out.println("把chari强转成int:" + ic);
		char ci = 987;
		System.out.println("整形987代表字符u:" + ci);
		
		char [] charArr = str.toCharArray();
		for (int i=0; i < charArr.length; i++) {//这个也可以判断一个字符数是否都是由数字组成的
			if (charArr[i] > '9' || charArr[i] < '0') {
				return false;
			}
		}
		return true;
	}
}
