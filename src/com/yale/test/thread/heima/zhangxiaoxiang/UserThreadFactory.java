package com.yale.test.thread.heima.zhangxiaoxiang;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class UserThreadFactory implements ThreadFactory {
	private final String namePrefix;
	private final AtomicInteger nextId = new AtomicInteger(1);

	// 定义线程组名称，在jstack问题排查时，非常有帮助,阿里巴巴把这里的构造方法设置为protected了,建议你最好也这样做
	public UserThreadFactory(String whatFeaturOfGroup) {
		namePrefix = "From UserThreadFactory's " + whatFeaturOfGroup + "-Worker-";
	}

	@Override
	public Thread newThread(Runnable task) {
		String name = namePrefix + nextId.getAndIncrement();
		//Thread thread = new Thread(null, task, name, 0, false);这个构造方法是java9之后才有的
		Thread thread = new Thread(task, name);
		System.out.println("自己设置线程池里面线程的名字:" + thread.getName());
		return thread;
	}
}