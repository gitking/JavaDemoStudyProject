package com.yale.test.xml;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

/**
 * SAX采用的是一种顺序的模式进行访问，是一种快速读取XML数据的方式。当时候SAX解析器进行操作时，会触发一系列事件SAX。
 * 采用事件处理的方式解析XML文件，利用 SAX 解析 XML 文档，涉及两个部分：解析器和事件处理器
 * sax是一种推式的机制,你创建一个sax 解析器,解析器在发现xml文档中的内容时就告诉你(把事件推给你). 如何处理这些内容，由程序员自己决定。
 * 当解析器解析到<?xml version="1.0" encoding="UTF-8" standalone="no"?>声明头时，会触发事件。解析到<china>元素头时也会触发事件！
 * 也就是说：当使用SAX解析器扫描XML文档(也就是Document对象)开始、结束，以及元素的开始、结束时都会触发事件，根据不同事件调用相对应的方法!
 * SAX解析采用部分读取的方式，可以处理大型文件，但只能对文件按顺序从头到尾解析一遍，不支持文件的增删改操作
 * https://mp.weixin.qq.com/s/A54bBHUZ_NCmneEzMXzr1Q
 * DOM和SAX解析的区别：
 * DOM解析读取整个XML文档，在内存中形成DOM树，很方便地对XML文档的内容进行增删改。但如果XML文档的内容过大，那么就会导致内存溢出！
 * SAX解析采用部分读取的方式，可以处理大型文件，但只能对文件按顺序从头到尾解析一遍，不支持文件的增删改操作
 * DOM和SAX解析有着明显的差别，什么时候使用DOM或者SAX就非常明了了。
 * @author dell
 */
public class XmlSax {

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		SAXParserFactory sf = SAXParserFactory.newInstance();
		SAXParser saxParse = sf.newSAXParser();
		InputStream is = XmlSax.class.getClassLoader().getResourceAsStream("city.xml");
		saxParse.parse(is, new MyHandler());
	}
}
