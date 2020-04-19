package com.yale.test.io.print;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * BufferedReader类属于一个缓冲的输入流,而且是一个字符流的操作对象。但是必须要清楚一点,对于缓冲流在java.io中定义
 * 有俩类:字节缓冲流BufferdInputStream和字符缓冲流BufferedReader
 * @author dell
 */
public class BufferedReaderDemo {

	public static void main(String[] args) throws IOException {
		BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("请输入信息:");
		//默认的换行模式是BufferedReader的最大缺点,如果不是因为此缺点,该类还会继续使用
		String str = buf.readLine();//接收输入信息,默认使用回车换行
		System.out.println("【ECHO】输入信息为:" + str);
		if (str.matches("\\d{1,3}")) {
			System.out.println("【ECHO】输入信息为:" + Integer.parseInt(str));
		} else {
			System.out.println("输入的数据有错误!");
		}
	}
}
