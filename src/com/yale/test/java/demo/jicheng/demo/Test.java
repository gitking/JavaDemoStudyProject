package com.yale.test.java.demo.jicheng.demo;

public class Test {
	public static void main(String[] args) {
		UserDao ud = new UserDao();
		ud.setJdbcTemplate("子类设置的jdbcTemplate");
		ud.print();
		System.out.println(ud.getJdbcTemplate());
	}
}
