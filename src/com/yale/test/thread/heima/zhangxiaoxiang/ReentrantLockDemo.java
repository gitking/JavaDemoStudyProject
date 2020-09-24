package com.yale.test.thread.heima.zhangxiaoxiang;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
 * 从Java 5开始，引入了一个高级的处理并发的java.util.concurrent包，它提供了大量更高级的并发功能，能大大简化多线程程序的编写。
 * 我们知道Java语言直接提供了synchronized关键字用于加锁，但这种锁一是很重，二是获取时必须一直等待，没有额外的尝试机制。
 * java.util.concurrent.locks包提供的ReentrantLock用于替代synchronized加锁，我们来看一下传统的synchronized代码：
 * 顾名思义，ReentrantLock是可重入锁，它和synchronized一样，一个线程可以多次获取同一个锁
 * 和synchronized不同的是，ReentrantLock可以尝试获取锁：
 * 使用ReentrantLock比直接使用synchronized更安全，可以替代synchronized进行线程同步。
 */
public class ReentrantLockDemo {
	private int count;
	public final Lock lock = new ReentrantLock();
	public void add(int n) {
		lock.lock();
		try {//必须先获取到锁，再进入try {...}代码块，最后使用finally保证释放锁；
			count +=n;
		} finally {
			//因为synchronized是Java语言层面提供的语法，所以我们不需要考虑异常，而ReentrantLock是Java代码实现的锁，我们就必须先获取锁，然后在finally中正确释放锁。
			lock.unlock();
		}
	}
	public void addTry(int n) throws InterruptedException {
		/*
		 * 下面代码在尝试获取锁的时候，最多等待1秒。如果1秒后仍未获取到锁，tryLock()返回false，程序就可以做一些额外处理，而不是无限等待下去。
		 * 所以，使用ReentrantLock比直接使用synchronized更安全，线程在tryLock()失败的时候不会导致死锁。
		 */
		if(lock.tryLock(1, TimeUnit.SECONDS)) {
			try {
				count +=n;
			} finally {
				//因为synchronized是Java语言层面提供的语法，所以我们不需要考虑异常，而ReentrantLock是Java代码实现的锁，我们就必须先获取锁，然后在finally中正确释放锁。
				lock.unlock();
			}
		}
	}
}
