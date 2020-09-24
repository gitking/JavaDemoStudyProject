package com.yale.test.thread.heima.zhangxiaoxiang;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 先进先出就叫队列,BlockingQueue是阻塞队列的接口,
 * 我们在前面已经通过ReentrantLock和Condition实现了一个BlockingQueue：
 * BlockingQueue的意思就是说，当一个线程调用这个TaskQueue的getTask()方法时，该方法内部可能会让线程变成等待状态，直到队列条件满足不为空，线程被唤醒后，getTask()方法才会返回。
 * 因为BlockingQueue非常有用，所以我们不必自己编写，可以直接使用Java标准库的java.util.concurrent包提供的线程安全的集合：ArrayBlockingQueue。
 * 除了BlockingQueue外，针对List、Map、Set、Deque等，java.util.concurrent包也提供了对应的并发集合类。我们归纳一下：
 * interface	non-thread-safe				thread-safe
	List		ArrayList				CopyOnWriteArrayList
	Map			HashMap					ConcurrentHashMap
	Set			HashSet/TreeSet			CopyOnWriteArraySet
	Queue		ArrayDeque/LinkedList	ArrayBlockingQueue/LinkedBlockingQueue
	Deque		ArrayDeque/LinkedList	LinkedBlockingDeque
	使用这些并发集合与使用非线程安全的集合类完全相同。我们以ConcurrentHashMap为例：
	因为所有的同步和加锁的逻辑都在集合内部实现，对外部调用者来说，只需要正常按接口引用，其他代码和原来的非线程安全代码完全一样。即当我们需要多线程访问时，把：
	Map<String, String> map = new HashMap<>();改为：Map<String, String> map = new ConcurrentHashMap<>();就可以了。
	java.util.Collections工具类还提供了一个旧的线程安全集合转换器，可以这么用：
	Map unsafeMap = new HashMap();
	Map threadSafeMap = Collections.synchronizedMap(unsafeMap);
	但是它实际上是用一个包装类包装了非线程安全的Map，然后对所有读写方法都用synchronized加锁，这样获得的线程安全集合的性能比java.util.concurrent集合要低很多，所以不推荐使用。
 * @author dell
 */
public class BlockingQueueTest {

	public static void main(String[] args) {
		final BlockingQueue queue = new ArrayBlockingQueue(3);//阻塞队列的长度为3
		for (int i=0;i<2;i++) {
			new Thread(){
				public void run (){
					while (true) {
						try {
							Thread.sleep((long)(Math.random()*1000));
							System.out.println(Thread.currentThread().getName() + "准备放数据");
							queue.put(1);//该方法放不进去的时候会阻塞,在这里一直等,等到能放进去的时候
							//queue.add(1);该方法放不进去的时候会抛出异常
							//queue.offer(1);该方法放不进去的时候会返回false
							//queue.offer(1,10,TimeUnit.SECONDS);该方法放不进去的时候会等待一段时间,超时之后返回false

							System.out.println(Thread.currentThread().getName() + "已经把数据放进去了," + 
							"队列目前有" + queue.size() + "数据");
						} catch(InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}.start();;
		}
		
		new Thread(){
			public void run() {
				while(true){
					try {
						Thread.sleep(1000);//将此处的睡眠时间分别改为100和1000,观察运行结果
						System.out.println(Thread.currentThread().getName() + "准备取数据!");
						queue.take();//该方法取不到数据的时候会阻塞,在这里一直等,等到能取到数据的时候
						//queue.remove();该方法取不到数据的时候会抛出异常
						//queue.poll();该方法取不到数据的时候会返回false
						//queue.poll(10,TimeUnit.SECONDS);该方法取不到数据的时候会等待一段时间,超时之后返回false
						System.out.println(Thread.currentThread().getName() + "已经取走数据,队列目前有" + queue.size() + "个数据.");
					} catch(InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
	}
}
