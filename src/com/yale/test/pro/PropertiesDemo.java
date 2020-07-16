package com.yale.test.pro;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class PropertiesDemo {

	public static void main(String[] args) {
		/**
		 * 实际上通过ResourceBundle类读取的时候只能够读取内容,而如果想要编辑内容,那么就需要通过Properties类来完成了,
		 * Properties这个类是专门做属性处理的
		 * Properties是Hashtable的子类
		 * public class Properties extends Hashtable<Object,Object>
		 * 注意其实所有的属性信息实际上都是以字符串的形式出现的
		 * 配置文件的特点是，它的Key-Value一般都是String-String类型的，因此我们完全可以用Map<String, String>来表示它。
		 * 用Properties读取配置文件非常简单。Java默认配置文件以.properties为扩展名，
		 * 每行以key=value表示，以#课开头的是注释。以下是一个典型的配置文件：
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
		
		/*
		 * 也可以从classpath读取.properties文件，因为load(InputStream)方法接收一个InputStream实例，表示一个字节流，它不一定是文件流，也可以是从jar包中读取的资源流
		 */
		Properties props = new Properties();
		try {
			props.load(props.getClass().getResourceAsStream("/common/setting.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		String settings = "# test" + "\n" + "course=Java" + "\n" + "last_open_date=2019-08-07T12:35:01";
        ByteArrayInputStream input;
		try {
			input = new ByteArrayInputStream(settings.getBytes("UTF-8"));
			Properties propsByte = new Properties();
	        propsByte.load(input);

	        System.out.println("course: " + propsByte.getProperty("course"));
	        System.out.println("last_open_date: " + propsByte.getProperty("last_open_date"));
	        System.out.println("last_open_file: " + propsByte.getProperty("last_open_file"));
	        System.out.println("auto_save: " + propsByte.getProperty("auto_save", "60"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//如果有多个.properties文件，可以反复调用load()读取，后读取的key-value会覆盖已读取的key-value：
		Properties propsLo = new Properties();
		try {
			/*
			 * Properties设计的目的是存储String类型的key－value，但Properties实际上是从Hashtable派生的，它的设计实际上是有问题的，但是为了保持兼容性，现在已经没法修改了。
			 * 除了getProperty()和setProperty()方法外，还有从Hashtable继承下来的get()和put()方法，这些方法的参数签名是Object，我们在使用Properties的时候，
			 * 不要去调用这些从Hashtable继承下来的方法。
			 */
			propsLo.load(propsLo.getClass().getResourceAsStream("/common/setting.properties"));
			propsLo.load(new FileInputStream("C:\\conf\\setting.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/*
		 * 编码
		 * 早期版本的Java规定.properties文件编码是ASCII编码（ISO8859-1），如果涉及到中文就必须用name=\u4e2d\u6587来表示，非常别扭。从JDK9开始，Java的.properties文件可以使用UTF-8编码了。
		 * 不过，需要注意的是，由于load(InputStream)默认总是以ASCII编码读取字节流，所以会导致读到乱码。我们需要用另一个重载方法load(Reader)读取：
		 * 就可以正常读取中文。InputStream和Reader的区别是一个是字节流，一个是字符流。字符流在内存中已经以char类型表示了，不涉及编码问题。
		 */
		Properties propss = new Properties();
		//propss.load(new FileReader("settings.properties", StandardCharsets.UTF_8));
	}
}
