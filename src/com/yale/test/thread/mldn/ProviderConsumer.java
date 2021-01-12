package com.yale.test.thread.mldn;

public class ProviderConsumer {

	public static void main(String[] args) throws InterruptedException {
		Data data = new Data();
		Thread dp = new Thread(new DataProvider(data));
		Thread dc = new Thread(new DataConsumer(data));
		dp.start();
		dc.start();
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
		while (flag) {//不能消费数据了,需要等待生产者生产数据
			try {
				/**
				 * 操作系统中,CPU竞争有很多种策略.Unix系统使用的是时间片算法,而windows则属于抢占式的.
				 * 在时间片算法中，所有的进程排成一个队列。操作系统按照他们的顺序，给每个进程分配一段时间，即该进程允许运行的时间。如果在时间片结束时进程还在运行，则CPU将被剥夺并分配给另一个进程。如果进程在时间片结束前阻塞或结束，则CPU当即进行切换。调度程 序所要做的就是维护一张就绪进程列表，当进程用完它的时间片后，它被移到队列的末尾。
				 * 所谓抢占式操作系统，就是说如果一个进程得到了 CPU 时间，除非它自己放弃使用 CPU ，否则将完全霸占 CPU 。因此可以看出，在抢 占式操作系统中，操作系统假设所有的进程都是“人品很好”的，会主动退出 CPU 。
				 * 在抢占式操作系统中，假设有若干进程，操作系统会根据他们的优先级、饥饿时间（已经多长时间没有使用过 CPU 了），给他们算出一 个总的优先级来。操作系统就会把 CPU 交给总优先级最高的这个进程。当进程执行完毕或者自己主动挂起后，操作系统就会重新计算一 次所有进程的总优先级，然后再挑一个优先级最高的把 CPU 控制权交给他。
				 * sleep()和wait()方法的区别
				 * 线程的sleep()这个方法能够保证一件事:在指定的沉睡时间之前,昏睡中的线程一定不会被唤醒。举例来说,
				 * 如果你要求线程去睡2000个毫秒,至少要等俩秒过后它才会继续地执行。
				 * sleep()方法是Thread类中定义的方法,到了一定的时间后该休眠的线程可以自动唤醒;不会失去锁对象,这个看源码的注释就知道了,sleep不会释放锁,但会失去CPU的资源
				 * 在线程中，调用sleep（0）可以释放cpu时间，让线程马上重新回到就绪队列而非等待队列，sleep(0)释放当前线程所剩余的时间片（如果有剩余的话），这样可以让操作系统切换其他线程来执行，提升效率。
				 * 在线程没退出之前，线程有三个状态，就绪态，运行态，等待态。sleep(n)之所以在n秒内不会参与CPU竞争，是因为，当线程调用sleep(n)的时候，线程是由运行态转入等待态，线程被放入等待队列中，等待定时器n秒后的中断事件，当到达n秒计时后，线程才重新由等待态转入就绪态，被放入就绪队列中，等待队列中的线程是不参与cpu竞争的，只有就绪队列中的线程才会参与cpu竞争，所谓的cpu调度，就是根据一定的算法（优先级，FIFO等。。。），从就绪队列中选择一个线程来分配cpu时间。
				 * 而sleep(0)之所以马上回去参与cpu竞争，是因为调用sleep(0)后，因为0的原因，线程直接回到就绪队列，而非进入等待队列，只要进入就绪队列，那么它就参与cpu竞争。 
				 * wait()方法是Object类中定义的方法,如果要想唤醒,必须使用notify()或者notifyAll()方法才可以唤醒.wait方法会释放锁,看源码注释就知道了
				 * 即使wait(1000)设置了时间,时间到了也是自动调用notify()方法唤醒线程的.wait被唤醒之后要重新抢占锁资源,wait存在虚假唤醒的可能,应该使用while(){}来判断
				 * yield:yield是Thread类中的方法,意思向调度程序提示当前线程愿意让步当前使用的处理器。 调度程序可以随意忽略此提示。会释放锁,看源码注释就知道了
				 * 总结:wait()方法是将自身线程放入等待池中(变为等待状态),并释放锁,释放CPU资源.调用wait方法的线程，需要线程调用 notify / notifyAll 方法唤醒等待池中的所有线程，才会进入就绪队列中等待系统分配资源。
				 * 	       而wait，notify，notifyAll只能在同步控制方法或者同步控制块中使用.否则会
				 * 	       报错java.lang.IllegalMonitorStateException,sleep必须捕获异常，而wait，notify，notifyAll的不需要捕获异常。
				 *    sleep()方法不会释放锁(就是系统资源,所以sleep时间到了之后才会立刻就是就绪状态),但会释放CPU资源,sleep在休眠指定时间之后,线程立即恢复为就绪状态,而不是等待状态.sleep可以在任何地方使用.
				 *    sleep方法会自动唤醒，如果时间不到，想要唤醒，可以使用interrupt方法强行打断。
				 */
				this.wait(10000000);//wait()方法是调用的是wait(0)方法,wait(1000);是等待一定的时间,时间单位是毫秒,wait(long timeout, int nanos)nanos是纳秒的意思,自己看源码把
				/**
				 * 你注意观察窗,线程被唤醒之后,下面的这些代码不会走了,因为被唤醒之后flag就是false了,
				 */
				System.out.println("wait()调用的是wait(0)方法,看源码注释和源码就只知道了");
				System.out.println("wait()调用的是wait(0)方法,当参数为0时,就不在考虑实时,直到等待通知,看源码注释,wait()其实就是死等");
				System.out.println("interrupted()是静态方法,线程是否中断状态:" + Thread.interrupted());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		//Thread.currentThread().interrupt();//中断线程,将线程设置为中断状态,仅仅是设置一个中断标志,线程实际上还在运行,可以打断sleep

		/**
		 * isInterrupted(true),参数传true的意思是：先返回线程当前是否处于中断状态后再清除中断状态。这样描述比较正确。
		 * 终于搞明白了, isInterrupted(true) 这个方法实际上干了俩件事情, 第一件事情是先获取线程的状态, 第二件事情是清除线程的中断状态。
		 * isInterrupted(false),参数传false的意思是：不清除线程状态,并返回线程状态。
		 * isInterrupted()这个线程的对象方法就是判断线程是否被标记为中断状态,返回结果为true代表 线程已被中断.作用是只测试此线程是否被中断 ，不清除中断状态。
		 * isInterrupted()方法的功能是:测试此线程是否已被中断。注意是测试此线程(不是当前线程),isInterrupted()这个方法会调用自己的重载方法isInterrupted(false)
		 * 并且参数传的是false.而isInterrupted(boolean ClearInterrupted)方法的意思是：测试线程是否被中断,判断标准是ClearInterrupted的值是否被重置.
		 * 线程的中断状态不受此方法(isInterrupted(false))的影响,由于此方法(isInterrupted(false))返回false,
		 * 因此将反映出由于中断时某个线程未处于活动状态而忽略的线程中断。
		 * 总结isInterrupted()或者isInterrupted(false)这俩个方法其实就是测试此线程是否被中断 ，不清除中断状态。
		 * https://blog.csdn.net/qq_39682377/article/details/81449451
		 */
		System.out.println("isInterrupted()是对象方法,线程是否中断状态:" + Thread.currentThread().isInterrupted());
		
		/**
		 * isInterrupted(true),参数传true的意思是：先返回线程当前是否处于中断状态后再清除中断状态。这样描述比较正确。
		 * 终于搞明白了, isInterrupted(true) 这个方法实际上干了俩件事情, 第一件事情是先获取线程的状态, 第二件事情是清除线程的中断状态。
		 * isInterrupted(false),参数传false的意思是：不清除线程状态,并返回线程状态。
		 * interrupted()方法的功能是:测试当前线程是否已被中断。注意是测试当前线程(不是此线程)
		 * interrupted()是测试当前线程是否被中断（检查中断标志），返回一个boolean并清除中断状态，第二次再调用时中断状态已经被清除，将返回一个false。
		 * interrupted()注意这个方法有坑,这个方法必须放在你自己线程的代码里面,interrupted()这个方法获取的是当前线程
		 * interrupted()方法的功能是:测试当前线程是否已被中断.注意看源码，interrupted()这个方法会调用isInterrupted(true)这个方法,并将参数设置为true
		 * isInterrupted(true)的意思是清除线程的中断状态,如果清除成功返回true,当你第二次调用isInterrupted(true)方法时,线程不是中断状态,就会清除失败
		 * https://blog.csdn.net/qq_39682377/article/details/81449451
		 */
		System.out.println("interrupted()是静态方法,测试线程是否中断状态:" + Thread.interrupted());
		
		//Thread.currentThread().suspend();//suspend挂起的线程,这个方法需要跟resume方法搭配使用,容易造成死锁,被废弃了
		//Thread.currentThread().resume();//resume恢复挂起的线程,这个方法需要跟suspend方法搭配使用,容易造成死锁,被废弃了
		
		try {
			/**
			 * 根据系统计时器和调度程序的精度和准确性，使当前正在执行的线程进入休眠状态（暂时停止执行）达指定的毫秒数。 线程不会失去任何监视器的所有权。
			 * 但会失去CPU的资源,就是不在占用CPU了,并且是在指定的时间内不参与CPU的抢占。
			 * Thread.sleep(0);这行代码的意思是,CPU选到我了,但是我主动放弃CPU(为了公平起见),然后立即(等待0秒)参与到下一轮的CPU抢占中.
			 */
			Thread.sleep(500000);
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
				/**
				 * 这里设置这么长时间也没用,当对象调用notify()或notifyAll()方法时,线程会被自动唤醒,
				 * 不必真的等10000000毫秒.
				 */
				this.wait(10000000);//wait()方法是死等,wait(1000);是等待一定的时间,时间单位是毫秒
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