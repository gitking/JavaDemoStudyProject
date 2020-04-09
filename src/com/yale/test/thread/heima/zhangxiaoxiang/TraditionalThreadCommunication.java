package com.yale.test.thread.heima.zhangxiaoxiang;

public class TraditionalThreadCommunication {

	public static void main(String[] args) {
		
		/**
		 * 子线程循环10次，接着主线程循环100次,接着又回到子线程循环10次,接着回到主线程又循环100次,如此循环50次
		 * 传智播客_张孝祥_传统线程同步通信技术 第四集
		 */
		final Business business = new Business();
		new Thread(new Runnable(){
			@Override
			public void run() {
				for (int i=0;i<=50;i++) {
						business.sub(i);
				}
			}
		}).start();
		
		for (int i=0;i<=50;i++) {
			business.main(i);
		}
	}
}

class Business {//这个不是内部类,这个是一个单独的类，那个这个类的修饰类型是public吗？
	private boolean bShouldSub = true;
	public synchronized void sub(int i) {
		while (!bShouldSub) {//这里不能用if,这里可以看Object里面的wait方法,例子.java官方API就是推荐使用while判断,防止虚假唤醒
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		for (int j=1; j<=10; j++) {
			System.out.println("sub thread sequence of "+ j + " loop of" + i);
		}
		bShouldSub = false;
		this.notify();
	}
	
	public synchronized void main(int i){
		while (bShouldSub) {
			try {
				this.wait();
			} catch(InterruptedException e){
				e.printStackTrace();
			}
		}
		for (int j=0; j<= 100;j ++){
			System.out.println("main thread sequence of " + j + " loop of" + i);
		}
		bShouldSub = true;
		this.notify();
	}
}
