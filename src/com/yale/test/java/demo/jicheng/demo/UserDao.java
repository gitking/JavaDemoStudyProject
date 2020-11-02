package com.yale.test.java.demo.jicheng.demo;

public class UserDao extends DaoSupport{
	private String jdbcTemplate;
	
	public void setValue() {
		this.setJdbcTemplate("子类设置的值");
	}
}
