package com.yale.test.mybatis.vo;

public class Teacher {
	private String id;//数据类型要跟表的数据类型保持一致
	private String name;

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
}
