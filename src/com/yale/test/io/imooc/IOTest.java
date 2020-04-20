package com.yale.test.io.imooc;

import java.io.File;
import java.io.IOException;

public class IOTest {

	public static void main(String[] args) throws IOException {
		long start = System.currentTimeMillis();
		IOUtil.copyFileByByte(new File("D:\\Bypass_1.13.78.zip"), new File("D:\\JavaDemo\\ByByte.zip"));
		long end = System.currentTimeMillis();
		System.out.println("一个字节一个字节读取花费时间:" + (end - start));
		
		start = System.currentTimeMillis();
		IOUtil.copyFileByBuffer(new File("D:\\Bypass_1.13.78.zip"), new File("D:\\JavaDemo\\ByByteBuffer.zip"));
		end = System.currentTimeMillis();
		System.out.println("带缓冲的字节读取花费时间:" + (end - start));
		
		
		start = System.currentTimeMillis();
		IOUtil.copyFile(new File("D:\\Bypass_1.13.78.zip"), new File("D:\\JavaDemo\\ByByteBatch.zip"));
		end = System.currentTimeMillis();
		System.out.println("批量字节读取花费时间:" + (end - start));
	}

}
