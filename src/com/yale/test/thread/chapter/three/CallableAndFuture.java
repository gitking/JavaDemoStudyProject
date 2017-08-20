package com.yale.test.thread.chapter.three;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class CallableAndFuture {
	public static void main(String[] args) {
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		Future<String> future = executorService.submit(new Callable<String>() {
			public String call() throws Exception {
				Thread.sleep(3000);
				return "Callable结果";
			}
		});
		System.out.println("等待结果");
		try {
			//System.out.println("得到结果:" + future.get());
			//指定获取结果的时间
			System.out.println("得到结果:" + future.get(1,TimeUnit.SECONDS));
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
		System.out.println("得到结果之前,后面的程序不能允许");
		executorService.shutdown();//关闭线程池
		
		
		ExecutorService threadPool = Executors.newSingleThreadExecutor();
		CompletionService<Integer> compl = new ExecutorCompletionService<Integer>(threadPool);
		for (int i=0;i<10;i++) {//循环提交10个任务
			final int seq = i;
			compl.submit(new Callable<Integer>(){
				public Integer call() throws Exception {
					Thread.sleep(new Random().nextInt(5000));
					return seq;
				}
			});
		}
		System.out.println("等待获取结果ing......");
		for (int i=0;i<10;i++) {
			try {
				System.out.println("得到结果:" + compl.take().get());
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		System.out.println("获取结果完成");
		threadPool.shutdown();
	}
}
