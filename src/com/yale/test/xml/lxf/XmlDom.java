package com.yale.test.xml.lxf;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/*
 * XML与JSON
 * XML和JSON是两种经常在网络使用的数据表示格式，本章我们介绍如何使用Java读写XML和JSON。
 * XML简介
 * XML是可扩展标记语言（eXtensible Markup Language）的缩写，它是是一种数据表示格式，可以描述非常复杂的数据结构，常用于传输和存储数据。
 * 例如，一个描述书籍的XML文档可能如下：
 * <?xml version="1.0" encoding="UTF-8" ?>
	<!DOCTYPE note SYSTEM "book.dtd">
	<book id="1">
	    <name>Java核心技术</name>
	    <author>Cay S. Horstmann</author>
	    <isbn lang="CN">1234567</isbn>
	    <tags>
	        <tag>Java</tag>
	        <tag>Network</tag>
	    </tags>
	    <pubDate/>
	</book>
 * XML有几个特点：一是纯文本，默认使用UTF-8编码，二是可嵌套，适合表示结构化数据。如果把XML内容存为文件，那么它就是一个XML文件，例如book.xml。此外，XML内容经常通过网络作为消息传输。
 * XML的结构
 * XML有固定的结构，首行必定是<?xml version="1.0"?>，可以加上可选的编码。紧接着，如果以类似<!DOCTYPE note SYSTEM "book.dtd">声明的是文档定义类型（DTD：Document Type Definition），DTD是可选的。
 * 接下来是XML的文档内容，一个XML文档有且仅有一个根元素，根元素可以包含任意个子元素，元素可以包含属性，例如，<isbn lang="CN">1234567</isbn>包含一个属性lang="CN"，且元素必须正确嵌套。如果是空元素，可以用<tag/>表示。
 * 由于使用了<、>以及引号等标识符，如果内容出现了特殊符号，需要使用&???;表示转义。例如，Java<tm>必须写成：
 * <name>Java&lt;tm&gt;</name>
 * 常见的特殊字符如下：
 * 	字符	表示
	<	&lt;
	>	&gt;
	&	&amp;
	"	&quot;
	'	&apos;
 * 格式正确的XML（Well Formed）是指XML的格式是正确的，可以被解析器正常读取。而合法的XML是指，不但XML格式正确，而且它的数据结构可以被DTD或者XSD验证。
 * DTD文档可以指定一系列规则，例如：
 *  根元素必须是book
    book元素必须包含name，author等指定元素
    isbn元素必须包含属性lang
    ...
 * 如何验证XML文件的正确性呢？最简单的方式是通过浏览器验证。可以直接把XML文件拖拽到浏览器窗口，如果格式错误，浏览器会报错。
 * 和结构类似的HTML不同，浏览器对HTML有一定的“容错性”，缺少关闭标签也可以被解析，但XML要求严格的格式，任何没有正确嵌套的标签都会导致错误。
 * XML是一个技术体系，除了我们经常用到的XML文档本身外，XML还支持：
    DTD和XSD：验证XML结构和数据是否有效；
    Namespace：XML节点和属性的名字空间；
    XSLT：把XML转化为另一种文本；
    XPath：一种XML节点查询语言；
    ...
 * 实际上，XML的这些相关技术实现起来非常复杂，在实际应用中很少用到，通常了解一下就可以了。
 * 小结
 * XML使用嵌套结构的数据表示方式，支持格式验证；
 * XML常用于配置文件、网络消息传输等。
 * 使用DOM
 * 因为XML是一种树形结构的文档，它有两种标准的解析API：
 * DOM：一次性读取XML，并在内存中表示为树形结构；
 * SAX：以流的形式读取XML，使用事件回调。
 * 我们先来看如何使用DOM来读取XML。
 * DOM是Document Object Model的缩写，DOM模型就是把XML结构作为一个树形结构处理，从根节点开始，每个节点都可以包含任意个子节点。
 * 我们以下面的XML为例：
 * <?xml version="1.0" encoding="UTF-8" ?>
	<book id="1">
	    <name>Java核心技术</name>
	    <author>Cay S. Horstmann</author>
	    <isbn lang="CN">1234567</isbn>
	    <tags>
	        <tag>Java</tag>
	        <tag>Network</tag>
	    </tags>
	    <pubDate/>
	</book>
 * 如果解析为DOM结构，它大概长这样：
 *                        ┌─────────┐
	                      │document │
	                      └─────────┘
	                           │
	                           ▼
	                      ┌─────────┐
	                      │  book   │
	                      └─────────┘
	                           │
	     ┌──────────┬──────────┼──────────┬──────────┐
	     ▼          ▼          ▼          ▼          ▼
	┌─────────┐┌─────────┐┌─────────┐┌─────────┐┌─────────┐
	│  name   ││ author  ││  isbn   ││  tags   ││ pubDate │
	└─────────┘└─────────┘└─────────┘└─────────┘└─────────┘
	                                      │
	                                 ┌────┴────┐
	                                 ▼         ▼
	                             ┌───────┐ ┌───────┐
	                             │  tag  │ │  tag  │
	                             └───────┘ └───────┘
 * 注意到最顶层的document代表XML文档，它是真正的“根”，而<book>虽然是根元素，但它是document的一个子节点。
 * Java提供了DOM API来解析XML，它使用下面的对象来表示XML的内容：
 * Document：代表整个XML文档；Element：代表一个XML元素；Attribute：代表一个元素的某个属性。
 * 使用DOM API解析一个XML文档的代码如下：
 * InputStream input = Main.class.getResourceAsStream("/book.xml");
 *	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
 *	DocumentBuilder db = dbf.newDocumentBuilder();
 *	Document doc = db.parse(input);
 * DocumentBuilder.parse()用于解析一个XML，它可以接收InputStream，File或者URL，如果解析无误，我们将获得一个Document对象，这个对象代表了整个XML文档的树形结构，需要遍历以便读取指定元素的值：
 * 对于DOM API解析出来的结构，我们从根节点Document出发，可以遍历所有子节点，获取所有元素、属性、文本数据，还可以包括注释，这些节点被统称为Node，每个Node都有自己的Type，根据Type来区分一个Node到底是元素，还是属性，还是文本，等等。
 * 使用DOM API时，如果要读取某个元素的文本，需要访问它的Text类型的子节点，所以使用起来还是比较繁琐的。
 * 小结
 * Java提供的DOM API可以将XML解析为DOM结构，以Document对象表示；
 * DOM可在内存中完整表示XML数据结构；
 * DOM解析速度慢，内存占用大。
 */
public class XmlDom {
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		InputStream input = XmlDom.class.getResourceAsStream("/book.xml");
		//解析并获取Document对象:
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(input);
		printNode(doc, 0);
	}
	
	static void printNode(Node n, int indent) {
		for (int i=0; i< indent; i++) {
			System.out.println(' ');
		}
		switch(n.getNodeType()) {
		case Node.DOCUMENT_NODE:
			System.out.println("Document: " + n.getNodeName());
			break;
		case Node.ELEMENT_NODE:
			System.out.println("Element: " + n.getNodeName());
			break;
		case Node.TEXT_NODE:
			System.out.println("Text: " + n.getNodeName() + " = " + n.getNodeValue());
			break;
		case Node.ATTRIBUTE_NODE:
			System.out.println("Attr: " + n.getNodeName() + " = " + n.getNodeValue());
			break;
		case Node.CDATA_SECTION_NODE:
			System.out.println("CDATA: " + n.getNodeName() + " = " + n.getNodeValue());
			break;
		case Node.COMMENT_NODE:
			System.out.println("Comment: " + n.getNodeName() + " = " + n.getNodeValue());
			break;
		default:
			System.out.println("NodeType: " + n.getNodeType() + " , NodeName: " + n.getNodeName());
		}
		/*
		 * 问:为什么没有解析出来isbn的属性lang？
		 * 答:用elementNode的getAttributes()取
		 */
		if (n.hasAttributes()) {
			NamedNodeMap as = n.getAttributes();//属性
			for (int i = 0; i < as.getLength(); i++) {
				printNode(as.item(i), indent + 1);
			}
		}
		
		for (Node child = n.getFirstChild(); child != null; child = child.getNextSibling()) {
			printNode(child, indent +1);
		}
	}
}
