package com.yale.test.design.structural.proxy.pool;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

/*
 * 代理
 * 为其他对象提供一种代理以控制对这个对象的访问。
 * 代理模式，即Proxy，它和Adapter模式很类似。我们先回顾Adapter模式，它用于把A接口转换为B接口：
 * public BAdapter implements B {
	    private A a;
	    public BAdapter(A a) {
	        this.a = a;
	    }
	    public void b() {
	        a.a();
	    }
	}
 * 而Proxy模式不是把A接口转换成B接口，它还是转换成A接口：
 * public AProxy implements A {
	    private A a;
	    public AProxy(A a) {
	        this.a = a;
	    }
	    public void a() {
	        this.a.a();
	    }
	}
 * 合着Proxy就是为了给A接口再包一层，这不是脱了裤子放屁吗？
 * 当然不是。我们观察Proxy的实现A接口的方法：
 *  public void a() {
	    this.a.a();
	}
 * 这样写当然没啥卵用。但是，如果我们在调用a.a()的前后，加一些额外的代码：
 * public void a() {
	    if (getCurrentUser().isRoot()) {
	        this.a.a();
	    } else {
	        throw new SecurityException("Forbidden");
	    }
	}
 * 这样一来，我们就实现了权限检查，只有符合要求的用户，才会真正调用目标方法，否则，会直接抛出异常。
 * 有的童鞋会问，为啥不把权限检查的功能直接写到目标实例A的内部？
 * 因为我们编写代码的原则有：1.职责清晰：一个类只负责一件事；2.易于测试：一次只测一个功能。
 * 用Proxy实现这个权限检查，我们可以获得更清晰、更简洁的代码：1.A接口：只定义接口；2.ABusiness类：只实现A接口的业务逻辑；3.APermissionProxy类：只实现A接口的权限检查代理。
 * 如果我们希望编写其他类型的代理，可以继续增加类似ALogProxy，而不必对现有的A接口、ABusiness类进行修改。
 * 实际上权限检查只是代理模式的一种应用。Proxy还广泛应用在：
 * 远程代理
 * 远程代理即Remote Proxy，本地的调用者持有的接口实际上是一个代理，这个代理负责把对接口的方法访问转换成远程调用，然后返回结果。Java内置的RMI机制就是一个完整的远程代理模式。
 * 虚代理
 * 虚代理即Virtual Proxy，它让调用者先持有一个代理对象，但真正的对象尚未创建。如果没有必要，这个真正的对象是不会被创建的，直到客户端需要真的必须调用时，才创建真正的对象。
 * JDBC的连接池返回的JDBC连接（Connection对象）就可以是一个虚代理，即获取连接时根本没有任何实际的数据库连接，直到第一次执行JDBC查询或更新操作时，才真正创建实际的JDBC连接。
 * 保护代理
 * 保护代理即Protection Proxy，它用代理对象控制对原始对象的访问，常用于鉴权。
 * 智能引用
 * 智能引用即Smart Reference，它也是一种代理对象，如果有很多客户端对它进行访问，通过内部的计数器可以在外部调用者都不使用后自动释放它。
 * 我们来看一下如何应用代理模式编写一个JDBC连接池（DataSource）。我们首先来编写一个虚代理，即如果调用者获取到Connection后，并没有执行任何SQL操作，那么这个Connection Proxy实际上并不会真正打开JDBC连接。调用者代码如下：
 * DataSource lazyDataSource = new LazyDataSource(jdbcUrl, jdbcUsername, jdbcPassword);
	System.out.println("get lazy connection...");
	try (Connection conn1 = lazyDataSource.getConnection()) {
	    // 并没有实际打开真正的Connection
	}
	System.out.println("get lazy connection...");
	try (Connection conn2 = lazyDataSource.getConnection()) {
	    try (PreparedStatement ps = conn2.prepareStatement("SELECT * FROM students")) { // 打开了真正的Connection
	        try (ResultSet rs = ps.executeQuery()) {
	            while (rs.next()) {
	                System.out.println(rs.getString("name"));
	            }
	        }
	    }
	}
 * 现在我们来思考如何实现这个LazyConnectionProxy。为了简化代码，我们首先针对Connection接口做一个抽象的代理类：
 * 这个AbstractConnectionProxy代理类的作用是把Connection接口定义的方法全部实现一遍，因为Connection接口定义的方法太多了，后面我们要编写的LazyConnectionProxy只需要继承AbstractConnectionProxy，
 * 就不必再把Connection接口方法挨个实现一遍。
 * LazyConnectionProxy实现如下：
 * 这个AbstractConnectionProxy代理类的作用是把Connection接口定义的方法全部实现一遍，因为Connection接口定义的方法太多了，后面我们要编写的LazyConnectionProxy只需要继承AbstractConnectionProxy，就不必再把Connection接口方法挨个实现一遍。
 * LazyConnectionProxy实现如下：
 * 如果调用者没有执行任何SQL语句，那么target字段始终为null。只有第一次执行SQL语句时（即调用任何类似prepareStatement()方法时，触发getRealConnection()调用），才会真正打开实际的JDBC Connection。
 */
public abstract class AbstractConnectionProxy implements Connection {
	
	//抽象方法获取实际的Connection
	protected abstract Connection getRealConnection();
	
	//实现Connection接口的每一个方法
	@Override
	public Statement createStatement() throws SQLException {
		return getRealConnection().createStatement();
	}
	
	@Override
	public PreparedStatement prepareStatement(String sql) throws SQLException{
		return getRealConnection().prepareStatement(sql);
	}
	
	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return getRealConnection().isWrapperFor(iface);
	}
	
	@Override
	public CallableStatement prepareCall(String sql) throws SQLException {
		return getRealConnection().prepareCall(sql);
	}
	
	@Override
	public String nativeSQL(String sql) throws SQLException {
		return getRealConnection().nativeSQL(sql);
	}
	
	@Override
	public void setAutoCommit(boolean autoCommit) throws SQLException {
		getRealConnection().setAutoCommit(autoCommit);
	}
	
	@Override
	public boolean getAutoCommit() throws SQLException {
		return getRealConnection().getAutoCommit();
	}
	
	@Override
	public void commit() throws SQLException {
		getRealConnection().commit();
	}
	
	@Override
	public void rollback() throws SQLException {
		getRealConnection().rollback();
	}
	
	@Override
	public void close() throws SQLException {
		getRealConnection().close();
	}
	
	@Override
	public boolean isClosed() throws SQLException {
		return getRealConnection().isClosed();
	}
	
	@Override
	public DatabaseMetaData getMetaData() throws SQLException {
		return getRealConnection().getMetaData();
	}
	
	@Override
	public void setReadOnly(boolean readOnly) throws SQLException {
		getRealConnection().setReadOnly(readOnly);
	}
	
	@Override
	public boolean isReadOnly() throws SQLException {
		return getRealConnection().isReadOnly();
	}
	@Override
	public void setCatalog(String catalog) throws SQLException {
		getRealConnection().setCatalog(catalog);
	}
	
	@Override
	public String getCatalog() throws SQLException {
		return getRealConnection().getCatalog();
	}
	
	@Override
	public void setTransactionIsolation(int level) throws SQLException {
		getRealConnection().setTransactionIsolation(level);
	}
	
	@Override
	public int getTransactionIsolation() throws SQLException {
		return getRealConnection().getTransactionIsolation();
	}
	
	@Override
	public SQLWarning getWarnings() throws SQLException {
		return getRealConnection().getWarnings();
	}
	
	@Override
	public void clearWarnings() throws SQLException {
		getRealConnection().clearWarnings();
	}

	@Override
	public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
		return getRealConnection().createStatement(resultSetType, resultSetConcurrency);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
			throws SQLException {
		return getRealConnection().prepareStatement(sql, resultSetType, resultSetConcurrency);
	}

	@Override
	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
		return getRealConnection().prepareCall(sql, resultSetType, resultSetConcurrency);
	}

	@Override
	public Map<String, Class<?>> getTypeMap() throws SQLException {
		return getRealConnection().getTypeMap();
	}

	@Override
	public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
		getRealConnection().setTypeMap(map);
	}

	@Override
	public void setHoldability(int holdability) throws SQLException {
		getRealConnection().setHoldability(holdability);
	}

	@Override
	public int getHoldability() throws SQLException {
		return getRealConnection().getHoldability();
	}

	@Override
	public Savepoint setSavepoint() throws SQLException {
		return getRealConnection().setSavepoint();
	}

	@Override
	public Savepoint setSavepoint(String name) throws SQLException {
		return getRealConnection().setSavepoint(name);
	}

	@Override
	public void rollback(Savepoint savepoint) throws SQLException {
		getRealConnection().rollback(savepoint);
	}

	@Override
	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		getRealConnection().releaseSavepoint(savepoint);
	}

	@Override
	public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		return getRealConnection().createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency,
			int resultSetHoldability) throws SQLException {
		return getRealConnection().prepareStatement(sql, resultSetType, resultSetConcurrency);
	}

	@Override
	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency,
			int resultSetHoldability) throws SQLException {
		return getRealConnection().prepareCall(sql, resultSetType, resultSetConcurrency);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
		return getRealConnection().prepareStatement(sql, autoGeneratedKeys);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
		return getRealConnection().prepareStatement(sql, columnIndexes);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
		return getRealConnection().prepareStatement(sql, columnNames);
	}

	@Override
	public Clob createClob() throws SQLException {
		return getRealConnection().createClob();
	}

	@Override
	public Blob createBlob() throws SQLException {
		return getRealConnection().createBlob();
	}

	@Override
	public NClob createNClob() throws SQLException {
		return getRealConnection().createNClob();
	}

	@Override
	public SQLXML createSQLXML() throws SQLException {
		return getRealConnection().createSQLXML();
	}

	@Override
	public boolean isValid(int timeout) throws SQLException {
		return getRealConnection().isValid(timeout);
	}

	@Override
	public void setClientInfo(String name, String value) throws SQLClientInfoException {
		getRealConnection().setClientInfo(name, value);
	}

	@Override
	public void setClientInfo(Properties properties) throws SQLClientInfoException {
		getRealConnection().setClientInfo(properties);
	}

	@Override
	public String getClientInfo(String name) throws SQLException {
		return getRealConnection().getClientInfo(name);
	}

	@Override
	public Properties getClientInfo() throws SQLException {
		return getRealConnection().getClientInfo();
	}

	@Override
	public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
		return getRealConnection().createArrayOf(typeName, elements);
	}

	@Override
	public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
		return getRealConnection().createStruct(typeName, attributes);
	}

	@Override
	public void setSchema(String schema) throws SQLException {
		getRealConnection().setSchema(schema);
	}

	@Override
	public String getSchema() throws SQLException {
		return getRealConnection().getSchema();
	}

	@Override
	public void abort(Executor executor) throws SQLException {
		getRealConnection().abort(executor);
	}

	@Override
	public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
		getRealConnection().setNetworkTimeout(executor, milliseconds);
	}

	@Override
	public int getNetworkTimeout() throws SQLException {
		return getRealConnection().getNetworkTimeout();
	}
	
	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return getRealConnection().unwrap(iface);
	}
}
