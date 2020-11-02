package com.yale.test.design.structural.adapter.demo;

import java.util.concurrent.Callable;

public class RunnableAdapter implements Runnable {
	
	private Callable<?> callable;//引用待转换接口
	
	public RunnableAdapter(Callable<?> callable) {
		this.callable = callable;
	}
	
	//执行指定接口
	@Override
	public void run() {
		//将指定接口调用委托给转换接口调用
		try {
			this.callable.call();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
