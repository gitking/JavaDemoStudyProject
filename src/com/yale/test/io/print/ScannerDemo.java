package com.yale.test.io.print;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * 打印流解决的是OutputStream的缺陷,而BufferedReader解决的是InputStream的缺陷,
 * 而java.util.Scanner解决的是Buffered类的缺陷(替换了Buffered类)
 * @author dell
 *
 */
public class ScannerDemo {

	public static void main(String[] args) throws FileNotFoundException {
		System.identityHashCode(20);
		Scanner sc = new Scanner(System.in);
		System.out.println("请输入内容:");
		/**
		 * sc.useDelimiter(pattern);自定义分隔福
		 */
		if(sc.hasNext()) {//现在又输入内容,不判断空字符串
			System.out.println("【ECHO】输入内容为:" + sc.next());
		}
		System.out.println("请输入年龄:");
		/**
		 * sc.useDelimiter(pattern);自定义分隔福
		 */
		if(sc.hasNextInt()) {//现在又输入内容,不判断空字符串
			System.out.println("【ECHO】输入年龄为:" + sc.next());
		} else {
			System.out.println("输入的不是数字");
		}
		
		if(sc.hasNext("\\d{4}-\\d{2}-\\d{2}")) {//可以自定义正则表达式
			System.out.println("【ECHO】输入生日为:" + sc.next());
		} else {
			System.out.println("输入的不是生日");
		}
		sc.close();
		
		/**
		 * Scanner实际上完美的替代了BufferedReader,而且更好的实现了InputStream的操作
		 * 以后除了二进制的文件拷贝的处理之外,那么只要是针对于程序的信息输出都使用打印流,信息输入都使用Scanner
		 */
		Scanner sca = new Scanner(new FileInputStream(new File("d:" + File.separator + "JavaDemo" + File.separator + "scanner.txt")));
		sca.useDelimiter("\n");
		while (sca.hasNext()) {
			System.out.println(sca.next());
		}
		sca.close();
	}
}
