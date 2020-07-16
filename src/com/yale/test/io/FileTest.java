package com.yale.test.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileTest {
	public static void main (String[] args) throws IOException {
		
		/*
		 * 注意Windows平台使用\作为路径分隔符，在Java字符串中需要用\\表示一个\。Linux平台使用/作为路径分隔符：
		 * 可以用.表示当前目录，..表示上级目录。
		 * File对象有3种形式表示的路径，一种是getPath()，返回构造方法传入的路径，一种是getAbsolutePath()，返回绝对路径，
		 * 一种是getCanonicalPath，它和绝对路径类似，但是返回的是规范路径。
		 */
		
		File wf = new File("C:\\Windows\\notepad1.exe");
		System.out.println("删除文件:" + wf.delete());
		
		File linuxF = new File("/usr/bin/javac");
		
		File ff = new File("..");
		System.out.println("..:" + ff.getPath());
		System.out.println("绝对路径:" + ff.getAbsolutePath());
		System.out.println("规范路径:" + ff.getCanonicalPath());
		
		System.out.println("文件是否可读:" + ff.canRead());
		System.out.println("文件是否可写(对目录而言，是否可执行表示能否列出它包含的文件和子目录。):" + ff.canWrite());
		System.out.println("文件是否执行:" + ff.canExecute());
		System.out.println("文件字节大小:" + ff.length());
		
		File f = File.createTempFile("temp-", ".txt");//创建一个临时文件
		f.deleteOnExit();//VM退出时自动删除
		System.out.println("临时文件的路径:" + f.getAbsolutePath());
		
		//因为Windows和Linux的路径分隔符不同，File对象有一个静态变量用于表示当前平台的系统分隔符：
		System.out.println("根据当前平台打印" + File.separator);// 根据当前平台打印"\"或"/"
		
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
		
		//当File对象表示一个目录时，可以使用list()和listFiles()列出目录下的文件和子目录名。listFiles()提供了一系列重载方法，可以过滤不想要的文件和目录：
		File flist = new File("C:\\Windows");
		File[] fs1 = flist.listFiles();
		printFiles(fs1);
		
		File[] fs2 = flist.listFiles(new FilenameFilter(){
			@Override
			public boolean accept(File dir, String name){
				return name.endsWith(".exe");
			}
		});
		printFiles(fs2);
		
		//Java标准库还提供了一个Path对象，它位于java.nio.file包。Path对象和File对象类似，但操作更加简单：
		//如果需要对目录进行复杂的拼接、遍历等操作，使用Path对象更方便。
		Path p1 = Paths.get(".", "project", "strudy");// 构造一个Path对象
		System.out.println("Paht:" + p1);
		Path p2 = p1.toAbsolutePath();//转换为绝对路径
		System.out.println(p2);
		Path p3 = p2.normalize();// 转换为规范路径
		System.out.println(p3);
		File fp = p3.toFile();//转换为File对象
		System.out.println(fp);
		for (Path p: Paths.get("..").toAbsolutePath()) {
			System.out.println("可以直接遍历p:" + p);
		}
		
	}
	
	static void printFiles(File[] files) {
		System.out.println("=============");
		if (files != null) {
			for (File f: files) {
				System.out.println(f);
			}
		}
		System.out.println("=============");
	}
}
