package com.yale.test.io.file;

import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/*
 * Reader是带编码转换器的InputStream，它把byte转换为char，而Writer就是带编码转换器的OutputStream，它把char转换为byte并输出。
 * Writer是所有字符输出流的超类，它提供的方法主要有：
	    写入一个字符（0~65535）：void write(int c)；
	    写入字符数组的所有字符：void write(char[] c)；
	    写入String表示的所有字符：void write(String s)。
 */
public class WriterDemo {
	//字符适合于处理中文数据
	public static void main(String[] args) throws IOException {
		File file  = new File("d:" + File.separator + "JavaDemo" + File.separator + "writer.txt");
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		
		String msg = "世界和平!";
		Writer writer = new FileWriter(file);
		//Writer writerEn = new FileWriter(file, StandardCharsets.UTF_8);JDK8才有这个问题
		writer.write(msg);
		writer.write(msg, 0, 2);//只写俩个字符
		//字符流如果不关闭,内容是写不到的文件里面的,因为字符流用到了缓存,但是字节流不关闭,内容是可以写到文件里面的
		//字符流需要在缓存中将字节转换为字符,字节则不需要
		writer.close();
		
		/*
		 * CharArrayWriter
		 * CharArrayWriter可以在内存中创建一个Writer，它的作用实际上是构造一个缓冲区，可以写入char，最后得到写入的char[]数组，这和ByteArrayOutputStream非常类似：
		 * StringWriter
		 * StringWriter也是一个基于内存的Writer，它和CharArrayWriter类似。实际上，StringWriter在内部维护了一个StringBuffer，并对外提供了Writer接口。
		 */
		CharArrayWriter writerChar = new CharArrayWriter();
		writerChar.write(65);
		writerChar.write(66);
		writerChar.write(67);
		char[] data = writerChar.toCharArray();//
	}

}
