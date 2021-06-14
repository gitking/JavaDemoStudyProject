package com.yale.test.mybatis.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/*
 * https://www.cnblogs.com/fangjian0423/p/spring-mybatis-MapperScannerConfigurer-analysis.html
 * https://www.cnblogs.com/fangjian0423/p/java-dynamic-proxy.html
 * org.mybatis.spring.mapper.MapperScannerConfigurer
 * <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer"> 这里就是参考公司的影像系统
 * 		<property name="basePackage" value="com.file/client/mapper" />
 *	</bean>
 */
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
