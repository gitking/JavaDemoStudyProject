package com.yale.test.mybatis.dao;

import java.io.IOException;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.yale.test.mybatis.util.MyBatisUtil;
import com.yale.test.mybatis.vo.User;

public class UserDao {
	
	public List<User> getAll() throws IOException {
		SqlSession session = MyBatisUtil.getSession();
		List<User> userList = session.selectList("com.yale.test.mybatis.vo.UserMapper.selectAll");
		session.close();
		return userList;
	}
	
	public User getById(int id) throws IOException {
		SqlSession session = MyBatisUtil.getSession();
		User user = session.selectOne("com.yale.test.mybatis.vo.UserMapper.selectUser", id);
		session.close();
		return user;
	}
	
	public int add(User user) throws IOException {
		SqlSession session = MyBatisUtil.getSession();
		int result = session.insert("com.yale.test.mybatis.vo.UserMapper.addUser", user);
		session.commit();//不提交,插入不进去,mybatis必须手动提交事务
		session.close();
		return result;
	}
	
	public int update(User user) throws IOException {
		SqlSession session = MyBatisUtil.getSession();
		/*
		 * 注意这里用的MyBatis的insert方法来更新的,也可以使用update方法
		 * 原理是这样的：在org.apache.ibatis.session.defaults.DefaultSqlSession
		 * 这个类里面insert和delete方法,最终调用的都是update方法
		 */
		int result = session.insert("com.yale.test.mybatis.vo.UserMapper.addUser", user);
		session.commit();//不提交,插入不进去,mybatis必须手动提交事务
		session.close();
		return result;
	}
	
	public int delete(int id) throws IOException {
		SqlSession session = MyBatisUtil.getSession();
		/*
		 * 注意这里用的MyBatis的insert方法来更新的,也可以使用update方法
		 * 原理是这样的：在org.apache.ibatis.session.defaults.DefaultSqlSession
		 * 这个类里面insert和delete方法,最终调用的都是update方法
		 */
		int result = session.delete("com.yale.test.mybatis.vo.UserMapper.deleteUser", id);
		session.commit();//不提交,插入不进去,mybatis必须手动提交事务
		session.close();
		return result;
	}
}
