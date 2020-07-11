package com.yale.test.springmvc.mybatis.dao;

import java.util.List;

import com.yale.test.springmvc.mybatis.vo.User;

public interface UserDao {
	public List<User> list();
}
