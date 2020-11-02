package com.yale.test.design.structural.proxy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.yale.test.design.structural.proxy.pool.LazyDataSource;
import com.yale.test.design.structural.proxy.pool.PooledDataSource;

/*
 * 
 */
public class Test {
	static final String jdbcUrl = "jdbc:mysql://localhost/learnjdbc?userSSL=false&characterEncoding=utf8";
	static final String jdbcUserName = "learn";
	static final String jdbcPassword = "learpassword";
	public static void main(String[] args) {
		DataSource lazyDataSource = new LazyDataSource(jdbcUrl, jdbcUserName, jdbcPassword);
		System.out.println("get lazy connection...");
		
		try (Connection conn1 = lazyDataSource.getConnection()){
			//没有实际打开真实的Connection
			System.out.println("get lazy connection...");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try (Connection conn2 = lazyDataSource.getConnection()) {
			try(PreparedStatement ps = conn2.prepareStatement("select * from students")){//打开了真正的Connection
				try(ResultSet rs = ps.executeQuery()) {
					while(rs.next()) {
						System.out.println(rs.getString("name"));
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		/*
		 * 除了第一次打开了一个真正的JDBC Connection，后续获取的Connection实际上是同一个JDBC Connection。但是，对于调用方来说，完全不需要知道底层做了哪些优化。
		 * 我们实际使用的DataSource，例如HikariCP，都是基于代理模式实现的，原理同上，但增加了更多的如动态伸缩的功能（一个连接空闲一段时间后自动关闭）。
		 * 有的童鞋会发现Proxy模式和Decorator模式有些类似。确实，这两者看起来很像，但区别在于：Decorator模式让调用者自己创建核心类，然后组合各种功能，
		 * 而Proxy模式决不能让调用者自己创建再组合，否则就失去了代理的功能。Proxy模式让调用者认为获取到的是核心类接口，但实际上是代理类。
		 * 小结
		 * 代理模式通过封装一个已有接口，并向调用方返回相同的接口类型，能让调用方在不改变任何代码的前提下增强某些功能（例如，鉴权、延迟加载、连接池复用等）。
		 * 使用Proxy模式要求调用方持有接口，作为Proxy的类也必须实现相同的接口类型。
		 */
		DataSource pooledDataSource = new PooledDataSource(jdbcUrl, jdbcUserName, jdbcPassword);
		try(Connection conn = pooledDataSource.getConnection()){
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try(Connection conn = pooledDataSource.getConnection()) {
			//获取到的是同一个Connection
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try(Connection conn = pooledDataSource.getConnection()) {
			//获取到的是同一个Connection
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
}
