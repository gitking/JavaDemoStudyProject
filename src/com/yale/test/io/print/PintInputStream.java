package com.yale.test.io.print;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * System.in对应的类型是InputStream,而这种的输入流指的是由用户通过 键盘进行的输入,JAVA本身并没有这种直接的 用户输入处理
 * 如果要想实现这种输入处理必须用java.io的模式来完成
 * @author dell
 *
 */
public class PintInputStream {

	public static void main(String[] args) throws IOException {
		InputStream in = System.in;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] data = new byte[10];
		System.out.print("请输入内容:");
		int temp = 0;//等待用户输入信息
		while ((temp = in.read(data)) != -1) {
			bos.write(data, 0, temp);//将用户输入的内容都先写进内存操作流中
			if (temp < data.length) {
				break;
			}
		}
		//System.out.println("【ECHO】输入内容为:" + new String(data, 0, temp));
		System.out.println("【ECHO】输入内容为:" + new String(bos.toByteArray()));
		print();
	}
	
	public static void print() throws IOException {
		InputStream in = System.in;
		StringBuffer buf = new StringBuffer();
		byte[] data = new byte[10];
		System.out.print("请输入内容:");
		int temp = 0;//等待用户输入信息
		while ((temp = in.read()) != -1) {//一个字节一个字节读
			if (temp == '\n') {
				break;
			}
			buf.append((char)temp);
		}
		//System.out.println("【ECHO】输入内容为:" + new String(data, 0, temp));
		System.out.println("【ECHO】输入内容为:" + new String(buf));
	}

}
