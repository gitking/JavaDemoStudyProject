package com.yale.test.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 在JAVA中还有俩类数据流:
 *  字节内存流:ByteArrayInputStream,ByteArrayOutputStream
 *  字符内存流:CharArrayReader,CharArrayWrite
 * @author dell
 */
public class ByteArrayInputStreamDemo2 {
	public static void main(String[] args) throws IOException {
		File[] fileArr = new File[]{
				new File("d:" + File.separator + "JavaDemo" + File.separator + "data-a.txt"),
				new File("d:" + File.separator + "JavaDemo" + File.separator + "data-b.txt")
		};
		//System.out.println(readFile(fileArr[0]));
		String[] data = new String[2];
		for (int x=0; x<fileArr.length; x++) {
			data[x] = readFile(fileArr[x]);
		}
		StringBuffer sb = new StringBuffer();
		String[] contentA = data[0].split(" ");
		String[] contentB = data[1].split(" ");
		for (int x=0; x < contentA.length; x++) {
			sb.append(contentA[x]).append("(").append(contentB[x]).append(")").append(" ");
		}
		System.out.println(sb);
	}
	
	public static String readFile(File file) throws IOException {
		if (file.exists()) {
			InputStream input = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] data = new byte[10];
			int temp = 0;
			while((temp = input.read(data)) != -1) {//input把数据从文件中读出来
				bos.write(data, 0, temp);//bos这个内存流,把读出来的文件放到内存中,注意bos并没有把读出来的数据往一个文件里面写,现在数据都在内存中呢
			}
			bos.close();
			input.close();
			return new String(bos.toByteArray());//将读出来的数据变换成一个数组
		}
		return null;
	}
}
