package com.yale.test.spring.vo;

/**
 * BodyTemplate这个类模仿的是Spring_2.5.6.jar包里面的org.springframework.orm.hibernate3.HibernateTemplate类
 * @author dell
 */
public class BodyTemplate {
	private String name;
	public BodyTemplate(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
