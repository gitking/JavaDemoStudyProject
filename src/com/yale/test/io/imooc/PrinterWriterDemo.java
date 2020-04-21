package com.yale.test.io.imooc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class PrinterWriterDemo {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("d:" + File.separator + "JavaDemo" + File.separator + "info2.txt")));
		PrintWriter pw = new PrintWriter("d:" + File.separator + "JavaDemo" + File.separator + "info4.txt");

		String line = null;
		while((line = br.readLine()) != null) {
			System.out.println(line);//一次读一行,并不能识别换行
//			bw.write(line);
//			bw.newLine();//要自己写一个换行出来
//			bw.flush();
			pw.println(line);//自带换行
			pw.flush();
		}
		br.close();
		pw.close();
	}
}
