package com.yale.test.spring.mybatis.dao.impl;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;

import com.yale.test.spring.mybatis.dao.UserDao;
import com.yale.test.spring.mybatis.vo.User;

public class UserDaoImpl2 extends SqlSessionDaoSupport implements UserDao{
	/*
	 * Spring整合MyBatis的第二种方式,直接继承SqlSessionDaoSupport类
	 * SqlSessionDaoSupport类里面有个getSqlSession()方法,直接调用他来操作数据库就行,
	 * 不需要使用SqlSessionTemplate这个类了
	 * private SqlSessionTemplate sqlSession;
	 */
	
	@Override
	public List<User> selectUser() {
		User user = new User();
		user.setName("大侠");
		System.out.println("看看报错事务会提交吗:" + user.getName());
		user.setPwd("9989");
		getSqlSession().insert("com.yale.test.spring.mybatis.vo.user.mapper.add", user);
		
		/*
		 * delete会报错,因为我配置文件里面的SQL语法故意写错了,但是你会发现上面的insert into 的事务提交了
		 * 如果你想实现事务的管理,需要在Spring里面配置声明式事务管理.
		 */
		//sqlSession.rollback();手工回滚事务
		//sqlSession.commit();手工提交事务
		//sqlSession.getConnection();获取JDBC的connection连接
		//getSqlSession().delete("com.yale.test.spring.mybatis.vo.user.mapper.remove", 20);
		return getSqlSession().selectList("com.yale.test.spring.mybatis.vo.user.mapper.selectAll");
	}
	
	@Override
	public List<User> getUser() {
		System.out.println("pring 设置的readOnly事务属性对oracle来说是无效的。");
		User user = new User();
		user.setName("大侠1");
		System.out.println("只读事务会提交吗:" + user.getName());
		user.setPwd("9989");
		getSqlSession().insert("com.yale.test.spring.mybatis.vo.user.mapper.add", user);
		
		/*
		 * delete会报错,因为我配置文件里面的SQL语法故意写错了,但是你会发现上面的insert into 的事务提交了
		 * 如果你想实现事务的管理,需要在Spring里面配置声明式事务管理.
		 */
		//sqlSession.rollback();手工回滚事务
		//sqlSession.commit();手工提交事务
		//sqlSession.getConnection();获取JDBC的connection连接
		return getSqlSession().selectList("com.yale.test.spring.mybatis.vo.user.mapper.selectAll");
	}
}
