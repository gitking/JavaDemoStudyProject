package com.yale.test.thread.chapter.one;

public class KeyPresonThread extends Thread {
	public KeyPresonThread (String name) {
		super(name);
	}
	public void run () {
		System.out.println(this.getName()+"程咬金开始战斗");
		for (int i=0;i<10;i++) {
			System.out.println(this.getName() + "程咬金上场攻击,[" + i + "]次");
		}
		System.out.println(this.getName() + "程咬金结束战斗");
	}
}
