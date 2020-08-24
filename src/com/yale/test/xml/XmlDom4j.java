package com.yale.test.xml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

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
	public static void main(String[] args) throws DocumentException, IOException {
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
	    add();
	    addPosition();
	    update();
	    delete();
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
	
	public static void add() throws DocumentException, IOException{
		SAXReader saxReader = new SAXReader();
		InputStream inputStream = XmlDom4j.class.getClassLoader().getResourceAsStream("dom4j.xml");
		Document document = saxReader.read(inputStream);
		
		//创建出新的节点,为节点设置文本内容
		Element newElement = DocumentHelper.createElement("addName");
		newElement.setText("通过DOM4j用代码添加的街道");
		
		Element root = document.getRootElement();
		root.add(newElement);//把心创建的节点挂在root节点下面
		
		//创建带有格式的对象
		OutputFormat outputFormat = OutputFormat.createPrettyPrint();
		outputFormat.setEncoding("UTF-8");
		
		XMLWriter xmlWriter = new XMLWriter(new FileWriter("dom4jAdd.xml"), outputFormat);
		xmlWriter.write(document);
		xmlWriter.close();
	}
	//在指定位置添加节点
	public static void addPosition() throws DocumentException, IOException{
		SAXReader saxReader = new SAXReader();
		InputStream inputStream = XmlDom4j.class.getClassLoader().getResourceAsStream("dom4j.xml");
		Document document = saxReader.read(inputStream);
		
		//创建出新的节点,为节点设置文本内容
		Element newElement = DocumentHelper.createElement("addName");
		newElement.setText("通过DOM4j用代码添加的街道");
		
		List<Element> elements = document.getRootElement().elements();
		elements.add(1, newElement);//将节点添加到指定位置上
		
		//创建带有格式的对象
		OutputFormat outputFormat = OutputFormat.createPrettyPrint();
		outputFormat.setEncoding("UTF-8");
		
		XMLWriter xmlWriter = new XMLWriter(new FileWriter("dom4jAddSec.xml"), outputFormat);
		xmlWriter.write(document);
		xmlWriter.close();
	}
	
	public static void update() throws DocumentException, IOException{
		SAXReader saxReader = new SAXReader();
		InputStream inputStream = XmlDom4j.class.getClassLoader().getResourceAsStream("dom4j.xml");
		Document document = saxReader.read(inputStream);
		
		Element ageEle = document.getRootElement().element("age");
		ageEle.setText("9999");
		
		//创建带有格式的对象
		OutputFormat outputFormat = OutputFormat.createPrettyPrint();
		outputFormat.setEncoding("UTF-8");
		
		XMLWriter xmlWriter = new XMLWriter(new FileWriter("dom4jAddThri.xml"), outputFormat);
		xmlWriter.write(document);
		xmlWriter.close();
	}
	
	
	public static void delete() throws DocumentException, IOException{
		SAXReader saxReader = new SAXReader();
		InputStream inputStream = XmlDom4j.class.getClassLoader().getResourceAsStream("dom4j.xml");
		Document document = saxReader.read(inputStream);
		
		Element ageEle = document.getRootElement().element("age");
		ageEle.getParent().remove(ageEle);
		
		//创建带有格式的对象
		OutputFormat outputFormat = OutputFormat.createPrettyPrint();
		outputFormat.setEncoding("UTF-8");
		
		XMLWriter xmlWriter = new XMLWriter(new FileWriter("dom4jAddFour.xml"), outputFormat);
		xmlWriter.write(document);
		xmlWriter.close();
	}
}
