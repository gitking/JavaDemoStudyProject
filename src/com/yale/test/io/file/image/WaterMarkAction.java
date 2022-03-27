package com.yale.test.io.file.image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 慕课网 Java实现图片水印 使用的Struts2
 * https://www.imooc.com/learn/482
 * @author issuser
 */
public class WaterMarkAction {
	public String uploadImage () {
		try {
			InputStream in = new FileInputStream(new File(""));
			OutputStream os = new FileOutputStream(new File(""));
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = in.read(buffer)) > 0) {
				os.write(buffer);
			}
			in.close();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}
