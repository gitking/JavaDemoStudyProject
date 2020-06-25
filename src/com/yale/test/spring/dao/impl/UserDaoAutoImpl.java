package com.yale.test.spring.dao.impl;

import com.yale.test.spring.dao.UserDao;

public class UserDaoAutoImpl implements UserDao{
	public UserDaoAutoImpl(){
		System.out.println("Spring调用我的UserDaoAutoImpl无参构造方法了,实例化了。。。。");
		System.out.println("***并且是在读取配置文件的时候就直接创建我UserDaoAutoImpl的对象了***");
	}
	
	@Override
	public void getUser() {
		System.out.println("UserDaoAutoImpl获取用户数据");
	}
}
