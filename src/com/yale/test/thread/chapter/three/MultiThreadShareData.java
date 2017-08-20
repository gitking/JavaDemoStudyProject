package com.yale.test.thread.chapter.three;

/**
 * 设计4个线程,其中俩个线程每次对j增加1,另外俩个线程对j每次减少1
 * @author ylwangd
 */
public class MultiThreadShareData {
	public static void main(String[] args) {
		ShareData shareData1 = new ShareData();
		for (int i =0;i<2;i++) {//第一种方式
			new Thread(new MyRunnable(shareData1)).start();//加1
			new Thread(new MyRunnable2(shareData1)).start();//减1
		}
		//下面的是第二种方式
//		new Thread(new Runnable(){
//			public void run(){
//				shareData1.inrcement();
//			}
//		}).start();
//		new Thread(new Runnable(){
//			public void run(){
//				shareData1.inrcement();
//			}
//		}).start();
//		
//		new Thread(new Runnable(){
//			public void run(){
//				shareData1.decrement();
//			}
//		}).start();
//		new Thread(new Runnable(){
//			public void run(){
//				shareData1.decrement();
//			}
//		}).start();
	}
}

class MyRunnable implements Runnable {
	private ShareData shareData1;
	public MyRunnable (ShareData shareData1){
		this.shareData1 = shareData1;
	}
	public void run () {
		this.shareData1.inrcement();
	}
}

class MyRunnable2 implements Runnable {
	private ShareData shareData2;
	public MyRunnable2 (ShareData shareData2){
		this.shareData2 = shareData2;
	}
	public void run () {
		this.shareData2.decrement();
	}
}
class ShareData {
	int j;
	public synchronized void inrcement() {
		j++;
		System.out.println("加完后的值"+j);
	}
	public synchronized void decrement(){
		j--;
		System.out.println("减完后的值"+j);
	}
}
