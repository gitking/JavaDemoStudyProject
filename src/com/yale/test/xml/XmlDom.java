package com.yale.test.xml;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * DOM 和 和 SAX 解析器有什么区别
 * DOM解析读取整个XML文档，在内存中形成DOM树，很方便地对XML文档的内容进行增删改。但如果XML文档的内容过大，那么就会导致内存溢出！
 * SAX解析采用部分读取的方式，可以处理大型文件，但只能对文件按顺序从头到尾解析一遍，不支持文件的增删改操作
 * 1,DOM是基于内存的，不管文件有多大，都会将所有的内容预先装载到内存中。从而消耗很大的内存空间。而SAX是基于事件的。当某个事件被触发时，才获取相应的XML的部分数据，从而不管XML文件有多大，都只占用了少量的内存空间。
 * 2,DOM可以读取XML也可以向XML文件中插入数据，而SAX却只能对XML进行读取，而不能在文件中插入数据。这也是SAX的一个缺点。
 * 3,SAX的另一个缺点：DOM我们可以指定要访问的元素进行随机访问，而SAX则不行。SAX是从文档开始执行遍历的。并且只能遍历一次。也就是说我们不能随机的访问XML文件，只能从头到尾的将XML文件遍历一次（当然也可以中间截断遍历）。
 * 微信公众号:https://mp.weixin.qq.com/s/KvmcacWSknGV1Ay35Nh52g
 * https://mp.weixin.qq.com/s/A54bBHUZ_NCmneEzMXzr1Q
 * xml可以描述复制的数据关系:比如, 中国下面有北京和上海这种联动下拉框
 * 我们没有XML这种语言之前，我们使用的是String作为两个程序之间的通讯！现在问题就来了，如果我们传输的是带有关系型结构的数据，String怎么表达呢？String对关系型数据不擅长，要是描述起来也难免会有歧义的时候！关系型数据
 * XML的用途:
 * 配置文件(例子：Tomcat的web.xml,server.xml……)，XML能够非常清晰描述出程序之间的关系
 * 充当小型数据库，如果我们的数据有时候需要人工配置的，那么XML充当小型的数据库是个不错的选择，程序直接读取XML文件显然要比读取数据库要快呢！
 * 在XML中元素和标签指的是同一个东西！不要被不同的名称所迷惑了！
 * 元素中需要值得注意的地方：
 * XML元素中的出现的空格和换行都会被当做元素内容进行处理
 * 每个XML文档必须有且只有一个根元素
 * 元素必须闭合
 * 大小写敏感
 * 不能交叉嵌套
 * 不能以数字开头
 * JDK中的XML API
 * ①：JAXP（The Java API For XML Processing)：主要负责解析XML
 * ②：JAXB（Java Architecture for XML Binding):主要负责将XML映射为Java对象
 * XML解析方式分为两种：
 * ①：dom(Document Object Model)文档对象模型，是W3C组织推荐解析XML的一种方式
 * ②：sax(Simple API For XML)，它是XML社区的标准，几乎所有XML解析器都支持它！
 * XML的解析引擎
 * jaxp->Crimson JDom->Xerces dom4j->Aelfred2
 * XML3种开发包
 * jaxp开发包是JDK自带的，不需要导入开发包。
 * 由于sun公司的jaxp不够完善，于是就被研发了Jdom。XML解析如果使用Jdom，需要导入开发包
 * dom4j是由于Jdom的开发人员出现了分歧，dom4j由Jdom的一批开发人员所研发。XML解析如果使用Jdom，需要导入开发包【现在用dom4j是最多的！】
 * @author dell
 */
public class XmlDom {
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, TransformerException {
		//API规范：需要用一个工厂来造解析器对象，于是我先造了一个工厂！
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		 //获取解析器对象
		DocumentBuilder db = dbf.newDocumentBuilder();
		
		//获取到解析XML文档的流对象,src目录下的city.xml
		InputStream inputStream = XmlDom.class.getClassLoader().getResourceAsStream("city.xml");
		
		//解析XML文件,得到代表xml文件的Document文档对象
		Document document = db.parse(inputStream);
		list(document);
		read(document);
		add(document);
		addOrderBy(document);
		delete(document);
		update(document);
		updateAttribute(document);
	}
	
	/**
	 * 通过一个文件来创建document
	 * 参考项目里面的DOMTool.java
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	private static void loadDocumentFromFile() throws SAXException, IOException, ParserConfigurationException {
		InputStream is = new FileInputStream(new File(""));
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
	}
	
	/**
	 * 从一个字符串来创建document.
	 * 参考项目里面的DOMTool.java
	 * @param str
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	private static void loadDocumentFromStr(String str) throws SAXException, IOException, ParserConfigurationException {
		 InputStream is = new ByteArrayInputStream(str.getBytes());
		 Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(is));
	}
	
	/**
	 * 直接创建一个Document
	 * 参考项目里面的DOMTool.java
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	private static void createNewDocument() throws SAXException, IOException, ParserConfigurationException {
		Document document =	DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
	}
	
	private static void loadDocumentFromUri(String uri) throws SAXException, IOException, ParserConfigurationException {
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(uri);
	}
	
	private static void list(Node node) {
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			System.out.println("Xml元素节点为:" + node.getNodeName());
		}
		
		NodeList nodeList = node.getChildNodes();
		
		//遍历子节点集合
		for (int i=0; i<nodeList.getLength(); i++) {
			Node child = nodeList.item(i);//获取一个子节点
			list(child);
		}
	}
	
	/**
	 * 读取节点
	 * @param document
	 */
	private static void read(Document document) {
		NodeList nodeList = document.getElementsByTagName("guangzhou");
		Node node = nodeList.item(0);
		String value = node.getTextContent();
		System.out.println("guangzhou的文本内容为:" + value);
	}
	
	/**
	 * 添加节点
	 * @param document
	 * @throws TransformerException 
	 */
	private static void add(Document document) throws TransformerException {
		Element ele = document.createElement("hangzhou");
		ele.setTextContent("杭州");
		Node parent = document.getElementsByTagName("china").item(0);
		parent.appendChild(ele);//在内存中节点已经添加好了,还没写到真正的文件里面去呢
		
		//要想把内存中的Dom树写到硬盘文件中，需要转换器！获取转换器也十分简单
		TransformerFactory tff = TransformerFactory.newInstance();
		Transformer tf = tff.newTransformer();
		tf.transform(new DOMSource(document), new StreamResult("city.xml"));
	}
	
	
	private static void addOrderBy(Document document) throws TransformerException {
		Element ele = document.createElement("guangxi");
		ele.setTextContent("广西");
		
		Node parent = document.getElementsByTagName("china").item(0);
		Node beijing = document.getElementsByTagName("beijing").item(0);
		//将广西节点插入到北京节点之前
		parent.insertBefore(ele, beijing);//在内存中节点已经添加好了,还没写到真正的文件里面去呢
		
		//要想把内存中的Dom树写到硬盘文件中，需要转换器！获取转换器也十分简单
		
		TransformerFactory tff = TransformerFactory.newInstance();
		Transformer tf = tff.newTransformer();
		tf.transform(new DOMSource(document), new StreamResult("city.xml"));
	}
	
	private static void delete(Document document) throws TransformerException {
		Node node = document.getElementsByTagName("beijing").item(0);
		node.getParentNode().removeChild(node);
		
		//要想把内存中的Dom树写到硬盘文件中，需要转换器！获取转换器也十分简单
		TransformerFactory tff = TransformerFactory.newInstance();
		Transformer tf = tff.newTransformer();
		tf.transform(new DOMSource(document), new StreamResult("city.xml"));
	}
	
	private static void update(Document document) throws TransformerException {
		Node node = document.getElementsByTagName("guangzhou").item(0);
		node.setTextContent("广州你好");
		
		//要想把内存中的Dom树写到硬盘文件中，需要转换器！获取转换器也十分简单
		TransformerFactory tff = TransformerFactory.newInstance();
		Transformer tf = tff.newTransformer();
		tf.transform(new DOMSource(document), new StreamResult("city.xml"));
	}
	
	/**
	 * 更新节点的属性
	 */
	private static void updateAttribute(Document document) throws TransformerException {
		Node node = document.getElementsByTagName("guangzhou").item(0);	
		Element guangZhou = (Element)node;
		guangZhou.setAttribute("play", "gzchanglong");
		
		//要想把内存中的Dom树写到硬盘文件中，需要转换器！获取转换器也十分简单
		TransformerFactory tff = TransformerFactory.newInstance();
		Transformer tf = tff.newTransformer();
		tf.transform(new DOMSource(document), new StreamResult("city.xml"));
	}
}
