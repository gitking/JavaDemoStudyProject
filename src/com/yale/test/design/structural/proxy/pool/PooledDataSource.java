package com.yale.test.design.structural.proxy.pool;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Logger;

import javax.sql.DataSource;

/*
 * 使用连接池的时候，我们更希望能重复使用连接。如果调用方编写这样的代码：
 * 调用方并不关心是否复用了Connection，但从PooledDataSource获取的Connection确实自带这个优化功能。如何实现可复用Connection的连接池？答案仍然是使用代理模式。
 * 复用连接的关键在于覆写close()方法，它并没有真正关闭底层JDBC连接，而是把自己放回一个空闲队列，以便下次使用。
 * 空闲队列由PooledDataSource负责维护：
 */
public class PooledDataSource implements DataSource{
	private String url;
	private String username;
	private String password;
	
	// 维护一个空闲队列:
	private Queue<PooledConnectionProxy> idleQueue = new ArrayBlockingQueue<>(100);
	
	public PooledDataSource(String url, String username, String password) {
		this.url = url;
		this.username = username;
		this.password = password;
	}
	
	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		PooledConnectionProxy conn = idleQueue.poll();// 首先试图获取一个空闲连接:
		if (conn == null) {
			conn = openNewConnection();// 没有空闲连接时，打开一个新连接:
		} else {
			System.out.println("Return pooled connection: " + conn.target);
		}
		return conn;
	}
	
	@Override
	public Connection getConnection() throws SQLException {
		return getConnection(this.username, this.password);
	}
	
	private PooledConnectionProxy openNewConnection() throws SQLException {
		Connection conn = DriverManager.getConnection(url, username, password);
		System.out.println("Open new connection: " + conn);
		return new PooledConnectionProxy(idleQueue, conn);
	}
	
	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		throw new SQLFeatureNotSupportedException();
	}
}
