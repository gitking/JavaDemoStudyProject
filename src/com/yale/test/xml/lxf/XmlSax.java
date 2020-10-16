package com.yale.test.xml.lxf;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/*
 * 使用SAX
 * 使用DOM解析XML的优点是用起来省事，但它的主要缺点是内存占用太大。
 * 另一种解析XML的方式是SAX。SAX是Simple API for XML的缩写，它是一种基于流的解析方式，边读取XML边解析，并以事件回调的方式让调用者获取数据。因为是一边读一边解析，所以无论XML有多大，占用的内存都很小。
 * SAX解析会触发一系列事件：
 *  startDocument：开始读取XML文档；
    startElement：读取到了一个元素，例如<book>；
    characters：读取到了字符；
    endElement：读取到了一个结束的元素，例如</book>；
    endDocument：读取XML文档结束。
 * 如果我们用SAX API解析XML，Java代码如下：
 * 关键代码SAXParser.parse()除了需要传入一个InputStream外，还需要传入一个回调对象，这个对象要继承自DefaultHandler：
 * 如果要读取<name>节点的文本，我们就必须在解析过程中根据startElement()和endElement()定位当前正在读取的节点，可以使用栈结构保存，每遇到一个startElement()入栈，每遇到一个endElement()出栈，这样，读到characters()时我们才知道当前读取的文本是哪个节点的。可见，使用SAX API仍然比较麻烦。
 * 小结
 * SAX是一种流式解析XML的API；
 * SAX通过事件触发，读取速度快，消耗内存少；
 * 调用方必须通过回调方法获得解析过程中的数据。
 * 问:也解析不出来attribute~
 * 答:注意startElement有个Attribute参数，读这个
 */
public class XmlSax {
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		InputStream input = XmlSax.class.getResourceAsStream("/book.xml");
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser saxParser = spf.newSAXParser();
		saxParser.parse(input,  new MyHandler());
	}
}

class MyHandler extends DefaultHandler {
	
	void print(Object... objs) {
		for (Object obj: objs) {
			System.out.println(obj);
			System.out.println(" ");
		}
		System.out.println();
	}
	
	@Override
	public void startDocument() throws SAXException {
		print("start document");
	}
	
	@Override
	public void endDocument() throws SAXException {
		print("end document");
	}
	
	/* 问:也解析不出来attribute~
	 * 答:注意startElement有个Attribute参数，读这个
	 */
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes)
			throws SAXException {
		print("start element:", localName, qName);
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		print("end element:", localName, qName);
	}
	
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		print("", new String(ch, start, length));
	}
	
	@Override
	public void error(SAXParseException e) throws SAXException {
		print("error:", e);
	}
}