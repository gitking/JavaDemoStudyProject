package com.yale.test.java.fanshe.perfma;


public class VolatileVisibilityTest {
	
	private static boolean initFlag = false;//initFlag这个变量不加volatile这个关键字,这个程序肯定会死循环。
	
	public static void main(String[] args) throws InterruptedException {
		
		new Thread(new Runnable(){
			@Override
			public void run(){
				System.out.println("waiting data...");
				while(!initFlag){
					/**
					 * 这个while循环里面加一个sleep或者加一个System.out.println都可以解决死循环的问题,即使initFlag这个变量不用volatile修饰也可以解决死循环的问题
					 * https://club.perfma.com/question/267086
					 * 按道理，子线程使用了system.out输出，system.out内部采用了syncronized同步，会触发num变量的主存同步，所以为什么还会出现死循环，感觉跟不同cpu mesi协议有关，可能mesi协议本事支持写入缓存未达到失效队列上限，所以导致主线程读取的num一直没有被失效
					 */
//					try {
//						Thread.sleep(1000);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
					//https://blog.csdn.net/C_AJing/article/details/103307797 System.out.println()对共享变量和多线程的影响，为什么会造成while循环终止
					//System.out.println("非常奇怪,加了这个System.out.println进行打印就不会出现死循环了,不加System.out.println就会死循环");
				}
				System.out.println("======================success");
			}
		}).start();
		
		Thread.sleep(5000);
		System.out.println("5秒休息好了");
		new Thread(new Runnable(){
			@Override
			public void run(){
				prepareData();
			}
		}).start();
	}
	
	
	public static void prepareData(){
		System.out.println("prepareing data...");
		initFlag = true;
		System.out.println("prepareing data end...");
	}
}
