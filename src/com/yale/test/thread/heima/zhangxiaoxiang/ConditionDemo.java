package com.yale.test.thread.heima.zhangxiaoxiang;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
 * 使用ReentrantLock比直接使用synchronized更安全，可以替代synchronized进行线程同步。
 * 但是，synchronized可以配合wait和notify实现线程在条件不满足时等待，条件满足时唤醒，用ReentrantLock我们怎么编写wait和notify的功能呢？
 * 答案是使用Condition对象来实现wait和notify的功能。
 * 我们仍然以TaskQueue为例，把前面用synchronized实现的功能通过ReentrantLock和Condition来实现：
 */
public class ConditionDemo {
	private final Lock lock = new ReentrantLock();
	//使用Condition时，引用的Condition对象必须从Lock实例的newCondition()返回，这样才能获得一个绑定了Lock实例的Condition实例。
	private final Condition condition = lock.newCondition();
	private Queue<String> queue = new LinkedList<>();
	
	public void addTask(String s) {
		lock.lock();
		try {
			queue.add(s);
			/*
			 * Condition提供的await()、signal()、signalAll()原理和synchronized锁对象的wait()、notify()、notifyAll()是一致的，并且其行为也是一样的：
			 * await()会释放当前锁，进入等待状态；
			 * signal()会唤醒某个等待线程；
			 * signalAll()会唤醒所有等待线程；
			 * 唤醒线程从await()返回后需要重新获得锁。
			 * 此外，和tryLock()类似，await()可以在等待指定时间后，如果还没有被其他线程通过signal()或signalAll()唤醒，可以自己醒来：
			 * 可见，使用Condition配合Lock，我们可以实现更灵活的线程同步。
			 */
			condition.signalAll();
		} finally {
			lock.unlock();
		}
	}
	
	public String getTask() throws InterruptedException {
		lock.lock();
		try {
			while(queue.isEmpty()) {
				condition.await();
			}
			return queue.remove();
		} finally {
			lock.unlock();
		}
	}
	
	public String getTaskTime() throws InterruptedException {
		lock.lock();
		try {
			while(queue.isEmpty()) {
				if (condition.await(1, TimeUnit.SECONDS)) {
					System.out.println("我是被其他线程唤醒的");
				} else {
					/* 疑问:如果是自己唤醒的话, 那这个线程有锁没有, 唤醒的过程时释放锁, 还是获取锁??
					 * 廖雪峰答:await()进去的时候释放锁，回来的时候获取锁，不管是别人唤醒的还是自己醒过来的
					 * 自己唤醒之后并不能立即执行代码,而是先要获取锁,就是先去抢占锁,拿到锁之后才能继续往下走
					 */
					System.out.println("我等待了1秒钟都没有被其他线程唤醒,然后我自己醒了.");
				}
			}
			return queue.remove();
		} finally {
			lock.unlock();
		}
	}
}
