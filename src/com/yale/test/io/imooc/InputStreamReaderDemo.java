package com.yale.test.io.imooc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class InputStreamReaderDemo {

	public static void main(String[] args) throws IOException {
		/**
		 * java的char是16位无符号整数,是字符的unicode编码(双字节编码)
		 */
		FileInputStream in = new FileInputStream("D:" + File.separator + "JavaDemo" + File.separator + "info.txt");
		InputStreamReader isr = new InputStreamReader(in);
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
