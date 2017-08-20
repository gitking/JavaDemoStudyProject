package com.yale.test.pro;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class ResourcesTest {
	public static void main(String[] args) {
		//这个时候设置的参数路径一定不要包含.properties后缀,而且一定要在CLASSPATH之中
		ResourceBundle rs = ResourceBundle.getBundle("com.yale.test.pro.Message");
		System.out.println(rs.getString("weblcome"));
		/**
		 * 可以使用JDK自带的native2ascii.exe工具,来对中文进行转码
		 */
		System.out.println("native2ascii.exe编辑properties使用帮助:" + rs.getString("useguid"));
		System.out.println("*********************************************");
		
		Locale local = Locale.getDefault();
		//这个时候设置的参数路径一定不要包含.properties后缀,而且一定要在CLASSPATH之中
		ResourceBundle resourceBundle = ResourceBundle.getBundle("com.yale.test.pro.Message",local);
		String str = resourceBundle.getString("weblcome");
		System.out.println(MessageFormat.format(str, "张三","李四","王五"));
		
		System.out.println("###############获取英文的国际化文件开始了################");
				
		Locale localEng = new Locale("en","US");
		//这个时候设置的参数路径一定不要包含.properties后缀,而且一定要在CLASSPATH之中
		ResourceBundle resourceBundleEn = ResourceBundle.getBundle("com.yale.test.pro.Message",localEng);
		String strEn = resourceBundleEn.getString("weblcome");
		System.out.println(MessageFormat.format(strEn, "张三","李四","王五"));
	}
}
