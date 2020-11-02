package com.yale.test.web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/*
 * 使用Listener
 * 除了Servlet和Filter外，JavaEE的Servlet规范还提供了第三种组件：Listener。
 * Listener顾名思义就是监听器，有好几种Listener，其中最常用的是ServletContextListener，我们编写一个实现了ServletContextListener接口的类如下：
 * 任何标注为@WebListener，且实现了特定接口的类会被Web服务器自动初始化。上述AppListener实现了ServletContextListener接口，它会在整个Web应用程序初始化完成后，以及Web应用程序关闭后获得回调通知。
 * 我们可以把初始化数据库连接池等工作放到contextInitialized()回调方法中，把清理资源的工作放到contextDestroyed()回调方法中，因为Web服务器保证在contextInitialized()执行后，才会接受用户的HTTP请求。
 * 很多第三方Web框架都会通过一个ServletContextListener接口初始化自己。
 * 除了ServletContextListener外，还有几种Listener：
 * 1.HttpSessionListener：监听HttpSession的创建和销毁事件；
 * 2.ServletRequestListener：监听ServletRequest请求的创建和销毁事件；
 * 3.ServletRequestAttributeListener：监听ServletRequest请求的属性变化事件（即调用ServletRequest.setAttribute()方法）；
 * 4.ServletContextAttributeListener：监听ServletContext的属性变化事件（即调用ServletContext.setAttribute()方法）；
 * ServletContext
 * 一个Web服务器可以运行一个或多个WebApp，对于每个WebApp，Web服务器都会为其创建一个全局唯一的ServletContext实例，
 * 我们在AppListener里面编写的两个回调方法实际上对应的就是ServletContext实例的创建和销毁：
 * ServletRequest、HttpSession等很多对象也提供getServletContext()方法获取到同一个ServletContext实例。
 * ServletContext实例最大的作用就是设置和共享全局信息。
 * 此外，ServletContext还提供了动态添加Servlet、Filter、Listener等功能，它允许应用程序在运行期间动态添加一个组件，虽然这个功能不是很常用。
 * 小结
 * 通过Listener我们可以监听Web应用程序的生命周期，获取HttpSession等创建和销毁的事件；
 * ServletContext是一个WebApp运行期的全局唯一实例，可用于设置和共享配置信息。
 * 问:请问要怎么正确关闭 tomcat 啊？在eclipse里直接点击 terminate 并没有打印 "WebApp destroyed."
 * 答:手动安装tomcat，有startup和shutdown两个脚本
 */
@WebListener
public class AppListener implements ServletContextListener{

	@Override
	public void contextInitialized(ServletContextEvent sce) {
	    // 在此初始化WebApp,例如打开数据库连接池等:
		System.out.println("WebApp initialized. ServletContext = " + sce.getServletContext());
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	    // 在此清理WebApp,例如关闭数据库连接池等:
		System.out.println("WebApp destroyed.");
	}
}
