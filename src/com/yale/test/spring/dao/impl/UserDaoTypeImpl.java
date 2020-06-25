package com.yale.test.spring.dao.impl;

import com.yale.test.spring.dao.UserDao;

public class UserDaoTypeImpl implements UserDao{
	public UserDaoTypeImpl(){
		System.out.println("Spring调用我的UserDaoTypeImpl无参构造方法了,实例化了。。。。");
		System.out.println("***并且是在读取配置文件的时候就直接创建我UserDaoTypeImpl的对象了***");
	}
	
	@Override
	public void getUser() {
		System.out.println("UserDaoTypeImpl获取用户数据");
	}
}
