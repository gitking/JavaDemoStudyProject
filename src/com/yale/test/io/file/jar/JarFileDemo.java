package com.yale.test.io.file.jar;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * https://cloud.tencent.com/developer/article/1130020
 * Apache commons-compress 解压 zip 文件是件很幸福的事，可以解决 zip 包中文件名有中文时跨平台的乱码问题，不管文件是在 Windows 压缩的还是在 Mac，Linux 压缩的，解压后都没有再出现乱码问题了。
 * Windows 压缩的时候使用的是系统的编码 GB2312，而 Mac 系统默认的编码是 UTF-8，于是出现了乱码。
 * @author dell
 *
 */
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
	         * ArrayDecoder源码来这里看
	         * https://hg.openjdk.java.net/jdk8u/jdk8u/jdk/file/63cecc0bd71d/src/share/classes/sun/nio/cs/ArrayDecoder.java
	         * sun下面的都不在src.zip里
			 */
	      String str = new String("D:\\浙商SVN\\03-开发\\修改说明UTF-8.zip");
	      System.out.println(str);
	      //StandardCharsets.US_ASCII用这个不会报java.lang.IllegalArgumentException: MALFORMED这个错误,但是会输出的都是乱码的
	      ZipFile zfUtf8 = new ZipFile(new File(str), ZipFile.OPEN_READ, StandardCharsets.US_ASCII);
	      Enumeration<ZipEntry> zipEntryUtf8 = (Enumeration<ZipEntry> )zfUtf8.entries();
	      while (zipEntryUtf8.hasMoreElements()) {
	    	  ZipEntry temp = zipEntryUtf8.nextElement();
	    	  System.out.println(temp.getComment());
	    	  System.out.println(temp.getName());
	      }
	      ZipFile zf = new ZipFile(new String("D:\\浙商SVN\\03-开发\\新建文本文档.zip".getBytes(), Charset.forName("UTF-8")));
	      Enumeration<ZipEntry> zipEntry = (Enumeration<ZipEntry> )zf.entries();
	      while (zipEntry.hasMoreElements()) {
	    	  ZipEntry temp = zipEntry.nextElement();
	    	  System.out.println(temp.getComment());
	    	  System.out.println(temp.getName());
	      }
	}
}
