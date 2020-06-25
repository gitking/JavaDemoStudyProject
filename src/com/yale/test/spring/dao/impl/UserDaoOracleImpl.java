package com.yale.test.spring.dao.impl;

import com.yale.test.spring.dao.UserDao;

public class UserDaoOracleImpl implements UserDao{
	public UserDaoOracleImpl(){
		System.out.println("Spring调用我的UserDaoOracleImpl无参构造方法了,实例化了。。。。");
		System.out.println("***并且是在读取配置文件的时候就直接创建我UserDaoOracleImpl的对象了***");
	}
	
	@Override
	public void getUser() {
		System.out.println("oracle获取用户数据");
	}
}
