package com.yale.test.spring.services.impl;

import com.yale.test.spring.dao.UserDao;
import com.yale.test.spring.services.UserService;

public class SimpleUserServiceImpl implements UserService{
private UserDao autDao;//演示Spring的自动注入(自动装配),自动装配主要为了简化Spring的配置文件, 不推荐使用自动装配,而是使用Annoation代替,
	
	public UserDao getAutDao() {
		return autDao;
	}
	public void setAutDao(UserDao autDao) {
		this.autDao = autDao;
	}


	@Override
	public void getUser() {
		System.out.println("我在使用spring的autowire=byName 自动注入特性...........");
		this.autDao.getUser();
	}
}
