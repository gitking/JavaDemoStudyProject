package com.yale.test.thread.mldn;
/*
 * 死锁
 * 死锁发生后，没有任何机制能解除死锁，只能强制结束JVM进程。
 * 因此，在编写多线程应用时，要特别注意防止死锁。因为死锁一旦形成，就只能强制结束进程。
 * 那么我们应该如何避免死锁呢？答案是：线程获取锁的顺序要一致。即严格按照先获取lockA，再获取lockB的顺序，改写dec()方法如下：
 */
public class DeadLock2{
	public static final Object lockA = new Object();
	public static final Object lockB = new Object();
	
	/*
	 * 在获取多个锁的时候，不同线程获取多个不同对象的锁可能导致死锁。对于上述代码，线程1和线程2如果分别执行add()和dec()方法时：
	    线程1：进入add()，获得lockA；
	    线程2：进入dec()，获得lockB。
	随后：
	    线程1：准备获得lockB，失败，等待中；
	    线程2：准备获得lockA，失败，等待中。
	 * 此时，两个线程各自持有不同的锁，然后各自试图获取对方手里的锁，造成了双方无限等待下去，这就是死锁。
	 * 那么我们应该如何避免死锁呢？答案是：线程获取锁的顺序要一致。即严格按照先获取lockA，再获取lockB的顺序，改写dec()方法如下：
	 */
	public void add (int m) {
		synchronized (lockA) {//获得lockA的锁
			m = m + 1;
			synchronized (lockB) {//获得lockB的锁
				m = m + 100;
			}//释放lockB的锁
		}//释放lockA的锁
	}
	
	public void dec (int m) {
		synchronized (lockB) {//获得lockB的锁
			m = m + 1;
			synchronized (lockA) {//获得lockA的锁
				m = m + 100;
			}//释放lockA的锁
		}//释放lockB的锁
	}
}
