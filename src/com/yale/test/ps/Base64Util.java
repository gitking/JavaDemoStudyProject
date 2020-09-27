package com.yale.test.ps;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;

/*
 * 这个Base64的写法不太好,来自芋道源码
 * https://zhuanlan.zhihu.com/p/93860175
 */
public class Base64Util {
	private static final String UTF_8 = "UTF-8";//字符串编码
	
	/*
	 * 加密字符串
	 */
	public static String decodeData(String inputData) throws UnsupportedEncodingException {
		if (null == inputData) {
			return null;
		}
		return new String(Base64.decodeBase64(inputData.getBytes(UTF_8)), UTF_8);
	}
	
	public static String encodeData(String inputData) throws UnsupportedEncodingException {
		if (null == inputData) {
			return null;
		}
		return new String(Base64.encodeBase64(inputData.getBytes(UTF_8)), UTF_8);
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		String str = Base64Util.encodeData("我是中文");
		System.out.println(str);
		System.out.println(Base64Util.decodeData(str));
	}
}
