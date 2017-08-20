package com.yale.test.thread.chapter.three;

import java.util.Random;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockTest {

	public static void main(String[] args) {
		final Queue3 q3 = new Queue3();
		for (int i=0;i<3;i++) {
			new Thread(){
				public void run(){
					while (true) {
						q3.get();
					}
				}
			}.start();
			new Thread(){
				public void run(){
					while (true) {
						q3.put(new Random().nextInt(10000));
					}
				}
			}.start();
		}
	}
}
class Queue3{
	private Object data = null;
	private ReadWriteLock rwl = new ReentrantReadWriteLock();//读写锁
	public void get () {
		rwl.readLock().lock();//上一把读锁
		try {
			System.out.println(Thread.currentThread().getName() + ",have Read data");
			Thread.sleep((int)(Math.random()*1000));
			System.out.println(Thread.currentThread().getName() + ",have get data" + data);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			rwl.readLock().unlock();
		}
	}
	
	public void put (Object data) {
		try {
			this.rwl.writeLock().lock();
			System.out.println(Thread.currentThread().getName()+",hava write data");
			Thread.sleep((int)(Math.random()*1000));
			this.data = data;
			System.out.println(Thread.currentThread().getName()+",hava write data" + data);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			this.rwl.writeLock().unlock();
		}
	}
}
