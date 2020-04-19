package com.yale.test.io.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class OutputStreamWriterDemo {

	public static void main(String[] args) throws IOException {
		/**
		 * 现在为止我们已经知道了俩类数据流:字节流和字符流,实际上这俩类流也是可以进行互相转换处理的
		 * OutputStreamWriter:将字节输出流变为字符输出流;
		 * InputStreamReader:将字节输入流变为字符输入流;
		 * 这样做有意义吗？
		 * 如果想发现以上俩个类的意义,则首先必须来看俩个类的继承关系
		 * 这里就用到了装饰者模式 
		 * 
		 * 在JAVA中还有俩类数据流:
		 *  字节内存流:ByteArrayInputStream,ByteArrayOutputStream
		 *  字符内存流:CharArrayReader,CharArrayWriter.
		 *  
		 *  
		 * LineNumberReader input = new LineNumberReader(ir); 
		 * https://www.cnblogs.com/Rozdy/p/5522873.html 
		 */
		File file = new File("d:" + File.separator + "JavaDemo" + File.separator + "OutputStreamWriter.txt");
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		OutputStream output = new FileOutputStream(file);
		Writer out = new OutputStreamWriter(output);//字节流变为字符流
		out.write("helloworld!");
		out.close();
	}
}
