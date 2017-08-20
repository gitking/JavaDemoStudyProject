package com.yale.test.thread.chapter.three;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

class MyThread implements Callable<String> {
	@Override
	public String call() throws Exception {
		String content = "Callable可以让线程返回一个接口,Runnable接口的run方法没有返回结果.但是Callable接口需要和FutureTask接口搭配使用";
		return content;
	}
}

public class CallableTest {
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		MyThread myThread = new MyThread();
		FutureTask<String> futureTask = new FutureTask<String>(myThread);
		new Thread(futureTask).start();
		System.out.println("Callable与普通有返回值方法的区别就在于,调用了Callable接口后,可以继续干别的事情,而不是一直在等结果的返回");
		System.out.println(futureTask.get());
	}
}
