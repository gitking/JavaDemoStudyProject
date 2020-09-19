package com.yale.test.thread.mldn;

/**
 * Java的线程锁是可重入的锁。
 * 什么是可重入的锁？我们还是来看例子：
 * @author dell
 */
public class SynchronizedDemo3 {
	public static void main(String[] args) throws InterruptedException {
		int tem = 1;
		int test = -tem;
		System.out.println(test);
		if (test > 0) {
			System.out.println("test是正数");
		} else {
			System.out.println("test是负数");
		}
	}
	
	private int count = 0;
	/*
	 * 观察synchronized修饰的add()方法，一旦线程执行到add()方法内部，说明它已经获取了当前实例的this锁。如果传入的n < 0，将在add()方法内部调用dec()方法。由于dec()方法也需要获取this锁，现在问题来了：
	 * 对同一个线程，能否在获取到锁以后继续获取同一个锁？
	 * 答案是肯定的。JVM允许同一个线程重复获取同一个锁，这种能被同一个线程反复获取的锁，就叫做可重入锁。
	 * 由于Java的线程锁是可重入锁，所以，获取锁的时候，不但要判断是否是第一次获取，还要记录这是第几次获取。每获取一次锁，记录+1，每退出synchronized块，记录-1，减到0的时候，才会真正释放锁。
	 */
	public synchronized void add (int n) {
		if (n <0) {
			dec(-n);
		} else {
			count+=n;
		}
	}
	
	public synchronized void dec(int n) {
		count +=n;
	}
}
