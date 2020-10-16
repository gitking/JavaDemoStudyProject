package com.yale.test.xml.jackson.demo1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/*
 * https://www.jianshu.com/p/0c2220d88924
 * gitee: https://gitee.com/xiyu2016/xmlparse.git
 */
public class Test {
	public static void main(String[] args) throws JsonMappingException, JsonProcessingException {
		ObjectMapper objectMapper = new XmlMapper();
	    String xml = "<?xml version=\"1.0\" encoding=\"gb2312\" standalone=\"yes\"?><student><dizhi>北京三里屯</dizhi><studentName>张三</studentName><nianling>25</nianling><dh>18858863333</dh><rs><array><acc01>89.50</acc01><acc02>100.00</acc02><acc03>91.00</acc03></array><array><acc01>78.50</acc01><acc02>69.00</acc02><acc03>88.90</acc03></array></rs></student>";
	  	Student<Score> student = objectMapper.readValue(xml, Student.class);
	  	System.out.println("xml转换为java对象" + student.getAddress());
		String result = objectMapper.writeValueAsString(student);
		System.out.println(result);
	}
}
