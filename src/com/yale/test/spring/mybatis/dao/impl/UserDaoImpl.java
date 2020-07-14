package com.yale.test.spring.mybatis.dao.impl;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;

import com.yale.test.spring.mybatis.dao.UserDao;
import com.yale.test.spring.mybatis.vo.User;

public class UserDaoImpl implements UserDao{
	/*
	 * SqlSessionTemplate是mybatis-spring整合的一个模板类,
	 * 可以把SqlSessionTemplate理解为对mybatis的org.apache.ibatis.session.SqlSession类的封装;
	 * SqlSessionTemplate实现了SqlSession这个接口
	 * SqlSessionTemplate和SqlSession都需要通过org.apache.ibatis.session.SqlSessionFactory获得
	 * 一起SqlSession是我们自己创建的,现在交给Spring管理了
	 */
	private SqlSessionTemplate sqlSession;
	@Override
	public List<User> selectUser() {
		User user = new User();
		user.setName("大侠");
		System.out.println("看看报错事务会提交吗:" + user.getName());
		user.setPwd("9989");
		sqlSession.insert("com.yale.test.spring.mybatis.vo.user.mapper.add", user);
		
		/*
		 * delete会报错,因为我配置文件里面的SQL语法故意写错了,但是你会发现上面的insert into 的事务提交了
		 * 如果你想实现事务的管理,需要在Spring里面配置声明式事务管理.
		 */
		sqlSession.delete("com.yale.test.spring.mybatis.vo.user.mapper.remove", 20);
		return sqlSession.selectList("com.yale.test.spring.mybatis.vo.user.mapper.selectAll");
	}
	public void setSqlSession(SqlSessionTemplate sqlSession) {
		this.sqlSession = sqlSession;
	}
}
