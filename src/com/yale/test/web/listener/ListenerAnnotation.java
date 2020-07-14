package com.yale.test.web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/*
 * 该注解用于将类声明为监听器,被@WebListener标注的类必须实现以下至少一个接口:
 * ServletContextListener
 * ServletContextAttributeListener
 * ServletRequestListener
 * ServletAttributeListener
 * HttpSessionListener
 * HttpSessionAttributeListener
 * 使用注解没法定义监听器或者过滤器的顺序
 */
@WebListener("用注解的方式注册监听器,不需要在web.xml里面配置,tomcat会自己扫描,这里描述该监听器的用途")
public class ListenerAnnotation implements ServletContextListener{

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("WebListener注解方式配置监听器,需要tomcat7.0 jdk1.6支持才行,web.xml里面的metadata-complete配置为false才行,true的话不行");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		
	}
}
