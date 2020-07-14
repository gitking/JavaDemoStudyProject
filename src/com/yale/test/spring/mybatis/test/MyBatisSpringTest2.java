package com.yale.test.spring.mybatis.test;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.yale.test.spring.mybatis.dao.UserDao;
import com.yale.test.spring.mybatis.vo.User;

public class MyBatisSpringTest2 {
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("com/yale/test/spring/vo/beans.xml");
		UserDao userDao = (UserDao)context.getBean("userDao");

		List<User> userList = userDao.selectUser();
		/*
		 * 这里需要使用Oracle的ojdbc6的jar包,ojdbc5的jar包会报错:
		 * Method oracle/jdbc/driver/OracleResultSetImpl.isClosed()Z is abstract
		 */
		for (User user: userList) {
			System.out.println("用户名为:" + user.getName());
		}
	}
}
