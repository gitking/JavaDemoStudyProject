package com.yale.test.thread.chapter.one;

public class ArmThread implements Runnable {
	
	volatile boolean keepAttack = true;//volatile 保证读写同步
	
	@Override
	public void run() {
		String threadName = Thread.currentThread().getName();
		while (this.keepAttack) {
			for (int i=0; i<5; i++) {
				System.out.println(threadName + ",攻击对方第[" + i + "]次");
				Thread.yield();//主动让出处理器资源
			}
		}
		System.out.println(threadName + ",结束了战斗");
	}
}
