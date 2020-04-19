package com.yale.test.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 在JAVA中还有俩类数据流:
 *  字节内存流:ByteArrayInputStream,ByteArrayOutputStream
 *  字符内存流:CharArrayReader,CharArrayWrite
 * @author dell
 */
public class ByteArrayInputStreamDemo {
	public static void main(String[] args) throws IOException {
		String msg = "hello world!";
		InputStream	 input = new ByteArrayInputStream(msg.getBytes());
		OutputStream output = new ByteArrayOutputStream();
		int temp = 0;
		while((temp = input.read()) != -1) {
			output.write(Character.toUpperCase(temp));//针对每个字节进行处理,变成大写
		}//此时所有的数据都在OutputStream类中了
		System.out.println(output);//直接输出对象,调用toString()方法
		input.close();
		output.close();
	}
}
