package com.yale.test.thread;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Scanner;

class ThreadThird implements Runnable {
	private int count = 0;
	private String separatorStr = File.separator;//盘符
	private String lineChange = System.getProperty("line.separator");
	public void run () {
		try {
			this.wirteFile();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	void wirteFile () throws IOException, InterruptedException {
		File fileName = new File("d:"+this.separatorStr+"JavaCreateFile.txt");
		OutputStream outputStream = new FileOutputStream(fileName,true);//追加内容
		while (this.count < 999) {
			outputStream.write(("word"+this.count+++this.lineChange).getBytes());
			System.out.println("线程:"+Thread.currentThread().getName()+",成功写入一个数字");
			Thread.sleep(1000);
		}
		outputStream.close();
	}
}
public class DameonThread {
	public static void main (String [] args) {
		System.out.println("主线程"+Thread.currentThread().getName()+"开始运行");
		ThreadThird threadThird = new ThreadThird();
		Thread m1 = new Thread(threadThird,"writeThread");
		m1.setDaemon(true);//设置为守护线程
		m1.start();
		Scanner sc = new Scanner(System.in);
		sc.next();//主线程一旦结束,守护线程就跟着结束了
		System.out.println("主线程"+Thread.currentThread().getName()+"结束");
	}
}
