package com.yale.test.thread.mldn;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
/**
 * 进程的概念:在操作系统之中一个程序的执行周期就称之为进程。
 * 课时26 进程与线程
 * 从JDK1.5之后新增加了一个类java.util.concurrent.Callable,通过Callable<V>实现多线程,Callable跟Runnable一样只有一个方法,call方法
 * java.util.concurrent这个开发包主要是进行高性能编程使用的,也就是说在这个开发包里面会提供一些高并发操作中才会使用到的类.
 * Callable<V>的call方法有返回值
 * @author dell
 */
class CallableDemo implements Callable<String>{
	@Override
	public String call() throws Exception {
		return "票卖完了,下次把";
	}
}

public class TestCallable {
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		System.out.println("线程只能通过Thread类的start方法启动,Callble需要跟FutureTask类结合");
		System.out.println("多线程的主要操作方法都在Thread类里面");

		FutureTask<String> task = new FutureTask<String>(new CallableDemo());
		new Thread(task).start();//启动线程
		System.out.println("通过FutureTask拿到返回值:" + task.get());
	}
}
