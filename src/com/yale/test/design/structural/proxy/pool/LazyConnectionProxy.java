package com.yale.test.design.structural.proxy.pool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Supplier;

/*
 * 如果调用者没有执行任何SQL语句，那么target字段始终为null。只有第一次执行SQL语句时（即调用任何类似prepareStatement()方法时，
 * 触发getRealConnection()调用），才会真正打开实际的JDBC Connection。
 * 最后，我们还需要编写一个LazyDataSource来支持这个LazyConnecitonProxy：
 * 可见第一个getConnection()调用获取到的LazyConnectionProxy并没有实际打开真正的JDBC Connection。
 */
public class LazyConnectionProxy extends AbstractConnectionProxy{
	//2、供给型函数式接口(方法无参有返回值): public interface Supplier<T> {public T get()}  
	private Supplier<Connection> supplier;
	private Connection target = null;
	
	public LazyConnectionProxy(Supplier<Connection> supplier) {
		this.supplier = supplier;
	}
	
	@Override
	public void close() throws SQLException {
		if (target != null) {
			System.out.println("Close connection: " + target);
			super.close();
		}
	}
	
	@Override
	protected Connection getRealConnection() {
		if (target != null) {
			target = supplier.get();
		}
		return target;
	}
}
