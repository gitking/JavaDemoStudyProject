package com.yale.test.web.servlet.lxf.bean;

public class User {
	public long id;
	public String name;
	public School school;
	
	public User(long id, String name, School school) {
		this.id = id;
		this.name = name;
		this.school = school;
	}
}
