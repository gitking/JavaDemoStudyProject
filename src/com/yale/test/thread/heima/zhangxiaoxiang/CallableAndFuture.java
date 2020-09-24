package com.yale.test.thread.heima.zhangxiaoxiang;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.ThreadMXBean;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

/*
 * Runnable接口有个问题，它的方法没有返回值。如果任务需要一个返回结果，那么只能保存到变量，还要提供额外的方法读取，非常不便。
 * 所以，Java标准库还提供了一个Callable接口，和Runnable接口比，它多了一个返回值：并且Callable接口是一个泛型接口，可以返回指定类型的结果。
 * 现在的问题是，如何获得异步执行的结果？
 * 如果仔细看ExecutorService.submit()方法，可以看到，它返回了一个Future类型，一个Future类型的实例代表一个未来能获取结果的对象：
 * ExecutorService executor = Executors.newFixedThreadPool(4); 
	// 定义任务:
	Callable<String> task = new Task();
	// 提交任务并获得Future:
	Future<String> future = executor.submit(task);
	// 从Future获取异步执行返回的结果:
	String result = future.get(); // 可能阻塞
	当我们提交一个Callable任务后，我们会同时获得一个Future对象，然后，我们在主线程某个时刻调用Future对象的get()方法，就可以获得异步执行的结果。在调用get()时，
	如果异步任务已经完成，我们就直接获得结果。如果异步任务还没有完成，那么get()会阻塞，直到任务完成后才返回结果。
	一个Future<V>接口表示一个未来可能会返回的结果，它定义的方法有：
    get()：获取结果（可能会等待）
    get(long timeout, TimeUnit unit)：获取结果，但只等待指定的时间；
    cancel(boolean mayInterruptIfRunning)：取消当前任务；
    isDone()：判断任务是否已完成。
 */
public class CallableAndFuture {

	public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {
		ExecutorService ec = Executors.newSingleThreadExecutor();
		Future<String> future = ec.submit(new Callable<String>(){
			public String call() throws Exception {
				Thread.sleep(2000);
				return "hello";
			}
		});//submit这个方法有返回结果,如果你不要结果,最好使用execute方法,不要使用submit
		//ec.execute(command);execute这个方法没有返回结果
		System.out.println("等待结果");
		System.out.println("拿到线程返回的结果:" + future.get());
		
		//System.out.println("等待1秒后那结果,拿不到结果就报错, 拿到线程返回的结果:" + future.get(1, TimeUnit.SECONDS));

		ec.shutdown();
		
		System.exit(1);
		
		ExecutorService ec2 = Executors.newFixedThreadPool(10);
		//把线程池传进去,提交一组线程进去,哪个线程先返回结果,就把哪个线程的返回结果取出来
		CompletionService<Integer> cs = new ExecutorCompletionService<>(ec2);
		for (int i=0;i<10;i++) {
			final int seq = i;
			cs.submit(new Callable<Integer>(){
				@Override
				public Integer call() throws Exception {
					Thread.sleep(new Random().nextInt(5000));
					return seq;
				}
			});
		}
		
		for(int i=0;i<10;i++) {
			Future<Integer> fu = cs.take();
			System.out.println("上面的线程池里面哪个线程先干完,就先把哪个的返回结果取出来:" + fu.get());
		}
		ec2.shutdown();
	}
}
