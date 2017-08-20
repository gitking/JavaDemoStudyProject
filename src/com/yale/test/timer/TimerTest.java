package com.yale.test.timer;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TimerTest {
	private static int count = 0;
	public static void main(String[] args) {
		/*new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				System.out.println("booming!");
			}
		},1000,3000);*///第一次1秒钟后执行,然后每隔3秒再执行一次
		
		
		class MyTimerTask extends TimerTask {
			public void run(){//先2秒后4秒然后反复循环
				count = (count + 1) % 2;
				System.out.println("booming");
				new Timer().schedule(new MyTimerTask(),	 2000 + 2000*count);
			}
		}
		
		new Timer().schedule(new MyTimerTask(), 2000);
		
		while (true) {
			System.out.println(new Date().getSeconds());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
