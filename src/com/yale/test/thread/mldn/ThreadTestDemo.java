package com.yale.test.thread.mldn;
/**
 * java的源码在jdk的安装目录里面,D:\jdk1.8_251\src.zip,但是你要用jdk的exe文件安装才有这个Src文件
 * 不过sun包下面的代码都不在src.zip里,sun包源码来这里看https://hg.openjdk.java.net/jdk8u/jdk8u/jdk/file/63cecc0bd71d/src/share/classes/sun/nio/cs/ArrayDecoder.java
 * 一般来讲安装完JDK后,在JDK的根目录下会有一个叫"src.zip"的压缩包,解压后是一个目录,其中包含了常见的Java源码,但是以sun开头的源码是找不到的。
 * 你可以通过一些反编译手段得到,也可在OpenJDK上找到一些源码.
 * @author dell
 */
class MyThread extends Thread {
	private String title;
	public MyThread(String title) {
		this.title = title;
	}
	public void run() {
		for (int x=0; x<10; x++) {
			System.out.println("currentThread取得当前线程对象" + Thread.currentThread().getName());
			System.out.println(Thread.currentThread().getName() + "-->" + this.title + ".x = " + x);
			try {
				Thread.sleep(1000);
				//Thread.yield();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
public class ThreadTestDemo {
	public static void main(String[] args) {
		System.out.println("多线程的主要操作方法都在Thread类里面");

		MyThread mt1 = new MyThread("线程A");
		mt1.setName("我是线程的名字01");
		System.out.println("我是线程的名字01,如果线程你没有设置线程的名字,则会自动分配一个线程的名字,但是请特别注意,线程名字如果要设置,一定不要设置重复的线程名字,并且中间不要修改线程的名字:");
		MyThread mt2 = new MyThread("线程B");
		MyThread mt3 = new MyThread("线程C");
		mt1.start();
		mt2.start();
		mt3.start();
		
		Thread mt4 = new Thread(mt3, "我是线程名字02");
		System.out.println("通过线程的构造方法也可以给线程设置名字");
		mt4.start();
		System.out.println("线程休眠指的是让线程暂缓执行一下,等到了预计的时间之后再恢复执行sleep");
		
		System.out.println("看文档看sleep和yield的区别,首先这俩个都是native本地方法");
		
		/**
		 * jdk官方不建议使用此方法,以下来自yield方法的注释翻译
		 * yield是启发式尝试，旨在改善相对进展否则会过度使用CPU的线程之间。 使用应该将其与详细的性能分析和基准测试结合起来，以确保它实际上具有所需的效果。
		 * 很少适合使用此方法。 它可能对调试或测试有用，可能有助于重现比赛条件引起的错误。 在设计时也可能有用并发控制结构，例如{@link java.util.concurrent.locks} package.
		 * https://club.perfma.com/article/297124
		 */
		System.out.println("yield:向调度程序提示当前线程愿意让步当前使用的处理器。 调度程序可以随意忽略此提示。");
		
		System.out.println("所谓的线程优先级指的优先级越高越有可能先执行,但仅仅是有可能而已。");
		System.out.println("在Thread类里面提供有以下的优先级操作方法:");
		/**
		 * 设置优先级:public final void setPriority(int newPriority)
		 * 取得优先级:public final int getPriority()
		 * 最高优先级:public static final int MAX_PRIORITY,10;
		 * 中等优先级:public static final int NORM_PRIORITY,5;
		 * 最低优先级:public static final int MIN_PRIORITY,1;
		 */
		System.out.println("main方法的优先级为:中等优先级(5)" + Thread.currentThread().getPriority());
	}
}
