package com.yale.test.thread.mldn;

/*
 * https://blog.csdn.net/qq_39682377/article/details/81449451
 * https://www.liaoxuefeng.com/wiki/1252599548343744/1306580767211554
 * 中断线程
 * 如果线程需要执行一个长时间任务，就可能需要能中断线程。中断线程就是其他线程注意是其他线程给该线程发一个信号，该线程收到信号后结束执行run()方法，使得自身线程能立刻结束运行。
 * 我们举个栗子：假设从网络下载一个100M的文件，如果网速很慢，用户等得不耐烦，就可能在下载过程中点“取消”，这时，程序就需要中断下载线程的执行。
 * 中断一个线程非常简单，只需要在其他线程中对目标线程调用interrupt()方法，目标线程需要反复检测自身状态是否是interrupted状态，如果是，就立刻结束运行。
 * 我们还是看示例代码：
 * 小结
 * 对目标线程调用interrupt()方法可以请求中断一个线程，目标线程通过检测isInterrupted()标志获取自身是否已中断。如果目标线程处于等待状态，该线程会捕获到InterruptedException；
 * 目标线程检测到isInterrupted()为true或者捕获了InterruptedException都应该立刻结束自身线程；
 * 通过标志位判断需要正确使用volatile关键字；
 * volatile关键字解决了共享变量在线程间的可见性问题。
 * 
 * https://www.liaoxuefeng.com/wiki/1252599548343744/1306580767211554
 * 失眠是因为闲  评论: 那么中断线程用哪个方法好呢？interrupt()还是running？
 * 廖雪峰答：实际情况是线程要自己根据条件1、条件2、条件3决定是否退出：interrupt一般用于清理，但很多程序都是直接kill退出，懒得清理
 * 廖雪峰这里说的一般用于清理，但很多程序都是直接kill退出，懒得清理。这句话的意思是,interrupt一般用于你要关闭服务器时,如果要通知线程结束,然后等待所有线程都结束,再关闭服务器。
 * 但是大多数人,都是直接关闭服务器的,不会通知线程结束的。
 * 
 * 廖雪峰答:线程退出就是run()方法执行完毕，至于内部捕获InterruptedException跟普通代码是一样的。
 * 
 * young233_54314问: 中断这个词真的不好。还没看文章之前我还以为是计算机组成原理/操作系统 里面讲的中断。 仔细一个原来是 中止的意思
 * https://tse4-mm.cn.bing.net/th/id/OIP.NXZbsGumE1saNcrt8AaCWgAAAA?pid=Api&rs=1     
 * 廖雪峰答:那你理解错了，interrupt真的是中断，因为线程可以选择不终止。
 */
public class ThreadInterruptedDemo3 {
	public static void main(String[] args) throws InterruptedException {
		/*
		 * 如果线程处于等待状态，例如，t.join()会让main线程进入等待状态，此时，如果对main线程调用interrupt()，
		 * join()方法会立刻抛出InterruptedException，因此，目标线程只要捕获到join()方法抛出的InterruptedException，
		 * 就说明有其他线程对其调用了interrupt()方法，通常情况下该线程应该立刻结束运行。
		 * 我们来看下面的示例代码：
		 * main线程通过调用t.interrupt()从而通知t线程中断，而此时t线程正位于hello.join()的等待中，此方法会立刻结束等待并抛出InterruptedException。
		 * 由于我们在t线程中捕获了InterruptedException，因此，就可以准备结束该线程。在t线程结束前，对hello线程也进行了interrupt()调用通知其中断。
		 * 如果去掉这一行代码，可以发现hello线程仍然会继续运行，且JVM不会退出。
		 */
		Thread t = new MyThreadInterruptedException();
		t.start();
		Thread.sleep(1000);
		t.interrupt();//中断线程
		t.join();//等待t线程结束
		System.out.println("演示结束");
		
		/*
		 * 另一个常用的中断线程的方法是设置标志位。我们通常会用一个running标志位来标识线程是否应该继续运行，在外部线程中，通过把HelloThread.running置为false，就可以让线程结束：
		 */
	}
}

class MyThreadInterruptedException extends Thread {
	@Override
	public void run() {
//		try {
		/*
		 * https://www.liaoxuefeng.com/wiki/1252599548343744/1306580767211554
		 * 下面的 wlwheart 评论说:若线程在阻塞状态时，调用了它的interrupt()方法，那么它的“中断状态”会被清除并且会收到一个InterruptedException异常。
		 * 这里有一个很有意思的问题sleep方法抛出的是InterruptedException异常,wait方法抛出的是IllegalMonitorStateException,值得思考一下
		 */
//			Thread.sleep(100000);
//		} catch (IllegalMonitorStateException e) {
//			System.out.println("IllegalMonitorStateException是运行时异常,不需要捕获的");
//			e.printStackTrace();
//		} catch (InterruptedException e) {
//			System.out.println("InterruptedException是正常异常,必须要被捕获的");
//			e.printStackTrace();
//		}
//		try {
			/*
			 * https://www.liaoxuefeng.com/wiki/1252599548343744/1306580767211554
			 * 下面的 wlwheart 评论说:若线程在阻塞状态时，调用了它的interrupt()方法，那么它的“中断状态”会被清除并且会收到一个InterruptedException异常。
			 * 这里有一个很有意思的问题sleep方法抛出的是InterruptedException异常,wait方法抛出的是IllegalMonitorStateException,值得思考一下
			 */
//			this.wait(10000000);
//		} catch (IllegalMonitorStateException e) {
//			System.out.println("IllegalMonitorStateException是运行时异常,不需要捕获的");
//			e.printStackTrace();
//		} catch (InterruptedException e) {
//			System.out.println("InterruptedException是正常异常,必须要被捕获的");
//			e.printStackTrace();
//		}
		Thread hello = new HelloThread();
		hello.start();
		try {
			hello.join();
		} catch (InterruptedException e) {
			System.out.println("拦截到InterruptedException异常,我被中断了,interrupted!");
		}
		hello.interrupt();
	}
}

class HelloThread extends Thread {
	@Override
	public void run() {
		int n =0;
		while (!isInterrupted()) {
			n++;
			System.out.println(n + "hello!");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				System.out.println("拦截到InterruptedException异常,这里会抛出异常吗??");
				break;
			}
		}
	}
}
