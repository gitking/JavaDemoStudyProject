package com.yale.test.spring.services.impl;

import com.yale.test.spring.dao.UserDao;
import com.yale.test.spring.services.UserService;

public class UserServiceImpl implements UserService{
	private UserDao userDao;

	public UserDao getUserDao() {
		return userDao;
	}

	/**
	 * 在spring配置文件里面配置property属性时,属性的name一定等于
	 * setSsserDao这个set方法后面的SsserDao,并且把首字母变成小写
	 * @param userDao
	 */
	public void setSsserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	@Override
	public void getUser() {
		this.userDao.getUser();
	}
}
