package com.yale.test.java.demo.jicheng.demo;

public class Test {
	public static void main(String[] args) {
		UserDao ud = new UserDao();
		ud.setJdbcTemplate("子类设置的jdbcTemplate");//注意这里调用的是父类的方法
		
		ud.print();//父类的方法
		System.out.println("---------------------------------------");

		System.out.println(ud.getJdbcTemplate());//调用的是父类的方法
		System.out.println("---------------------------------------");

		System.out.println(ud.subGetJdbcTemplate());
		System.out.println("---------------------------------------");
		
		ud.subPrintValue();
		System.out.println("============================");
		
		ud.setValue1();
		
		ud.print();//父类的方法
		System.out.println("---------------------------------------");

		System.out.println(ud.getJdbcTemplate());//调用的是父类的方法
		System.out.println("---------------------------------------");

		System.out.println(ud.subGetJdbcTemplate());
		System.out.println("---------------------------------------");
		
		ud.subPrintValue();
		System.out.println("---------------------------------------");
		
		
		System.out.println("1============================1");
		
		ud.setValue();
		
		ud.print();//父类的方法
		System.out.println("---------------------------------------");

		System.out.println(ud.getJdbcTemplate());//调用的是父类的方法
		System.out.println("---------------------------------------");

		System.out.println(ud.subGetJdbcTemplate());
		System.out.println("---------------------------------------");
		
		ud.subPrintValue();
		System.out.println("---------------------------------------");
	}
}
