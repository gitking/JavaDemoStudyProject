package com.yale.test.thread.mldn;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

class TaskQueue {
	Queue<String> queue = new LinkedList<String>();
	public synchronized void addTask(String s) {
		this.queue.add(s);
	}
	
	/*
	 * 上述代码看上去没有问题：getTask()内部先判断队列是否为空，如果为空，就循环等待，直到另一个线程往队列中放入了一个任务，while()循环退出，就可以返回队列的元素了。
	 * 但实际上while()循环永远不会退出。因为线程在执行while()循环时，已经在getTask()入口获取了this锁，其他线程根本无法调用addTask()，因为addTask()执行条件也是获取this锁。
	 * 因此，执行上述代码，线程会在getTask()中因为死循环而100%占用CPU资源。
	 * 如果深入思考一下，我们想要的执行效果是：
     * 线程1可以调用addTask()不断往队列中添加任务；
     * 线程2可以调用getTask()从队列中获取任务。如果队列为空，则getTask()应该等待，直到队列中至少有一个任务时再返回
	 */
	public synchronized String getTask() {
		while (this.queue.isEmpty()) {
		}
		return queue.remove();
	}
	
	/*
	 * 因此，多线程协调运行的原则就是：当条件不满足时，线程进入等待状态；当条件满足时，线程被唤醒，继续执行任务。
	 * 对于上述TaskQueue，我们先改造getTask()方法，在条件不满足时，线程进入等待状态
	 * 当一个线程执行到getTask()方法内部的while循环时，它必定已经获取到了this锁，此时，线程执行while条件判断，如果条件成立（队列为空），线程将执行this.wait()，进入等待状态。
	 * 这里的关键是：wait()方法必须在当前获取的锁对象上调用，这里获取的是this锁，因此调用this.wait()。
	 * 调用wait()方法后，线程进入等待状态，wait()方法不会返回，直到将来某个时刻，线程从等待状态被其他线程唤醒后，wait()方法才会返回，然后，继续执行下一条语句。
	 * 有些仔细的童鞋会指出：即使线程在getTask()内部等待，其他线程如果拿不到this锁，照样无法执行addTask()，肿么办？
	 * 这个问题的关键就在于wait()方法的执行机制非常复杂。首先，它不是一个普通的Java方法，而是定义在Object类的一个native方法，也就是由JVM的C代码实现的。其次，必须在synchronized块中才能调用wait()方法，因为wait()方法调用时，会释放线程获得的锁，wait()方法返回后，线程又会重新试图获得锁。
	 * 因此，只能在锁对象上调用wait()方法。因为在getTask()中，我们获得了this锁，因此，只能在this对象上调用wait()方法：
	 * 当一个线程在this.wait()等待时，它就会释放this锁，从而使得其他线程能够在addTask()方法获得this锁。
	 * 现在我们面临第二个问题：如何让等待的线程被重新唤醒，然后从wait()方法返回？答案是在相同的锁对象上调用notify()方法。我们修改addTask()如下：
	 */
	public synchronized String getTaskWait() throws InterruptedException {
		/*
		 * 再注意到我们在while()循环中调用wait()，而不是if语句：
		 * 为什么要用while()呢？假如,在this对象上面有三个线程在等待,当另外一个线程调用了this.notifyAll()方法,将这个三个wait()线程都唤醒
		 * 唤醒之后当然只有一个wait线程,能拿到锁,然后这个线程方法执行完毕后,释放锁.然后另外俩个wait线程又会参与到锁的竞争中去,假如其中一个wait()线程
		 * 抢到锁了,如果你用if代码就不会判断queue队列是否为空了,直接去remove,如果你用while()循环,即使抢到锁之后,还会再次确认一下,queue队列是否为空,
		 * 如果为空则继续等待,不为空就可以安全的remvoe了。
		 * 廖雪峰的回答如下:
		 * https://www.liaoxuefeng.com/wiki/1252599548343744/1306580911915042
		 * 你这么想：
		 * 有A、B、C 3个线程在isEmpty()=true后进入了wait()，此时queue=[]（空的）
		 * D线程放入了一个task，此时queue=['task1']，然后唤醒所有等待线程。
		 * 此刻：A、B、C都要从wait()返回，但只有其中一个能先获得锁先执行，假设是A，它立刻调用remove()然后释放锁，这个时候，queue又空了
		 * 随后B、C如果获得锁不经过while判断直接remove()，它会报错。经过while判断发现queue还是空的，于是再次wait()
		 * 用while还是if，跟你的逻辑有关系。
		 */
		while(this.queue.isEmpty()){
			this.wait();// 释放this锁:
			//被唤醒后,重新获取this锁,但不是一定会获取锁,需要等待CPU的分配
		}
		return this.queue.remove();
	}
	
	/*
	 * 注意到在往队列中添加了任务后，线程立刻对this锁对象调用notify()方法，这个方法会唤醒一个正在this锁等待的线程（就是在getTask()中位于this.wait()的线程），从而使得等待线程从this.wait()方法返回。
	 */
	public synchronized void addTaskNotify(String s) {
		this.queue.add(s);
		//this.notify(); // 唤醒在this锁等待的线程
		
		/*
		 * 调用了this.notifyAll()而不是this.notify()，使用notifyAll()将唤醒所有当前正在this锁等待的线程，
		 * 而notify()只会唤醒其中一个（具体哪个依赖操作系统，有一定的随机性）。这是因为可能有多个线程正在getTask()方法内部的wait()中等待，
		 * 使用notifyAll()将一次性全部唤醒。通常来说，notifyAll()更安全。有些时候，如果我们的代码逻辑考虑不周，
		 * 用notify()会导致只唤醒了一个线程，而其他线程可能永远等待下去醒不过来了。
		 */
		this.notifyAll();
	}
}
//我们来看一个完整的例子：
/*
 * 现在我们面临第二个问题：如何让等待的线程被重新唤醒，然后从wait()方法返回？答案是在相同的锁对象上调用notify()方法。我们修改addTask()如下：
 * 小结
 * wait和notify用于多线程协调运行：
 * 在synchronized内部可以调用wait()使线程进入等待状态；
 * 必须在已获得的锁对象上调用wait()方法；
 * 在synchronized内部可以调用notify()或notifyAll()唤醒其他等待线程；
 * 必须在已获得的锁对象上调用notify()或notifyAll()方法；
 * 已唤醒的线程还需要重新获得锁后才能继续执行。
 */
public class ThreadWaitDemo {
	public static void main(String[] args) throws InterruptedException {
		TaskQueue queue = new TaskQueue();
		List<Thread> listTh = new ArrayList<Thread>();
		for (int i=0; i<5; i++) {
			Thread t = new Thread(){
				@Override
				public void run(){
					while (true) {
						try {
							String s = queue.getTaskWait();
							System.out.println("execute task:" + s);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			};
			t.start();
			listTh.add(t);
		}
		
		Thread thread = new Thread(()->{
			for(int i=0; i<10; i++){
				String s = "t-" + Math.random();
				System.out.println("add task:" + s);
				queue.addTaskNotify(s);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
		thread.join();
		Thread.sleep(100);
	}
}