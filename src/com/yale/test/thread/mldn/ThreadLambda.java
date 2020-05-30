package com.yale.test.thread.mldn;

public class ThreadLambda {
	public static void main(String[] args) {
		new Thread(()-> System.out.println("用Lambda表达式,实现Runnable接口")){}.start();
		new Thread(new Runnable(){
			@Override
			public void run() {
				System.out.println("匿名内部类,当线程调用start方法之后并不是立刻执行,而是进入到就绪状态,等待进行调度后执行,");
				System.out.println("需要将资源分配给你运行后才可以执行多线程中的代码(run()中的代码),当你执行了一段时间之后,你需要让出资源");
				System.out.println("让其它线程来继续执行,也就是说这个时候的run方法可能还没执行完呢，只执行了了一半,那么就要让出资源,");
				System.out.println("随后重新进入到就绪状态,重新等待分配新资源再继续执行");
				System.out.println("让其它线程来继续执行,也就是说这个时候的run方法可能还没执行完呢，只执行了了一半,");
			}
		}).start();
	}
}
