package com.yale.test.io.file;

import java.io.CharArrayReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/*
 * 如果数据源不是文本，就只能使用InputStream，如果数据源是文本，使用Reader更方便一些。Writer和OutputStream是类似的。
 * Reader是Java的IO库提供的另一个输入流接口。和InputStream的区别是，InputStream是一个字节流，即以byte为单位读取，而Reader是一个字符流，即以char为单位读取：
 */
public class ReaderDemo {
	public static void main(String[] args) throws IOException {
		File file = new File("d:" + File.separator + "JavaDemo" + File.separator + "Writer.txt");
		if (file.exists()) {
			Reader reader = new FileReader(file);
			char[] data = new char[1024];
			/*
			 * read()这个方法读取字符流的下一个字符，并返回字符表示的int，范围是0~65535。如果已读到末尾，返回-1。
			 * Reader还提供了一次性读取若干字符并填充到char[]数组的方法：
			 * read(char[] c) 它返回实际读入的字符个数，最大不超过char[]数组的长度。返回-1表示流结束。
			 */
			int len = reader.read(data);
			System.out.println("读到的数据为:【" + new String(data,0, len) + "】");
			//字符流如果不关闭,内容是写不到的文件里面的,因为字符流用到了缓存,但是字节流不关闭,内容是可以写到文件里面的
			//字符流需要在缓存中将字节转换为字符,字节则不需要
			reader.close();
		} else {
			System.out.println("文件不存在");
		}
		
		/*
		 * CharArrayReader可以在内存中模拟一个Reader，它的作用实际上是把一个char[]数组变成一个Reader，
		 * 这和ByteArrayInputStream非常类似：
		 * StringReader可以直接把String作为数据源，它和CharArrayReader几乎一样：
		 */
		try (Reader rearCharArr = new CharArrayReader("hello".toCharArray())){
			
		}
		
		try (Reader rearCharArr = new StringReader("hello")){
			
		}
		
		/*
		 * 如果我们读取一个纯ASCII编码的文本文件，上述代码工作是没有问题的。但如果文件中包含中文，就会出现乱码，因为FileReader默认的编码与系统相关，
		 * 例如，Windows系统的默认编码可能是GBK，打开一个UTF-8编码的文本文件就会出现乱码。
		 * 要避免乱码问题，我们需要在创建FileReader时指定编码：
		 * Reader reader = new FileReader("src/readme.txt", StandardCharsets.UTF_8);
		 */
		File fileEncode = new File("d:" + File.separator + "JavaDemo" + File.separator + "Writer.txt");
		if (fileEncode.exists()) {
			Reader reader = new FileReader(fileEncode);//new FileReader(fileEncode, StandardCharsets.UTF_8);JDK没有这个构造方法
			char[] data = new char[1024];
			int len = reader.read(data);//这个方法读取字符流的下一个字符，并返回字符表示的int，范围是0~65535。如果已读到末尾，返回-1。
			System.out.println("读到的数据为:【" + new String(data,0, len) + "】");
			//字符流如果不关闭,内容是写不到的文件里面的,因为字符流用到了缓存,但是字节流不关闭,内容是可以写到文件里面的
			//字符流需要在缓存中将字节转换为字符,字节则不需要
			reader.close();
		} else {
			System.out.println("文件不存在");
		}
	}
}
