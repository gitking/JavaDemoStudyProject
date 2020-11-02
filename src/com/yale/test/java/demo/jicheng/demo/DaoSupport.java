package com.yale.test.java.demo.jicheng.demo;

public class DaoSupport {
	public String jdbcTemplate;
	
	public final void setJdbcTemplate(String jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public final String getJdbcTemplate() {
		return this.jdbcTemplate;
	}
	
	public void print() {
		System.out.println("子类调用父类方法:" + this.jdbcTemplate);
	}
}
