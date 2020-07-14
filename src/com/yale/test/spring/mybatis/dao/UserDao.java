package com.yale.test.spring.mybatis.dao;

import java.util.List;

import com.yale.test.spring.mybatis.vo.User;

public interface UserDao {
	List<User> selectUser();
	List<User> getUser();
}
