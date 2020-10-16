package com.yale.test.xml.jackson.demo1;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/*
 * Base
 * 这里为了测试集成的属性是否可以被解析，最后的结果是可以的。
 * @JacksonXmlProperty注解是标注该属性在xml中对应的节点名称，这就是为了解决某某系统接口字段命名不规范的利器。
 * https://www.jianshu.com/p/0c2220d88924
 */
public class Base {
	@JacksonXmlProperty(localName="dizhi")
	private String address;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}	
