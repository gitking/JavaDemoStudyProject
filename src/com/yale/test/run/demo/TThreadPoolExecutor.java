package com.yale.test.run.demo;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * TThreadPoolExecutor.java模拟juc的ThreadPoolExecutor
 * @author issuser
 *
 */
public class TThreadPoolExecutor extends TExecutorService {
	/**
	 * 线程池状态,false:未关闭,true关闭
	 */
	private AtomicBoolean ctl = new AtomicBoolean();
	
	@Override
	public void execute() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					//显式的sleep1ns,模拟主动切换线程
					TimeUnit.NANOSECONDS.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
		
		/*
		 * 模拟ThreadPoolExecutor,启动新建线程后,循环检查线程池状态,验证是否会在finalize中shutdown
		 * 如果线程池被提前shutdown,则抛异常
		 */
		for(int i=0;i<1_000_000; i++) {
			if (ctl.get()) {
				throw new RuntimeException("reject!!![" + ctl.get() + "]");
			}
		}
	}
	
	@Override
	public void shutdown() {
		ctl.compareAndSet(false, true);
	}
}
