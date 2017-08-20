package com.yale.test.thread.chapter.three;

class Message2 {
	private String note;

	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
}

class MessageConsumer2 {
	public void print() {
		System.out.println(Thread.currentThread().getName() + ":" + MessageUtil2.get().getNote());
	}
}

class MessageUtil2 {
	public static ThreadLocal<Message2> msgThr = new ThreadLocal<Message2>();
	public static void set (Message2 msg) {
		msgThr.set(msg);
	}
	public static Message2 get () {
		return msgThr.get();
	}
}
public class ThreadLocalTest2 {
	public static void main(String[] args) {
		
		System.out.println("ThreadLocal可以帮助我们减少一些重要的引用传递,ThreadLocal能保证线程只能取自己设置进去的数据,一个线程只能存一个取一个");
		new Thread(()->{
			Message2 msg = new Message2();
			msg.setNote("www.mldn.cn");
			System.out.println(Thread.currentThread().getName() + "我先执行Begin");
			MessageUtil2.set(msg);
			System.out.println(Thread.currentThread().getName() + "我先执行");
			MessageConsumer2 mc = new MessageConsumer2();
			mc.print();
		},"线程A").start();
		
		new Thread(()->{
			Message2 msg = new Message2();
			msg.setNote("我明天要去练车");
			System.out.println(Thread.currentThread().getName() + "我先执行Begin");
			MessageUtil2.set(msg);
			System.out.println(Thread.currentThread().getName() + "我先执行");
			MessageConsumer2 mc = new MessageConsumer2();
			mc.print();
		},"线程B").start();
	}
}
