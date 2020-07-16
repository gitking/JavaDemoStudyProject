package com.yale.test.io.imooc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class BufferedReaderDemo {

	public static void main(String[] args) throws IOException {
		/*
		 * 注意到在叠加多个FilterInputStream，我们只需要持有最外层的InputStream，并且，当最外层的InputStream关闭时（在try(resource)块的结束处自动关闭），
		 * 内层的InputStream的close()方法也会被自动调用，并最终调用到最核心的“基础”InputStream，因此不存在资源泄露。
		 */
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("d:" + File.separator + "JavaDemo" + File.separator + "info2.txt")));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("D:" + File.separator + "JavaDemo" + File.separator + "info3.txt")));
		
		String line = null;
		while((line = br.readLine()) != null) {
			System.out.println(line);//一次读一行,并不能识别换行
			bw.write(line);
			bw.newLine();//要自己写一个换行出来
			bw.flush();
		}
		br.close();
		bw.close();
	}
}
