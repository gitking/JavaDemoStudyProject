package com.yale.test.thread.mldn;

public class ProviderConsumer {

	public static void main(String[] args) {
		Data data = new Data();
		new Thread(new DataProvider(data)).start();
		new Thread(new DataConsumer(data)).start();
	}
}

class DataProvider implements Runnable{
	private Data data;
	public DataProvider(Data data) {
		this.data = data;
	}
	
	@Override
	public void run() {
		for (int x=0; x<50; x++) {
			if (x % 2 == 0) {
				this.data.set("老李", "是个好人");
			} else {
				this.data.set("民族败类", "老方B");
			}
		}
	}
}

class DataConsumer implements Runnable {
	private Data data;
	public DataConsumer(Data data) {
		this.data = data;
	}
	@Override
	public void run() {
		for (int x=0; x<50; x++) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.data.get();
		}
	}
}
class Data {//负责数据保存
	private String title;
	private String note;
	//flag = true,表示允许生产,但是不允许消费者取走
	//flag = false, 表示生产完毕,允许消费者取走,但是不允许生产
	private boolean flag = true;
	public synchronized void get() {
		if (flag) {//不能消费数据了,需要等待生产者生产数据
			try {
				/**
				 * sleep()和wait()方法的区别
				 * sleep()方法是Thread类中定义的方法,到了一定的时间后该休眠的线程可以自动唤醒;不会失去锁对象,这个看源码的注释就知道了,sleep不会释放锁
				 * wait()方法是Object类中定义的方法,如果要想唤醒,必须使用notify()或者notifyAll()方法才可以唤醒.wait方法会释放锁,看源码注释就知道了
				 * 即使wait(1000)设置了时间,时间到了也是自动调用notify()方法唤醒线程的.
				 * yield:yield是Thread类中的方法,意思向调度程序提示当前线程愿意让步当前使用的处理器。 调度程序可以随意忽略此提示。会释放锁,看源码注释就知道了
				 */
				this.wait();//wait()方法是死等,wait(1000);是等待一定的时间,时间单位是毫秒,wait(long timeout, int nanos)nanos是纳秒的意思,自己看源码把
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		try {
			/**
			 * 根据系统计时器和调度程序的精度和准确性，使当前正在执行的线程进入休眠状态（暂时停止执行）达指定的毫秒数。 线程不会失去任何监视器的所有权。
			 */
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(this.title + " = " + this.note);
		flag = true;//表示已经消费过了,可以生产数据了
		/**
		 * notify();//随机唤醒一个等待线程
		 * notifyAll()唤醒所有当前对象的等待线程,哪个优先级高,哪个比较有可能先执行
		 */
		this.notify();//随机唤醒一个等待线程
	}
	
	public synchronized void set(String title, String note) {
		if (!flag) {//不能生产数据,需要等待
			try {
				this.wait();//wait()方法是死等,wait(1000);是等待一定的时间,时间单位是毫秒
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.title = title;
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.note = note;
		this.flag = false;//表示不再生产数据,可以消费数据了
		this.notify();//随机唤醒一个等待线程
	}
}