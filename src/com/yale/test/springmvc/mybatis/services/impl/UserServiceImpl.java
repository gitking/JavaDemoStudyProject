package com.yale.test.springmvc.mybatis.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yale.test.springmvc.mybatis.dao.UserDao;
import com.yale.test.springmvc.mybatis.services.UserService;
import com.yale.test.springmvc.mybatis.vo.User;

@Service("userService")
public class UserServiceImpl implements UserService{
	@Autowired
	private UserDao userDao;

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	@Override
	public List<User> list() {
		return userDao.list();
	}
}
