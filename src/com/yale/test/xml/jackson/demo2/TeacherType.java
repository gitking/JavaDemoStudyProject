package com.yale.test.xml.jackson.demo2;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

public class TeacherType {
	@JacksonXmlProperty(isAttribute=true, localName="type")
	private String type;
	
	@JacksonXmlText
	private String value;
	
	
	public TeacherType() {
		super();
	}
	
	public TeacherType(String type, String value) {
		super();
		this.type = type;
		this.value = value;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
