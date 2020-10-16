package com.yale.test.xml.jackson;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

public class BookAttr {
	@JacksonXmlProperty(isAttribute=true, localName="lang")
	public String lang;
	
	@JacksonXmlText
	public String value;
}
