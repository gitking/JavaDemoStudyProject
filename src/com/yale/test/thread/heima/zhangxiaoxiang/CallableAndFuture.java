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
