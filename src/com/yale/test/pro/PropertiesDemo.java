package com.yale.test.pro;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesDemo {

	public static void main(String[] args) {
		/**
		 * 实际上通过ResourceBundle类读取的时候只能够读取内容,而如果想要编辑内容,那么就需要通过Properties类来完成了,
		 * Properties这个类是专门做属性处理的
		 * Properties是Hashtable的子类
		 * public class Properties extends Hashtable<Object,Object>
		 * 注意其实所有的属性信息实际上都是以字符串的形式出现的
		 */
		Properties pro = new Properties();
		pro.setProperty("bj", "BeiJing");
		pro.setProperty("tj", "Tianin");
		
		//注意是getProperty方法,不是get方法
		System.out.println(pro.getProperty("bj"));
		System.out.println("getProperty取不到就返回null:" + pro.getProperty("nj"));
		System.out.println("get取不到就返回null:" + pro.getProperty("nj", "我是默认值"));
		
		
		try {
			//将pro输出到文件
			pro.store(new FileOutputStream(new File("D:" + File.separator + "area.properties")), "comments是注释的意思");
			System.out.println("将Properties输出到文件");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Properties proLoad = new Properties();
		try {
			proLoad.load(new FileInputStream(new File("D:" + File.separator + "area.properties")));
			System.out.println("从文件中读取properties文件:" + proLoad.getProperty("bj"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/**
		 * public static Connection getConnection(String url, Properties info) throws SQLException
		 * 这里对参数Properties的要求有固定格式,
		 */
		System.out.println("在JDBC中的java.sql.DriverManager类中有一个方法getConnection的参数可以接收Properties");
		
		System.out.println("总结:Properties只能够操作String,它可以进行远程属性内容的加载。ResourceBundle只能加载classpath下的资源文件");
	}
}
