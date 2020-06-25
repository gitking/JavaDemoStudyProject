package com.yale.test.spring.dao.impl;

import com.yale.test.spring.dao.UserDao;

public class UserDaoMySqlImpl implements UserDao{
	
	public UserDaoMySqlImpl(){
		System.out.println("Spring调用我的UserDaoMySqlImpl无参构造方法了,实例化了。。。。");
		System.out.println("***并且是在读取配置文件的时候就直接创建我UserDaoMySqlImpl的对象了***");
	}
	@Override
	public void getUser() {
		System.out.println("MySQL获取用户数据");
	}
}
