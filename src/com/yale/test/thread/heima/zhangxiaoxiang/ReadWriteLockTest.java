package com.yale.test.thread.heima.zhangxiaoxiang;

import java.util.Random;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockTest {

	public static void main(String[] args) {
		final Queue3 q3 = new Queue3();
		for (int i=0;i<3;i++) {
			new Thread(){
				@Override
				public void run() {
					while(true) {
						q3.get();
					}
				}
			}.start();
			
			new Thread(){
				public void run() {
					while(true) {
						q3.put(new Random().nextInt(10000));
					}
				}
			}.start();
		}
	}
}

class Queue3 {
	private Object data = null;//共享数据
	//读写锁
	private ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
	public void get() {
		rwl.readLock().lock();//读锁不互斥
		System.out.println(Thread.currentThread().getName() + " be ready to data...");
		try {
			Thread.sleep((long)(Math.random()*1000));
			System.out.println(Thread.currentThread().getName() + " have read data:" + data);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {//释放锁要放在finally里面
			rwl.readLock().unlock();
		}
	}
	
	public void put (Object data) {
		rwl.writeLock().lock();
		System.out.println(Thread.currentThread().getName() + " be ready to write data...");
		try {
			Thread.sleep((long)(Math.random()*1000));
			this.data = data;
			System.out.println(Thread.currentThread().getName() + " have write data:" + data);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			rwl.writeLock().unlock();//释放锁要放在finally里面
		}
	}
}

class CacheData {
	//这个例子是JAVA6官方API文档中ReentrantReadWriteLock类的一个例子
	Object data;
	volatile boolean cacheValid;
	ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
	void processCacheData() {
		rwl.readLock().lock();
		if (!cacheValid) {
			//在获得写锁之前必须释放读锁
			rwl.readLock().unlock();
			rwl.writeLock().lock();
			//重复检查状态,因为其他线程可能已经获得写锁了并且改变了状态,在我们之前
			if (!cacheValid){
				data = "1231";//这里可以换成去数据库读取数据
				cacheValid = true;
			}
			rwl.readLock().lock();//在释放写锁之前先上降级后的读锁
			rwl.writeLock().unlock();//释放写锁,读锁依然在
		}
		//use(data);可以使用数据了
		rwl.readLock().unlock();
	}
}
