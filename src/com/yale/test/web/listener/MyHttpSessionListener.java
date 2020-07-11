package com.yale.test.web.listener;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class MyHttpSessionListener implements HttpSessionListener {

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		System.out.println("sessionCreated,httpSession被创建了");
		System.out.println("httpSession会在用户第一次通过浏览器访问我们的应用的时候,tomcat就会创建一个session");
		HttpSession hs = se.getSession();
		System.out.println("httpSession的id:" + hs.getId());
		System.out.println("HttpSessionListener这个监听器的主要用途是:1统计在线人数，2记录访问日志");
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		System.out.println("sessionCreated,httpSession被销毁了");
		System.out.println("注销session有俩种情况,第一:tomcat被关闭的时候,第二：用户关闭浏览器一段时间后session过期后就会自动销毁，即使你不关闭浏览器但是长时间不活动，session也会自动过期销毁");
	}
}
