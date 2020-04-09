package com.yale.test.thread.heima.zhangxiaoxiang;

public class TraditionalThread {

	public static void main(String[] args) {
		Thread thread = new Thread(){
			@Override
			public void run() {
				while(true) {
					try {
						Thread.sleep(500);//sleep是Thread的静态方法
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("1:" + Thread.currentThread().getName());
					System.out.println("2:" + this.getName());
				}
			}
		};
		thread.start();
		
		Thread thread02 = new Thread(new Runnable(){
			@Override
			public void run() {
				while(true) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("1:" + Thread.currentThread().getName());
					
					//这里就不能用this了,这里的this是代表runnable的实现类,而不是线程的对象,getName是Thread线程类里面的方法
					//System.out.println("2:" + this.getName());
				}
			}
		});
		thread02.start();
		
		/**
		 * 下面这段代码很有 意思,Thread构造方法里面传一个Runnable实现类,
		 * 然后Thread的匿名子类又重写了父类的Run方法,
		 * 最终会运行哪一个Run方法呢?
		 */
		new Thread(new Runnable(){
			@Override
			public void run() {
				System.out.println("你猜这段代码会运行哪个Run方法,Runnable的匿名实现类的run方法");
			}
		}){
			public void run() {
				System.out.println("你猜这段代码会运行哪个Run方法,thread子类的run方法");
			}
		}.start();;
	}
}
