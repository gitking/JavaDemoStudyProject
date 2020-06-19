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
		System.out.println("UUID类是根据你当前的地址和时间戳自动生成一个几乎不会重复的字符串,重复的几率大概是千万分之一");
		System.out.println("用雪花ID不会重复,自己百度");
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
