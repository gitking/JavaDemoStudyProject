package com.yale.test.io.imooc;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class IOUtil {

	public static void main(String[] args) throws IOException {
		printHex("d:\\JavaDemo\\print.txt");
		System.out.println();
		System.out.println("*********************************");
		System.out.println();
		printHexByByteArray("D:\\GitWorkSpace\\JavaDemoStudyProject\\src\\com\\yale\\test\\io\\ByteArrayInputStreamDemo.java");
	}
	
	public static void printHex(String fileName) throws IOException {
		FileInputStream in = new FileInputStream(fileName);
		int b;
		int i = 1;
		while((b = in.read()) != -1) {
			if (b <= 0xf) {//0xf是1位就是1bit的意思
				//单位数前面补0
				System.out.print("0");
			}
			System.out.print(Integer.toHexString(b) + " ");
			if (i++ %10 == 0) {
				System.out.println();
			}
		}
		in.close();
	}
	
	public static void printHexByByteArray(String fileName) throws IOException {
		FileInputStream in = new FileInputStream(fileName);
		byte[] buf = new byte[20 * 1024];
		/**
		 * java的char是16位无符号整数,是字符的unicode编码(双字节编码)
		 * 从in中批量读取字节,放入到buf这个字节数组中,
		 * 从第0个位置开始做,最多放buf.length个
		 * 返回的是读到的字节的个数
		 */
		int bytes = in.read(buf, 0, buf.length);
		int j=1;
		for (int i=0;i<bytes; i++) {
			if(buf[i] <=0xf) {
				System.out.print("0");
			}
			System.out.print(Integer.toHexString(buf[i]) + " ");
			if (j++%10==0) {
				System.out.println();
			}
		}
		in.close();
	}
	
	/**
	 * 文件拷贝，字节批量读取
	 * @param srcFile
	 * @param destFile
	 * @throws IOException
	 */
	public static void copyFile(File srcFile, File destFile) throws IOException {
		if (!srcFile.exists()) {
			throw new IllegalArgumentException("文件:" + srcFile + "不存在");
		}
		if (!srcFile.isFile()) {
			throw new IllegalArgumentException(srcFile + "不是文件");
		}
		FileInputStream in = new FileInputStream(srcFile);
		FileOutputStream out = new FileOutputStream(destFile);
		byte[] buf = new byte[8*1024];
		int b;
		while((b = in.read(buf, 0, buf.length)) != -1) {
			out.write(buf, 0, b);
			out.flush();
		}
		in.close();
		out.close();
	}
	
	/**
	 * 进行文件的拷贝,利用带缓冲的字节流
	 * @param srcFile
	 * @param destFile
	 * @throws IOException
	 */
	public static void copyFileByBuffer(File srcFile, File destFile) throws IOException {
		if (!srcFile.exists()) {
			throw new IllegalArgumentException("文件:" + srcFile + "不存在");
		}
		if (!srcFile.isFile()) {
			throw new IllegalArgumentException(srcFile + "不是文件");
		}
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(srcFile));
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destFile));
		int c;
		while((c = bis.read()) != -1){
			bos.write(c);
			bos.flush();
		}
		bis.close();
		bos.close();
	}
	
	/**
	 * 单字节,不带缓冲进行文件拷贝
	 * @param srcFile
	 * @param destFile
	 * @throws IOException
	 */
	public static void copyFileByByte(File srcFile, File destFile) throws IOException {
		if (!srcFile.exists()) {
			throw new IllegalArgumentException("文件:" + srcFile + "不存在");
		}
		if (!srcFile.isFile()) {
			throw new IllegalArgumentException(srcFile + "不是文件");
		}
		FileInputStream in = new FileInputStream(srcFile);
		FileOutputStream out = new FileOutputStream(destFile);
		int c;
		while((c = in.read()) != -1) {
			out.write(c);
			out.flush();
			
		}
		in.close();
		out.close();
	}
}
