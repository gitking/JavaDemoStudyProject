package com.yale.test.io.imooc;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileOutputStreamDemo {

	public static void main(String[] args) throws IOException {
		//如果文件不存在直接创建
		FileOutputStream out = new FileOutputStream("demo/out.bat");
		out.write('A');//一次写一个字节,写出了'A'的低八位
		out.write('B');
		int a = 10;//write只能写八位就是一个字节,那么写一个int需要写4次每次8位
		out.write(a >>> 24);
		out.write(a >>> 16);
		out.write(a >>> 8);
		out.write(a);
		byte[] gbk = "中国".getBytes("gbk");
		out.write(gbk);
		out.close();
		IOUtil.printHex("demo/out.bat");
	}

}
