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
 * 问:我查看API的时候发现java.io这个包中有500万种类,你到底怎么知道要用哪一种？
 * 答:输入/输出的API使用一种模块化的"连接"概念来让你可以把连接串流与链接串流以各种可能应用到的排列组合连起来。这个串流链并不是只能衔接俩种层次。
 * 你可以连接多钟链接串流来达成所需的处置。
 * 通常你只会用到少数几个类而已。如果想要读写文本文件,或许BufferedReader与BufferedWriter(链接到FileReader与FileWriter上)就够用了。
 * 换言之,本书有涵盖到九成以上你所会用到的JAVA输入/输出。
 * 问:那1.4版本新增的输入/输出nio这个类呢？
 * 答:java.nio这个类带来重大的效能提升可以充分利用执行程序的机器上的原始容量。nio的一项关键能力是你可以直接控制buffer。另一项能力是non-blocking的输入和输出,它能让你的输入/输出程序代码在没有
 * 东西可读取或写入时不必等在那里。某些现有的类(包括FileInputStream和FileOutputStream)会利用到其中一部分的功能。nio使用起来更为复杂,除非你真的很需要新功能,不然的话,使用我们这里所讨论的
 * 功能方法会简单得多。此外,如果没有很小心的设计,nio可能会引发效能损失。非nio的输入/输出适合九成以上的应用,特别在你还是新手的时候更是如此。
 * 但你还是可以使用FileOutputStream并通过getChannel()方法来存取channel来开始使用nio。
 * 《Head First Java》
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
