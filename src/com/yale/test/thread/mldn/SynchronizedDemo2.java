package com.yale.test.thread.mldn;

/**
 * 当多个线程同时运行时，线程的调度由操作系统决定，程序本身无法决定。因此，任何一个线程都有可能在任何指令处被操作系统暂停，然后在某个时间段后继续执行。
 * 这个时候，有个单线程模型下不存在的问题就来了：如果多个线程同时读写共享变量，会出现数据不一致的问题。
 * 多线程模型下，要保证逻辑正确，对共享变量进行读写时，必须保证一组指令以原子方式执行：即某一个线程执行时，其他线程必须等待：
 * 通过加锁和解锁的操作，就能保证3条指令总是在一个线程执行期间，不会有其他线程会进入此指令区间。即使在执行期线程被操作系统中断执行，
 * 其他线程也会因为无法获得锁导致无法进入此指令区间。只有执行线程将锁释放后，其他线程才有机会获得锁并执行。这种加锁和解锁之间的代码块我们称之为临界区（Critical Section），
 * 任何时候临界区最多只有一个线程能执行。 可见，保证一段代码的原子性就是通过加锁和解锁实现的。Java程序使用synchronized关键字对一个对象进行加锁：
 * @author dell
 */
public class SynchronizedDemo2 {
	public static void main(String[] args) throws InterruptedException {
		AddThread add = new AddThread();
		DecThread dec = new DecThread();
		add.start();
		dec.start();
		add.join();
		dec.join();
		/*
		 * 上面的代码很简单，两个线程同时对一个int变量进行操作，一个加10000次，一个减10000次，最后结果应该是0，但是，每次运行，结果实际上都是不一样的。
		 * 这是因为对变量进行读取和写入时，结果要正确，必须保证是原子操作。原子操作是指不能被中断的一个或一系列操作。
		 * 例如，对于语句：n = n + 1;看上去是一行语句，实际上对应了3条指令：ILOAD IADD ISTORE
		 */
		System.out.println("共享变量count的最终结果每次运行都不一样:" + Counter.count);
		
		/* synchronized保证了代码块在任意时刻最多只有一个线程能执行。我们把上面的代码用synchronized改写如下：
		 * 使用synchronized解决了多线程同步访问共享变量的正确性问题。但是，它的缺点是带来了性能下降。因为synchronized代码块无法并发执行。此外，加锁和解锁需要消耗一定的时间，所以，synchronized会降低程序的执行效率。
		 * 我们来概括一下如何使用synchronized：
		 * 找出修改共享变量的线程代码块；
		 * 选择一个共享实例作为锁；
		 * 使用synchronized(lockObject) { ... }。
		 * 因为JVM只保证同一个锁在任意时刻只能被一个线程获取，但两个不同的锁在同一时刻可以被两个线程分别获取。
		 * 因此，使用synchronized的时候，获取到的是哪个锁非常重要。锁对象如果不对，代码逻辑就不对
		 * synchronized除了加锁外，还具有内存屏障功能，并且强制读取所有共享变量的主内存最新值，退出synchronized时再强制回写主内存（如果有修改）
		 */
		AddThread1 add1 = new AddThread1();
		DecThread1 dec1 = new DecThread1();
		add1.start();
		dec1.start();
		add1.join();
		dec1.join();
		System.out.println("共享变量count的最终结果肯定是0:" + Counter2.count);
		
		/*
		 * 不需要synchronized的操作
		 * JVM规范定义了几种原子操作：
		 * 基本类型（long和double除外）赋值，例如：int n = m；
		 * 引用类型赋值，例如：List<String> list = anotherList。
		 * long和double是64位数据，JVM没有明确规定64位赋值操作是不是一个原子操作，不过在x64平台的JVM是把long和double的赋值作为原子操作实现的。
		 */
	}
	public int value;
	public void set(int m) {
		//单条原子操作的语句不需要同步。例如：
		synchronized (this) {//就不需要同步
			this.value = m;
		}
	}
	public String val ="";//对引用也是类似。例如：
	public void set(String s) {
	    this.val = s;//不需要同步
	}
	
	/*
	 * 但是，如果是多行赋值语句，就必须保证是同步操作，例如：
	 * 这个方法如果不同步会出现下面的情况
	 * 你想把游戏里一个人从坐标(10,10)移动到(20,20)，另一个线程把坐标准备移动到(30,30):
		public void setPosition(int x, int y) {
		    this.x = x;
		    this.y = y;
		}
	 * 最终结果要么是(20,20)要么是(30,30)，
	 * 但是不同步的情况下，有可能出现(20, 30)或者(30,20)
	 * 这种是逻辑错误，不是你想要的最终结果
	 */
	public void setArr(int first, String val) {
		synchronized (this) {
			this.val = val;
			this.value = first;
		}
	}
	/*
	 * 有些时候，通过一些巧妙的转换，可以把非原子操作变为原子操作。例如，上述代码如果改造成：
	 * 就不再需要同步，因为this.arr = ps是引用赋值的原子操作。而语句：
	 * int[] ps = new int[] { first, last };
	 * 这里的ps是方法内部定义的局部变量，每个线程都会有各自的局部变量，互不影响，并且互不可见，并不需要同步。
	 */
	public int[] arr;
	public void setArr2(int first, int val) {
		int[] ps = new int[]{first, val};
		this.arr = ps;
		/* this.arr = new int[] { first, last };
		 * 写法是一样的，都是线程安全的，因为多线程能看到的共享变量只有this.arr
		 */
	}
	
	/*
	 * 它没有同步，因为读一个int变量不需要同步
	 */
	public int get(){
		return value;
	}
}

class Counter {
	public static int count = 0;
}

class AddThread extends Thread {
	public void run() {
		for (int i=0;i<10000;i++) {
			Counter.count +=1;
		}
	}
}

class DecThread extends Thread {
	public void run() {
		for (int i=0;i<10000;i++) {
			Counter.count -=1;
		}
	}
}


class Counter2 {
	public static final Object lock = new Object();
	public static int count = 0;
}

class AddThread1 extends Thread {
	public void run() {
		for (int i=0;i<10000;i++) {
			synchronized (Counter2.lock) {//获取锁
				Counter2.count +=1;
				//synchronized除了加锁外，还具有内存屏障功能，并且强制读取所有共享变量的主内存最新值，退出synchronized时再强制回写主内存（如果有修改）
			}//释放锁,在使用synchronized的时候，不必担心抛出异常。因为无论是否有异常，都会在synchronized结束处正确释放锁：
		}
	}
}

class DecThread1 extends Thread {
	public void run() {
		for (int i=0;i<10000;i++) {
			synchronized (Counter2.lock) {
				Counter2.count -=1;
			}
		}
	}
}