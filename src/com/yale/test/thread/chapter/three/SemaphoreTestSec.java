package com.yale.test.thread.chapter.three;

import java.util.concurrent.Semaphore;

public class SemaphoreTestSec {
	private final int maxNum = 10;
	private final int [] numArr = new int[]{1,2,3,4,5,6,7,8,9,10};
	//创建10个计数信号量
	private final Semaphore semaphore = new Semaphore(maxNum,true);//true可以保证公平性,谁先来谁先得到
	private final boolean[] used = new boolean[maxNum];  
	
	public void putItem(int num){
		if (markAsUnused(num)) {
			semaphore.release();//释放一个信号计数
		}
	}
	
	public int getItem(){
		try {
			semaphore.acquire();//阻塞一个信号计数
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return getNextAvailableItem();
	}
	
	public synchronized int getNextAvailableItem() {
		for (int i=0;i<maxNum;i++) {
			if(!used[i]){//还没有被使用的元素
				used[i] = true;//将对应的元素标记为已使用
				return numArr[i];//返回这个元素
			}
		}
		return 0;
	}
	
	/**
	 * 将某个数标记为未使用
	 * @param num
	 * @return
	 */
	public synchronized boolean markAsUnused(int num){
		for (int i=0;i<numArr.length;i++){
			if (num == numArr[i]) {//如果这个数存在
				if (used[i]) {//并且已经被使用了
					used[i] = false;//那么就将它标记为未使用
					return true;//标记成功
				} else {
					return false;
				}
			}
		}
		return false;
	}
}
