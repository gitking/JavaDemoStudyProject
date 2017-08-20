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
		if (!file.exists()) {
			System.out.println(file.getAbsolutePath() + "不存在");
			if (file.isDirectory()) {//是否是目录
				System.out.println(file.getAbsolutePath() + "目录");
			} else {
				System.out.println(file.getAbsolutePath() + "不是目录");
				boolean success = file.createNewFile();//创建问题件
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
