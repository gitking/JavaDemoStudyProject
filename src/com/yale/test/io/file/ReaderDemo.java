package com.yale.test.io.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class ReaderDemo {
	public static void main(String[] args) throws IOException {
		File file = new File("d:" + File.separator + "JavaDemo" + File.separator + "Writer.txt");
		if (file.exists()) {
			Reader reader = new FileReader(file);
			char[] data = new char[1024];
			int len = reader.read(data);
			System.out.println("读到的数据为:【" + new String(data,0, len) + "】");
			//字符流如果不关闭,内容是写不到的文件里面的,因为字符流用到了缓存,但是字节流不关闭,内容是可以写到文件里面的
			//字符流需要在缓存中将字节转换为字符,字节则不需要
			reader.close();
		} else {
			System.out.println("文件不存在");
		}
	}
}
