package com.yale.test.io;

import java.io.File;

public class FileUtils {

	public static void main(String[] args) {
		listDirectory(new File("D:\\GitWorkSpace"));
	}
	
	public static void listDirectory(File dir) {
		if (!dir.exists()) {
			throw new IllegalArgumentException("目录:" + dir + "不存在");
		}
		if(!dir.isDirectory()) {
			throw new IllegalArgumentException(dir + "不是目录");
		}
		
		String[] fileNames = dir.list();//返回的是字符串数组,是子目录的名称,不包含子目录下的内容
		for (String string : fileNames) {
			System.out.println(dir + "\\" + string);
		}
		
		//如果要遍历子目录下的内容就需要构造成File对象做递归操作,File提供了直接返回的
		File[] files = dir.listFiles();//返回的是子目录的(文件的抽象)
		if (files != null && files.length>0) {
			for (File file : files) {
				if (file.isDirectory()) {
					listDirectory(file);//递归
				} else {
					System.out.println(file);
				}
			}
		}
	}
}
