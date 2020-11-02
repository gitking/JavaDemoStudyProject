package com.yale.test.spring.database.jdbc;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/*
 * 可以通过HSQLDB自带的工具来初始化数据库表，这里我们写一个Bean，在Spring容器启动时自动创建一个users表：
 */
@Component
public class DatabaseInitializer {
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	public void init() {
		jdbcTemplate.update("DROP TABLE IF EXISTS users");
		jdbcTemplate.update("CREATE TABLE IF NOT EXISTS users("
				+ "id BIGINT IDENTITY NOT NULL PRIMARY KEY,"
				+ "email VARCHAR(100) NOT NULL,"
				+ "password VARCHAR(100) NOT NULL,"
				+ "name VARCHAR(100) NOT NULL,"
				+ "UNIQUE(email))");
	}
	
	/*
	 * 不可第二次运行
	 * 我发现这一章的代码实例里都会因为“用户重复”的原因不可重复正常运行，会报错。
	 * 在DatabaseInitializer里加入清理方法即可解决
	 * 如果表已经创建
	 * 在init()的第一行再加上个
	 * jdbcTemplate.update("DROP TABLE IF EXISTS users");
	 * 就能不手动清除数据库表正常运行了（不然貌似报错后压根执行不到@PreDestroy方法）
	 */
	@PreDestroy
	public void clean() {
		jdbcTemplate.update("DROP TABLE users");
	}
}
