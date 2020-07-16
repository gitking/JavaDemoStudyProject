package com.yale.test.io.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/*
 * 如果数据源不是文本，就只能使用InputStream，如果数据源是文本，使用Reader更方便一些。Writer和OutputStream是类似的。
 */
public class FileInputStreamDeom {

	public static void main(String[] args) throws IOException {
		File file = new File("d:" + File.separator + "JavaDemo" + File.separator + "hello.txt");
		if (file.exists()) {
			InputStream in = new FileInputStream(file);
			byte[] data = new byte[1024];//定义每次可以读取的最大长度
			int len = in.read(data);//返回读取到的长度
			in.close();
			System.out.println("读取到的内容为:【" + new String(data, 0, len) + "】");
		}
	}
}
