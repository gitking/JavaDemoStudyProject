package com.yale.test.io.imooc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class InputStreamReaderDemo {

	public static void main(String[] args) throws IOException {
		/**
		 * java的char是16位无符号整数,是字符的unicode编码(双字节编码)
		 * Reader和InputStream有什么关系？
		 * 除了特殊的CharArrayReader和StringReader，普通的Reader实际上是基于InputStream构造的，因为Reader需要从InputStream中读入字节流（byte），然后，根据编码设置，
		 * 再转换为char就可以实现字符流。如果我们查看FileReader的源码，它在内部实际上持有一个FileInputStream。
		 * 既然Reader本质上是一个基于InputStream的byte到char的转换器，那么，如果我们已经有一个InputStream，想把它转换为Reader，
		 * 是完全可行的。InputStreamReader就是这样一个转换器，它可以把任何InputStream转换为Reader。示例代码如下：
		 */
		FileInputStream in = new FileInputStream("D:" + File.separator + "JavaDemo" + File.separator + "info.txt");
		InputStreamReader isr = new InputStreamReader(in);
		//构造InputStreamReader时，我们需要传入InputStream，还需要指定编码，就可以得到一个Reader对象。
		InputStreamReader isrEn = new InputStreamReader(in, StandardCharsets.UTF_8);
		
//		int c;
//		while ((c = isr.read()) != -1){
//			System.out.print((char)c);
//		}
		
		FileOutputStream out = new FileOutputStream("D:" + File.separator + "JavaDemo" + File.separator + "info1.txt");
		OutputStreamWriter osw = new OutputStreamWriter(out);
		char[] buffer = new char[8*1024];
		int d;
		while((d = isr.read(buffer, 0, buffer.length)) != -1) {
			String s = new 	String(buffer, 0, d);
			System.out.print(s);
			osw.write(buffer, 0, buffer.length);
		}
		isr.close();
		osw.close();
	}
}
