package com.yale.test.io.file;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class WriterDemo {
	//字符适合于处理中文数据
	public static void main(String[] args) throws IOException {
		File file  = new File("d:" + File.separator + "JavaDemo" + File.separator + "writer.txt");
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		
		String msg = "世界和平!";
		Writer writer = new FileWriter(file);
		writer.write(msg);
		writer.write(msg, 0, 2);//只写俩个字符
		//字符流如果不关闭,内容是写不到的文件里面的,因为字符流用到了缓存,但是字节流不关闭,内容是可以写到文件里面的
		//字符流需要在缓存中将字节转换为字符,字节则不需要
		writer.close();
	}

}
