package com.yale.test.java.fanshe.perfma;

public class VolatileAtomicTest {
	public static volatile int num = 0;
	
	public static synchronized void increase() {//把synchronized去掉,运行结果就不是1万了,.结果总是小于等于1万。
		num++;
	}
	
	public static void main(String[] args) throws InterruptedException {
		Thread[] threads = new Thread[10];
		for (int i=0; i<threads.length; i++){
			threads[i] = new Thread(new Runnable(){
				@Override
				public void run(){
					for(int i=0;i<1000;i++) {
						increase();
					}
				}
			});
			threads[i].start();
		}
		for (Thread t : threads) {
			t.join();
		}
		
		System.out.println("num的最终结果是多少？" + num);
	}
}
