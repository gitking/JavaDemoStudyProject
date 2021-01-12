package com.yale.test.java.fanshe.perfma;

public class VolatileBarrierExample2 {
	private static boolean stop = false;
	private static volatile int i = 0;
	public static void main(String[] args) throws InterruptedException {
		Thread thread = new Thread(new Runnable(){
			@Override
			public void run(){
				while(!stop){
					i++;//注意这个i是全局变量
				}
			}
		});
		
		thread.start();//启动线程
		Thread.sleep(1000);//Main线程休眠1秒
		stop= true;//这个可以正常结束循环,VolatileBarrierExample01不会正常结束循环
		System.out.println("Main线程已经把stop修改成true了,子线程的循环会结束吗?");
		thread.join();
	}
}
