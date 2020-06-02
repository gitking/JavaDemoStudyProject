package com.yale.test.io.file.jar;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import sun.nio.cs.ArrayDecoder;

public class JarFileDemo {
	public static void main(String[] args) throws IOException {
		
		/**
		 * 压缩包里面尽量不要有中文,如果必须有中文,中文的文件必须是UTF-8编码的,否则不支持会报MALFORMED错误
		 * Caused By: java.lang.IllegalArgumentException: MALFORMED
         * at java.util.zip.ZipCoder.toString(ZipCoder.java:58)
         * ZipCoder类里面的toString方法明确说明了只支持UTF-8编码
		 */
		JarFile jf = new JarFile(new File("D:\\jre1.8.0_251\\lib\\resources.jar"));
		 Enumeration localEnumeration = jf.entries();
	      while (localEnumeration.hasMoreElements()) {
	        JarEntry localJarEntry = (JarEntry)localEnumeration.nextElement();
	        String str1 = localJarEntry.getName();
	        System.out.println(str1);
	      }
	      
	       /**
			 * 压缩包里面尽量不要有中文,如果必须有中文,中文的文件必须是UTF-8编码的,否则不支持会报MALFORMED错误
			 * Caused By: java.lang.IllegalArgumentException: MALFORMED
	         * at java.util.zip.ZipCoder.toString(ZipCoder.java:58)
	         * ZipCoder类里面的toString方法明确说明了只支持UTF-8编码
	         * 不支持的中文主要是sun.nio.cs.ArrayDecoder这类不支持中文
	         * ArrayDecoder ad = new ArrayDecoder();
			 */
	      ZipFile zfUtf8 = new ZipFile("D:\\浙商SVN\\03-开发\\修改说明UTF-8.zip");
	      Enumeration<ZipEntry> zipEntryUtf8 = (Enumeration<ZipEntry> )zfUtf8.entries();
	      while (zipEntryUtf8.hasMoreElements()) {
	    	  ZipEntry temp = zipEntryUtf8.nextElement();
	    	  System.out.println(temp.getComment());
	    	  System.out.println(temp.getName());
	      }
	      
	      ZipFile zf = new ZipFile("D:\\浙商SVN\\03-开发\\新建文本文档.zip");
	      Enumeration<ZipEntry> zipEntry = (Enumeration<ZipEntry> )zf.entries();
	      while (zipEntry.hasMoreElements()) {
	    	  ZipEntry temp = zipEntry.nextElement();
	    	  System.out.println(temp.getComment());
	    	  System.out.println(temp.getName());
	      }
	}
}
