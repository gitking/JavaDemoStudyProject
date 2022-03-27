package com.yale.test.io.apache;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.io.FileUtils;

/**
 * commons-io 文件流处理
 * <dependency>  
    <groupId>commons-io</groupId>  
    <artifactId>commons-io</artifactId>  
    <version>2.8.0</version>  
	</dependency>  
 */
public class CommonsIO {
	public static void main(String[] args) throws IOException {
		File file = new File("demo1.txt");
		List<String> lines = FileUtils.readLines(file, Charset.defaultCharset());//读取文件
		
		FileUtils.writeLines(new File("demo2.txt"), lines);
		
		//复制文件
		File srcFile = new File("");
		File destFile = new File("");
		FileUtils.copyFile(srcFile, destFile);
	}
}
