package com.yale.test.java.demo.jicheng.demo;

public class DaoSupport {
	public String jdbcTemplate;
	
	public final void setJdbcTemplate(String jdbcTemplate) {
		//如果有局部变量和字段重名，那么局部变量优先级更高，就必须加上this：
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public final String getJdbcTemplate() {
		return "【父类方法】:" + this.jdbcTemplate;
	}
	
	public void print() {
		System.out.println("【父类】现在是子类调用父类方法:" + this.jdbcTemplate);
	}
}
