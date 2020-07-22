package com.yale.test.mybatis.test;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.yale.test.mybatis.dao.SqlDao;
import com.yale.test.mybatis.util.MyBatisUtil;
import com.yale.test.mybatis.vo.User;

public class MyBatisAnnotationTest {
	public static void main(String[] args) throws IOException {
		SqlSession sqlSession = MyBatisUtil.getSession();
		SqlDao sqlDao = sqlSession.getMapper(SqlDao.class);
		System.out.println("这里想都不用想,myBatis返回的肯定是一个代理类:" + sqlDao.getClass());
		List<User> userList = sqlDao.getAll();
		for (Iterator<User> it =userList.iterator(); it.hasNext();) {
			User user = it.next();
			System.out.println("使用注解的方式进行查询:id=" + user.getId() + ",name=" + user.getName() + ",password=" + user.getPassword());
		}
		User user = new User();
		user.setId(001);
		user.setName("注解");
		user.setPassword("9808009");
		int res = sqlDao.addUser(user);
		System.out.println("使用注解插入数据:" + res);
	}
}
