package com.yale.test.xml.jackson;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

//https://www.liaoxuefeng.com/wiki/1252599548343744/1320418596093986#0
//Jackson 可不止 JSON
//Jackson 可能有些人认为它只是个 JSON 库，其实不止。它除了支持 JSON 序列化以外，还支持 XML/YAML/Properties/CSV 等等，而且 Jackson 是将基础功能进行了抽象，每一种序列化都是扩展的形式存在；所以上面的增量输出 SequenceWriter，也是支持其他格式的！
//https://juejin.cn/post/6961029395825819661?share_token=8aefa481-1a07-4499-a721-c62e41d92bf7
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
