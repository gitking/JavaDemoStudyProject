package com.yale.test.math.array;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 使用队列实现生产者消费者模式
 * @author dell
 */
public class ProviderConsumer {

	public static void main(String[] args) throws InterruptedException {
		DataQueue data = new DataQueue();
		Thread dp = new Thread(new DataProvider(data));
		Thread dc = new Thread(new DataConsumer(data));
		dp.start();
		dc.start();
	}
}

class DataProvider implements Runnable{
	private DataQueue data;
	public DataProvider(DataQueue data) {
		this.data = data;
	}
	
	@Override
	public void run() {
		for (int x=0; x<5000; x++) {
			if (x % 2 == 0) {
				this.data.product("老李", "是个好人");
			} else {
				this.data.product("民族败类", "老方B");
			}
		}
		System.out.println("数据我生产完了");
	}
}

class DataConsumer implements Runnable {
	private DataQueue data;
	public DataConsumer(DataQueue data) {
		this.data = data;
	}
	@Override
	public void run() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for (int x=0; x<500000; x++) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.data.consumer();//消费数据
		}
	}
}

class DataQueue {
	private class Data {//负责数据保存
		private String title;
		private String note;
		@Override
		public String toString() {
			return "title = " + this.title + ",note = " + this.note;
		}
		
		public synchronized void set (String title, String note) {
			this.title = title;
			this.note = note;
		}
		
		public synchronized Data get() {
			return this;
		}
	}
	
	//flag = true,表示允许生产,但是不允许消费者取走
	//flag = false, 表示生产完毕,允许消费者取走,但是不允许生产
	private boolean flag = true;
	
	private Queue<Data> queue = new LinkedList<Data>();
	
	public synchronized void product(String title, String note) {
		Data data = new Data();
		data.set(title, note);
		this.queue.add(data);//一直存放数据
	}
	
	public synchronized void consumer () {//取走数据
		Data data = this.queue.poll();
		if (data == null) {
			try {
				System.out.println("数据没了,我先休息2秒,等生产者生产一会我再来取数据");
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
//			try {
//				super.wait(2000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
		} else {
			System.out.println(data.get());
		}
	}
}