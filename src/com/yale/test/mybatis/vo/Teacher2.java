package com.yale.test.mybatis.vo;

import java.util.List;

public class Teacher2 {
	private String id;//数据类型要跟表的数据类型保持一致
	private String name;
	private List<Student2> stuList;//一对多
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Student2> getStuList() {
		return stuList;
	}
	public void setStuList(List<Student2> stuList) {
		this.stuList = stuList;
	}
}
