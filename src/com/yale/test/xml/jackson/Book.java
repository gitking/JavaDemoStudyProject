package com.yale.test.xml.jackson;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Book {
	public long id;
	public String name;
	public String author;
	@JacksonXmlProperty(localName="isbn")
	public BookAttr isbn;
	
	/**
	 * 要想获取属性lang的值,你得这么想。你要告诉Jackson,lang是哪个xml节点的属性才行,
	 * 所以你需要新建一个类BookAttr
	 */
	//public String lang;

	public List<String> tags;
	public String pubDate;
}
