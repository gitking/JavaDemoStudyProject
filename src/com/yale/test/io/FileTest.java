package com.yale.test.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class FileTest {
	public static void main (String[] args) throws IOException {
		
	
		File file = new File("file.txt");
		System.out.println("判断路径是否是一个文件" + file.isFile());
		
		//下面这个俩个构造方法是一个意思
		//File file = new File("d:\\javaio\\file.txt");
		//File file = new File("fd:\\javaio", "file.txt");

		if (!file.exists()) {
			System.out.println(file.getAbsolutePath() + "不存在");
			if (file.isDirectory()) {//是否是目录
				System.out.println("直接打印file对象跟打印file.getAbsolutePath()是一样的结果" + file);
				System.out.println(file.getAbsolutePath() + "目录");
				System.out.println("打印父目录的路径:" + file.getParent());
			} else {
				System.out.println(file.getAbsolutePath() + "不是目录");
				boolean success = file.createNewFile();//创建问题件
				//file.mkdir();mkdir创建一级目录,上面没有别的目录
				//file.mkdirs();//如果多级目录都不存在,直接创建多级目录
				System.out.println("如果是目录打印目录的名字,如果是文件打印文件的名字:" + file.getName());
				if (success) {
					System.out.println("文件创建成功");
				} else {
					System.out.println("文件创建失败");
				}
			}
		} else {
			FileOutputStream outputStream = new FileOutputStream(file,true);
			OutputStreamWriter ows = new OutputStreamWriter(outputStream);
			BufferedWriter bs = new BufferedWriter(ows);
			bs.write("你好");
			bs.flush();
			ows.close();
			outputStream.close();
			System.out.println("写入完毕");
			FileInputStream in = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(isr);
			boolean isLoop = true;
			while (isLoop) {
				String context = br.readLine();
				if (context == null) {
					isLoop = false;
				} else {
					System.out.println(context);
				}
			}
			System.out.println("读取完毕");
			br.close();
			isr.close();
			in.close();
			System.out.println(file.getAbsolutePath() + "存在");
		}
	}
}
