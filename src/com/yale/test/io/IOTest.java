package com.yale.test.io;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class IOTest {
	public static void main(String[] args) throws IOException {
		System.out.println("IO的操作部分重点掌握俩个代码模型.整个IO的核心组成:五个类(File,OutputStream,InputStream,Reader,Writer),一个接口(Serializable)");
		File file = new File(System.getProperty("java.io.tmpdir") + "java" + File.separator + "javaFile" + File.separator + "javaFile.txt");
		if (!file.getParentFile().exists()) {
			//mkdirs这个方法可以创建多级目录,mkdir只能创建一个目录
			boolean success = file.getParentFile().mkdirs();
			if (success) {
				File[] fileArr = file.getParentFile().listFiles();//得到目录下的所有文件
				for (int i=0; i<fileArr.length; i++) {
					File fileTemp = fileArr[i];
					if (fileTemp.isDirectory()) {
						System.out.println(fileTemp.getPath());
					} else if (fileTemp.isFile()) {
						System.out.println("获得文件的大小:" + myRound(file.length()/1024/1024, 2));
						System.out.println("获得文件的最后修改时间:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(file.lastModified())));
						System.out.println("文件是否可执行:" + file.canExecute());
						System.out.println("文件是否是只读:" + file.canRead());
						System.out.println("文件是否可写:" + file.canWrite());
						System.out.println("文件是否是隐藏文件:" + file.isHidden());
					}
				}
				System.out.println("父目录创建成功");
			} else {
				System.out.println("父目录创建失败");
			}
		}
		System.out.println("父目录:" + file.getParentFile().getAbsolutePath());
		if (file.exists()) {
			System.out.println("获得文件的大小:" + myRound(file.length()/1024/1024, 2));
			System.out.println("获得文件的最后修改时间:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(file.lastModified())));
			System.out.println("文件是否可执行:" + file.canExecute());
			System.out.println("文件是否是只读:" + file.canRead());
			System.out.println("文件是否可写:" + file.canWrite());
			System.out.println("文件是否是隐藏文件:" + file.isHidden());
			boolean success = file.delete();
			if (success) {
				System.out.println("文件删除成功");
			} else {
				System.out.println("文件删除失败");
			}
		} else {
			boolean success = file.createNewFile();
			if (success) {
				System.out.println("文件创建成功");
			} else {
				System.out.println("文件创建失败");
			}
		}
		
		listDir(new File("d:" + File.separator));
	}
	
	/**
	 * 保留俩位小数的方法
	 * 假如数字为:109.9831要保留俩位小数,那么先让109.9831  乘 100 round后  再除 100,就达到了保留俩位小数的目的,JS中常用这种方法进行保留俩位小数,java中不用
	 * @param num
	 * @param scale
	 * @return
	 */
	public static double myRound(double num, int scale) {
		return Math.round(num * Math.pow(10, scale)) / Math.pow(10, scale);
	}
	
	public static void listDir (File file) {
		if (file != null) {
			if (file.isDirectory()) {
				File [] fileArr = file.listFiles();
				if (fileArr != null) {
					for (int i=0; i<fileArr.length; i++) {
						listDir(fileArr[i]);
					}
				}
			}
			System.out.println(file.getPath());
			System.out.println(file.getAbsolutePath());
		}
	} 
}
