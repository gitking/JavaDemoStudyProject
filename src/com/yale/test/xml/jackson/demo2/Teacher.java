package com.yale.test.xml.jackson.demo2;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Teacher {
	@JacksonXmlProperty(localName="TypeCode")
	private TeacherType teacherTypeCode;
	private String name;
	private String gender;
	private String age;
	
	public TeacherType getTeacherTypeCode() {
		return teacherTypeCode;
	}
	public void setTeacherTypeCode(TeacherType teacherTypeCode) {
		this.teacherTypeCode = teacherTypeCode;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
}
