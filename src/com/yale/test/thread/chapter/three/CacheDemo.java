package com.yale.test.thread.chapter.three;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
/**
 * 缓存数据实现
 * @author Administrator
 */
public class CacheDemo {

	private Map<String,Object> cache = new HashMap<String,Object>();
	private ReadWriteLock rwl = new ReentrantReadWriteLock();
	public static void main(String[] args) {
	}
	public Object get(String key) {
		Object data = null;
		try {
			this.rwl.readLock().lock();//先上一把读锁
			data = cache.get(key);
			if (data== null) {//等于null说要去写数据了(真实业务场景是去数据库读数据)
				rwl.readLock().unlock();//先释放读锁
				try {
					rwl.writeLock().lock();//然后上一把写锁
					if (data == null) {
						data = "success";
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					this.rwl.writeLock().unlock();//释放写锁
				}
				rwl.readLock().lock();
			} else {
				return data;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.rwl.readLock().unlock();
		}
		return data;
	}
}
