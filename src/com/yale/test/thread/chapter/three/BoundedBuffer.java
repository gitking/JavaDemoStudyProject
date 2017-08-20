package com.yale.test.thread.chapter.three;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class BoundedBuffer {
	private final Lock lock = new ReentrantLock();
	private final Condition notFull = lock.newCondition();
	private final Condition notEmpty = lock.newCondition();
	private final int[] numArr = new int[100];
	private int count,index,getIndex;
	
	public void put (int num){
		lock.lock();
		try {
			while(count == numArr.length){//数组满了,就让notFull进入阻塞状态
				notFull.await();
			}
			numArr[index] = num;
			index++;
			if (index == numArr.length) {
				index = 0;
			}
			count ++;
			notEmpty.signal();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
	
	public int get(){
		lock.lock();
		try {
			while(count == 0) {
				notEmpty.await();
			}
			int num = numArr[getIndex];
			getIndex ++;
			if (getIndex == numArr.length) {
				getIndex = 0;
			}
			count--;
			notFull.signal();
			return num;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
		return 0;
	}
}
