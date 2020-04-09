package com.yale.test.thread.heima.zhangxiaoxiang;


public class TraditionalThreadSynchronized {

	public static void main(String[] args) {
		//外部类的静态方法里面不能new内部类的实例对象
		//final Outputer outputer = new Outputer();
		new TraditionalThreadSynchronized().init();
	}
	
	private void init(){
		final Outputer outputer = new Outputer();

		new Thread(new Runnable(){
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					outputer.output("zhangxiaoxiang");//尝试拿锁,拿到就执行,拿不到就等着,线程就进去阻塞状态
				}
			}
		}).start();
		
		new Thread(new Runnable(){
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					outputer.output("lihuoming");//尝试拿锁,拿到就执行,拿不到就等着,线程就进去阻塞状态
				}
			}
		}).start();
	}
	
	
	static class Outputer {
		//内部类的一个重要特征就是可以访问外部类的成员变量, 那就意味着外部类一定有了实例对象,就是说内部类只能访问外部类对象身上的成员变量
		//外部类的静态方法,不创建对象就可以调用静态方法
		//要想创建内部类的实例对象,必须先创建外部类的实例对象
		//不是静态内部类里面不能有静态方法,这个是为什么？静态内部类才能有静态方法
		//内部类和静态内部类的创建实例方法分别是什么？
		public void output(String name) {
			synchronized (this) {
				//synchronized关键字的作用就是给当前线程的这块代码上一把锁,代码运行完自动释放锁,锁没释放之前,别的线程进不来不能执行该代码.
				int len = name.length();
				for (int i =0; i< len; i++) {
					System.out.print(name.charAt(i));
				}
				System.out.println();
			}
		}
		
		public synchronized void output2(String name) {//这个等价于上面的synchronized (this)
			int len = name.length();
			for (int i =0; i< len; i++) {
				System.out.print(name.charAt(i));
			}
			System.out.println();
		}
		
		public static synchronized void output3(String name) {
			int len = name.length();
			for (int i =0; i< len; i++) {
				System.out.print(name.charAt(i));
			}
			System.out.println();
		}
	}
}
