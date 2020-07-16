package com.yale.test.io.imooc;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/*
 * 如果数据源不是文本，就只能使用InputStream，如果数据源是文本，使用Reader更方便一些。Writer和OutputStream是类似的。
 * 同步和异步
 * 同步IO是指，读写IO时代码必须等待数据返回后才继续执行后续代码，它的优点是代码编写简单，缺点是CPU执行效率低。
 * 而异步IO是指，读写IO时仅发出请求，然后立刻执行后续代码，它的优点是CPU执行效率高，缺点是代码编写复杂。
 * Java标准库的包java.io提供了同步IO，而java.nio则是异步IO。上面我们讨论的InputStream、OutputStream、Reader和Writer都是同步IO的抽象类，对应的具体实现类，以文件为例，有FileInputStream、FileOutputStream、FileReader和FileWriter。
 * 本节我们只讨论Java的同步IO，即输入/输出流的IO模型。
 * Java的IO标准库提供的InputStream根据来源可以包括：
    FileInputStream：从文件读取数据，是最终数据源；
    ServletInputStream：从HTTP请求读取数据，是最终数据源；
    Socket.getInputStream()：从TCP连接读取数据，是最终数据源；
    GZIPInputStream
 */
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
