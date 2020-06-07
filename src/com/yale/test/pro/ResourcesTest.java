package com.yale.test.pro;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
/**
 * 如何将文字和程序进行分离呢,这个时候就需要有一个属性文件的支持了.所谓的属性文件也会被称为资源文件(*.properties作为后缀),
 * 通过不同的资源文件来进行读取
 * 
 */
public class ResourcesTest {
	public static void main(String[] args) {
		
		System.out.println("当前的语言环境" + Locale.getDefault());
		System.out.println("Locale中的常量中国" + Locale.CHINA);
		System.out.println("Locale中的常量" + Locale.CHINESE);
		System.out.println("Locale中的常量美国" + Locale.US);
		System.out.println("Locale类还可以通过构造方法设置");
		Locale locale = new Locale("zh", "CN");
		System.out.println("获取通过构造方法设置的城市:" + locale.getCountry());


		//这个时候设置的参数路径一定不要包含.properties后缀,而且一定要在CLASSPATH之中,并且要带完整的包路径
		ResourceBundle rs = ResourceBundle.getBundle("com.yale.test.pro.Message");
		System.out.println(rs.getString("weblcome"));
		/**
		 * 可以使用JDK自带的native2ascii.exe工具,来对中文进行转码
		 */
		System.out.println("native2ascii.exe编辑properties使用帮助:" + rs.getString("useguid"));
		System.out.println("*********************************************");
		
		System.out.println("但是native2ascii.exe这个工具也太麻烦了,建议安装eclipse的properties插件");
		System.out.println("插件下载地址:http://propedit.sourceforge.jp/eclipse/updates");
		System.out.println("安装方法:eclipse-help-Install New Software..");
		System.out.println("安装成功后右键选中properties文件,用PropertiesEditor模式打开文件就可以输入中文了");

		
		Locale local = Locale.getDefault();
		//这个时候设置的参数路径一定不要包含.properties后缀,而且一定要在CLASSPATH之中
		//getBundle这个方法参数后面如果输入了local,默认读取的就是对应的properties文件,那个Message.properties文件就没用了
		ResourceBundle resourceBundle = ResourceBundle.getBundle("com.yale.test.pro.Message",local);
		String str = resourceBundle.getString("weblcome");
		System.out.println(MessageFormat.format(str, "张三","李四","王五"));
		
		System.out.println("###############获取英文的国际化文件开始了################");
				
		Locale localEng = new Locale("en","US");//读取Message_en_US.properties文件
		//这个时候设置的参数路径一定不要包含.properties后缀,而且一定要在CLASSPATH之中
		ResourceBundle resourceBundleEn = ResourceBundle.getBundle("com.yale.test.pro.Message",localEng);
		String strEn = resourceBundleEn.getString("weblcome");
		System.out.println(MessageFormat.format(strEn, "张三","李四","王五"));
		//如果nohave这个key值不在properties文件里面,代码会报错,java.util.MissingResourceException:
		try {
			String nohave = resourceBundleEn.getString("nohave");
		} catch (MissingResourceException e) {
			e.printStackTrace();
		}

	}
}
