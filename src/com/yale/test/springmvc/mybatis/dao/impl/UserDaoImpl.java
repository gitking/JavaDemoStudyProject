package com.yale.test.springmvc.mybatis.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.yale.test.springmvc.mybatis.dao.UserDao;
import com.yale.test.springmvc.mybatis.vo.User;

@Repository("userDao")
public class UserDaoImpl extends SqlSessionDaoSupport implements UserDao {
		@Autowired
		@Override
		public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
			super.setSqlSessionFactory(sqlSessionFactory);
		}
		@Override
		public List<User> list() {
			return getSqlSession().selectList("com.yale.test.spring.mybatis.vo.user.mapper.list");
		}
}
