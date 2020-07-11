package com.yale.test.web.listener;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

public class MyServletRequestListener implements ServletRequestListener {

	@Override
	public void requestDestroyed(ServletRequestEvent sre) {
		System.out.println("ServletRequestListener监听器:用户请求结束的时候,直接就销毁了");
	}

	@Override
	public void requestInitialized(ServletRequestEvent sre) {
		System.out.println("ServletRequestListener监听器:用户请求的时候先执行这个requestInitialized方法");
		System.out.println("ServletRequestListener监听器:主要用途:读取参数,记录访问历史");
	}
}
