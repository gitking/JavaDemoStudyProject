package com.yale.test.timer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

class MyTimerTask extends TimerTask {
	public void run () {
		System.out.println("通过查看TimerTask类,可以知道TimerTask实际上是个抽象类,实现了runnable接口");
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
		UUID uuid = UUID.randomUUID();
		System.out.println("随机生成唯一ID:" + uuid.toString());
	}
}

public class TimerTest2 {
	public static void main(String[] args) {
		Timer timer = new Timer();
		//1000毫秒后开始执行,然后每隔2000毫秒执行一下
		timer.schedule(new MyTimerTask(), 1000, 2000);
	}
}
