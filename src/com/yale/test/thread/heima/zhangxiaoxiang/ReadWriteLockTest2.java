package com.yale.test.thread.heima.zhangxiaoxiang;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.bouncycastle.util.Arrays;

/*
 * 前面讲到的ReentrantLock保证了只有一个线程可以执行临界区代码：
 * 但是有些时候，这种保护有点过头。因为我们发现，任何时刻，只允许一个线程修改，也就是调用inc()方法是必须获取锁，但是，get()方法只读取数据，不修改数据，它实际上允许多个线程同时调用。
 * 实际上我们想要的是：允许多个线程同时读，但只要有一个线程在写，其他线程就必须等待：
 * 		读		   写
	读	允许		不允许
	写	不允许	不允许
	使用ReadWriteLock可以解决这个问题，它保证：
 * 只允许一个线程写入（其他线程既不能写入也不能读取）；
 * 没有写入时，多个线程允许同时读（提高性能）。
 * 用ReadWriteLock实现这个功能十分容易。我们需要创建一个ReadWriteLock实例，然后分别获取读锁和写锁：
 * 把读写操作分别用读锁和写锁来加锁，在读取时，多个线程可以同时获得读锁，这样就大大提高了并发读的执行效率。
 * 使用ReadWriteLock时，适用条件是同一个数据，有大量线程读取，但仅有少数线程修改。
 * 例如，一个论坛的帖子，回复可以看做写入操作，它是不频繁的，但是，浏览可以看做读取操作，是非常频繁的，这种情况就可以使用ReadWriteLock。
 * 小结
 * 使用ReadWriteLock可以提高读取效率：
    ReadWriteLock只允许一个线程写入；
    ReadWriteLock允许多个线程在没有写入时同时读取；
    ReadWriteLock适合读多写少的场景。
 */
public class ReadWriteLockTest2 {
	private final ReadWriteLock rwlock = new ReentrantReadWriteLock();
	private final Lock rlock = rwlock.readLock();
	private final Lock wlock = rwlock.writeLock();
	private int[] counts = new int[10];
	
	
	public void inc(int index) {
		wlock.lock();//加写锁
		try{
			counts[index] +=1;
		} finally {
			wlock.unlock();//释放写锁
		}
	}
	
	public int[] get() {
		rlock.lock();//加读锁,我正在读的时候别人不能修改数据,我读完才能修改
		try {
			return Arrays.copyOf(counts, counts.length);
		} finally {
			rlock.unlock();//释放读锁
		}
	}
	public static void main(String[] args) {
		
	}
}
