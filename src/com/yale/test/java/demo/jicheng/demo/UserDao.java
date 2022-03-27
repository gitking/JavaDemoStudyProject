package com.yale.test.java.demo.jicheng.demo;

public class UserDao extends DaoSupport{
	public String jdbcTemplate;
	
	public void setValue() {
		//this.setJdbcTemplate("子类设置的值");
		this.jdbcTemplate = "【子类设置的值】";
	}
	
	
	public void setValue1() {
		this.setJdbcTemplate("子类设置的值setValue1");
	}
	public void subPrintValue() {
		System.out.println("【子类】子类的值" + this.jdbcTemplate + ",super父类的值:" + super.jdbcTemplate);
	}
	
	public String subGetJdbcTemplate() {
		return "【子类】:" + this.jdbcTemplate;
	}
}
