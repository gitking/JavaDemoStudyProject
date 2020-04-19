package com.yale.test.io.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class CopyUtil {
	private CopyUtil() {
	}
	
	/**
	 * 判断要拷贝的源路径是否存在
	 * @param path
	 * @return
	 */
	public static boolean fileExists(String path) {
		return new File(path).exists();
	}
	
	/**
	 * 根据传入的路径来判断其父路劲是否存在,如果不存在则创建,存在啥也不干
	 * @param path 要操作的文件路径,通过此路劲取的父路径
	 * @return
	 */
	public static void createParentDirecoty(String path) {
		File file = new File(path);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
	}
	
	public static boolean copy(String srcPath, String desPath) throws Exception {
		File inFile = new File(srcPath);
		File outFile = new File(desPath);
		boolean flag = false;
		InputStream input = null;
		OutputStream output = null;
		try {
			input = new FileInputStream(inFile);
			output = new FileOutputStream(outFile);
			copyHandle02(input, output);
			flag = true;
		} catch (Exception e) {
			throw e;
		} finally {
			input.close();
			output.close();
		}
		return flag;
	}
	
	public static void copyHandle(InputStream in, OutputStream output) throws IOException {
		long start = System.currentTimeMillis();
		int temp = 0;//读取到的数据长度
		do {
			temp = in.read();//一次读一个字节,效率极低,工作中绝对不用
			output.write(temp);
		} while(temp != -1);
		long end = System.currentTimeMillis();
		System.out.println("【统计】+拷贝文件所花费的时间:" + (end - start) + "毫秒");
	}
	
	
	/**
	 * 这个方法效率高
	 * @param in
	 * @param output
	 * @throws IOException
	 */
	public static void copyHandle02(InputStream in, OutputStream output) throws IOException {
		long start = System.currentTimeMillis();
		int temp = 0;//读取到的数据长度
		byte[] data = new byte[2048];
		while ((temp = in.read(data)) != -1) {
			output.write(data, 0, temp);
		}
		long end = System.currentTimeMillis();
		System.out.println("【统计】+拷贝文件所花费的时间:" + (end - start) + "毫秒");
	}
}
public class Copy {

	//main方法传参
	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.out.println("错误的执行方式,命令调用：java copy 源文件路径 目标文件路径");
			System.exit(2);
		}
		
		if (CopyUtil.fileExists(args[0])) {
			CopyUtil.createParentDirecoty(args[1]);
			System.out.println(CopyUtil.copy(args[0], args[1]) ? "文件拷贝成功" : "文件拷贝失败");
		} else {
			System.out.println("对不起,源文件路径不存在,无法执行拷贝操作");
			System.exit(1);
		}
	}
}
