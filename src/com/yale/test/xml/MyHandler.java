package com.yale.test.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class MyHandler extends DefaultHandler {
	
	boolean flag = false;
	@Override
	public void startDocument() throws SAXException {
		System.out.println("我开始来扫描来了");
	}
	
	@Override
	public void endDocument() throws SAXException {
		System.out.println("我结束了");
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		//输出节点的名字！
        if ("guangzhou".equals(qName)) {
        	System.out.println("开始扫描节点:" + qName);
        	flag = true;
        }
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
        if ("guangzhou".equals(qName) && flag) {
        	//输出节点的名字！
            System.out.println("结束扫描节点:" + qName);
            flag = false;
        }
	}
	
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (flag) {
			System.out.println(new String(ch, start, length));
		}
	}
}
