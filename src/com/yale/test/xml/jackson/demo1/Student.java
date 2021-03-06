package com.yale.test.xml.jackson.demo1;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/*
 * @JacksonXmlRootElement 这个注解是指名根节点，见名知意不用多说。
 * 重点是@JacksonXmlElementWrapper将某节点下单元素解析为Array或者Collection类型，因为除了公共字段外，其他字段都可能是变化的所以在集合哪儿使用了泛型。
 * 《定制 Jackson 解析器来完成对复杂格式 XML 的解析 ｜ Java Debug 笔记》https://juejin.cn/post/6961271701271216141?share_token=b126d332-8bd5-40df-9c7e-45089baca931
 * 《将海量动态数据以 JSON 格式导出 | Java Debug 笔记》https://juejin.cn/post/6961029395825819661?share_token=8aefa481-1a07-4499-a721-c62e41d92bf7
 */
@JacksonXmlRootElement(localName = "student")
public class Student<T> extends Base {
	
	@JacksonXmlProperty(localName="studentName")
	private String name;
	
	@JacksonXmlProperty(localName="nianling")
	private Integer age;
	
	@JacksonXmlProperty(localName="dh")
	private String phone;
	
	@JacksonXmlProperty(localName="array")
	@JacksonXmlElementWrapper(localName="rs")
	private List<T> grades;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public List<T> getGrades() {
		return grades;
	}
	public void setGrades(List<T> grades) {
		this.grades = grades;
	}
	
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}
