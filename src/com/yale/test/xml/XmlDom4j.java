package com.yale.test.xml;

import java.io.File;
import java.io.InputStream;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * dom4j
 * Dom4j是一个非常优秀的Java XML API，具有性能优异、功能强大和极易使用的特点。
 * 为什么需要有dom4j
 * dom缺点:比较耗费内存
 * sax缺点:只能对xml文件进行读取,不能修改,添加,删除
 * dom4j:既可以提高效率,同时也可以进行crud操作
 * 因为dom4j不是sun公司的产品,所以我们开发dom4j需要导入开发包
 * @author dell
 */
public class XmlDom4j {
	public static void main(String[] args) throws DocumentException {
		//获取到解析器
		SAXReader saxreader = new SAXReader();
		InputStream is = XmlDom4j.class.getClassLoader().getResourceAsStream("city.xml");
		Document document = saxreader.read(is);
		/**
		 * 创建Document的三种方式
		 */
		SAXReader reader = new SAXReader();
	    Document document01 = reader.read(new File("dom4j.xml"));
	    
	    String text = "<members></members>";
	    Document document02 = DocumentHelper.parseText(text);
	    
	    /**
	     * 主动创建document对象.
	     */
	    Document document03 =DocumentHelper.createDocument();
	    //创建根节点
	    Element root = document03.addElement("members");
	    
	    read();
	    read2();
	}
	
	public static void read() throws DocumentException {
		SAXReader saxreadr = new SAXReader();
		InputStream is = XmlDom4j.class.getClassLoader().getResourceAsStream("dom4j.xml");
		Document document = saxreadr.read(is);
		Element root = document.getRootElement();
		Element name = root.element("name");
		String text = name.getText();
		String attribute = name.attributeValue("littleName");
		System.out.println("文本内容是:" + text);
		System.out.println("属性内容是:" + attribute);
	}
	
	public static void read2() throws DocumentException {
		SAXReader saxreadr = new SAXReader();
		InputStream is = XmlDom4j.class.getClassLoader().getResourceAsStream("dom4j2.xml");
		Document document = saxreadr.read(is);
		Element root = document.getRootElement();
		Element name = root.element("guangdong").element("guangzhou").element("luogang");
		String text = name.getText();
		String attribute = name.attributeValue("littleName");
		System.out.println("文本内容是:" + text);
		System.out.println("属性内容是:" + attribute);
	}
}
