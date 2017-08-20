package com.yale.test.thread.chapter.three;

class Message {
	private String note;

	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
}

class MessageConsumer {
	public void print() {
		System.out.println(Thread.currentThread().getName() + ":" + MessageUtil.msg.getNote());
	}
}

class MessageUtil {
	public static Message msg;
}
public class ThreadLocalTest {
	public static void main(String[] args) {
		System.out.println("这个类打印出来有问题,请看ThreadLocalTest2.java的解决办法");
		new Thread(()->{
			Message msg = new Message();
			msg.setNote("www.mldn.cn");
			System.out.println(Thread.currentThread().getName() + "我先执行Begin");
			MessageUtil.msg = msg;
			System.out.println(Thread.currentThread().getName() + "我先执行");
			MessageConsumer mc = new MessageConsumer();
			mc.print();
		},"线程A").start();
		
		new Thread(()->{
			Message msg = new Message();
			msg.setNote("我明天要去练车");
			System.out.println(Thread.currentThread().getName() + "我先执行Begin");
			MessageUtil.msg = msg;
			System.out.println(Thread.currentThread().getName() + "我先执行");
			MessageConsumer mc = new MessageConsumer();
			mc.print();
		},"线程B").start();
	}
}
