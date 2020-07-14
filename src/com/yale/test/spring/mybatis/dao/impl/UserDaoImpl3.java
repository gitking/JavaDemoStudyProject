package com.yale.test.spring.mybatis.dao.impl;

import java.util.List;

import com.yale.test.spring.mybatis.dao.UserDao;
import com.yale.test.spring.mybatis.dao.UserMapper;
import com.yale.test.spring.mybatis.vo.User;

/*
 * Spring整合MyBatis的第三种方式,SqlSessionDaoSupport类和SqlSessionTemplate类都不需要了
 */
public class UserDaoImpl3 implements UserDao{
	/*
	 * 其实这个UserMapper可以直接用了,不需要注入到这个类里面.
	 * 你怎么用都行
	 */
	private UserMapper userMapper;
	
	public void setUserMapper(UserMapper userMapper) {
		System.out.println("注意这个接口UserMapper并没有实现类:所以这里Spring注入的是一个代理类" + userMapper.getClass());
		this.userMapper = userMapper;
	}

	@Override
	public List<User> selectUser() {
		return userMapper.selectUser();
	}
	
	@Override
	public List<User> getUser() {
		return null;
	}
}
