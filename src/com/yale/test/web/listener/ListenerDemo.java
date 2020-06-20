package com.yale.test.web.listener;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
/**
 * 监听器:
 *  监听器是一个接口,内容由我们来实现
 *  它需要注册,例如注册在按钮上
 *  监听器中的方法,会在特殊事件发生时自动调用(观察者模式)
 *  事件源
 *  事件
 *  监听器(监听器注册在事件源上,等待特定的事件发生,然后调用自己的方法)
 *  JavaWeb被监听的事件源为:ServletContext, HttpSession,ServletRequest即三大域对象
 *  ServletContextListener,HttpSessionListener,ServletRequestListener
 */
public class ListenerDemo implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		Map<String, Integer> map = new LinkedHashMap<String, Integer>();//统计访问IP访问次数
		ServletContext sc = sce.getServletContext();
		sc.setAttribute("map", map);
		System.out.println("我是最先被调用的,tomcat启动的时候我就被调用了");
		System.out.println("ServletContextListener监听ServletContext的出生,");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println("ServletContextListener监听ServletContext的死亡");
	}
}
