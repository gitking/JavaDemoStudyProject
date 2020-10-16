package com.yale.test.xml.jackson.demo2;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/*
 * 概述
　　Jackson是一个强大工具，可用于Json、XML、实体之间的相互转换。此篇博客主要着重于 实体和XML之间的转换。
	XML转换核心
	　　 要想灵活地进行转换，需要在实体上使用到Jackson提供的四个注解：
	　　 @JacksonXmlElementWrapper：可用于指定List等集合类，外围标签名；
	　　 @JacksonXmlProperty：指定包装标签名，或者指定标签内部属性名；
	　　 @JacksonXmlRootElement：指定生成xml根标签的名字；
	　　 @JacksonXmlText：指定当前这个值，没有xml标签包裹。
	实例展示
	XML结构：一个班级—>一个老师(卡卡西)—>三个学生(小樱，佐助，鸣人)
	班级类：Group
	https://blog.csdn.net/u014746965/article/details/78647616
 */
@JacksonXmlRootElement(localName="Class")
public class Group {
	Teacher teacher;//教师
	
	@JacksonXmlElementWrapper(localName="Students")
	@JacksonXmlProperty(localName="Stu")
	List<Student> student;

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public List<Student> getStudent() {
		return student;
	}

	public void setStudent(List<Student> student) {
		this.student = student;
	}
}
