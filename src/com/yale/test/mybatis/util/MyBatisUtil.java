package com.yale.test.mybatis.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class MyBatisUtil {
	public static void main(String[] args) {
		
	}
	
	/*
	 * 通过配置文件创建SqlSessionFactory,SqlSessionFactory是一个SqlSession的工厂
	 */
	public static SqlSessionFactory getSqlSessionFactory() throws IOException {
		String resource = "com/yale/test/mybatis/mybatis-config.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		return sqlSessionFactory;
	}
	
	/*
	 * SqlSession通过id找到对应的SQL语句,然后执行SQL语句
	 */
	public static SqlSession getSession() throws IOException {
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		return sqlSessionFactory.openSession();
	}
}
