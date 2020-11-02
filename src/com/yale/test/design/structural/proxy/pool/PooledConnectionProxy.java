package com.yale.test.design.structural.proxy.pool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Queue;

public class PooledConnectionProxy extends AbstractConnectionProxy {
	
	Connection target;// 实际的Connection:
	Queue<PooledConnectionProxy> idleQueue;// 空闲队列:
	
	public PooledConnectionProxy(Queue<PooledConnectionProxy> idlequeue, Connection target) {
		this.target = target;
		this.idleQueue = idleQueue;
	}
	
	@Override
	public void close() throws SQLException {
		System.out.println("Fake close and released to idle queue for future reuse: " + target);
		// 并没有调用实际Connection的close()方法,
        // 而是把自己放入空闲队列:
		//复用连接的关键在于覆写close()方法，它并没有真正关闭底层JDBC连接，而是把自己放回一个空闲队列，以便下次使用。
		idleQueue.offer(this);
	}
	
	@Override
	protected Connection getRealConnection() {
		return target;
	}
}
