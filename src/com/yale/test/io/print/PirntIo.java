package com.yale.test.io.print;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

class PrintUtil {
	
	private OutputStream output;
	public PrintUtil(OutputStream output) {
		this.output = output;
	}
	public void print(String str) {
		try {
			this.output.write(str.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void println(String str) {
		this.print(str + "\r\n");
	}
	public void print (int data) {
		this.print(String.valueOf(data));
	}
	
	public void println(int data) {
		this.println(String.valueOf(data));
	}
	
	public void print(double data) {
		this.print(String.valueOf(data));
	}
	
	public void println(double data) {
		this.println(String.valueOf(data));
	}
	
	public void close() {
		try {
			this.output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
public class PirntIo {

	/**
	 * 打印流有字节打印流:PrintStream,字符打印流:PrintWriter,以后使用PrintWriter的几率很高
	 * 
	 * @param args
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException {
		PrintUtil pu = new PrintUtil(new FileOutputStream(new File("d:" + File.separator + "JavaDemo" + File.separator + "info.txt")));
		pu.print("姓名:");
		pu.println("阿玉");
		pu.println(1+10);
		pu.print(1.2 + 10.3);
		pu.close();
	}
}
