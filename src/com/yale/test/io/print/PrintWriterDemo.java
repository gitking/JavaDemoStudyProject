package com.yale.test.io.print;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

/*
 * PrintWriter
 * PrintStream最终输出的总是byte数据，而PrintWriter则是扩展了Writer接口，它的print()/println()方法最终输出的是char数据。
 * 两者的使用方法几乎是一模一样的：
 */
public class PrintWriterDemo {

	public static void main(String[] args) throws FileNotFoundException {
		String name = "小鱼仔";
		int age = 20;
		double salart = -10000.88884646;
		PrintWriter pu = new PrintWriter(new FileOutputStream(new File("d:" + File.separator + "JavaDemo" + File.separator + "print.txt")));
		pu.printf("姓名:%s, 年龄:%d, 工资 :%7.2f", name, age, salart);
		pu.close();
		
		String str = String.format("姓名:%s, 年龄:%d, 工资 :%7.2f", name, age, salart);
		System.out.println("String也可以实现格式化输出:" + str);
	}
}
