package com.yale.test.web.servlet;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 这个类是模拟tomcat的GenericServlet的类实现
 * 几乎跟tomcat的GenericServlet源码一样
 * @author dell
 *
 */
public class GenericServletDemo implements Servlet {
	private ServletConfig servletConfig;

	@Override
	public void destroy() {

	}

	/**
	 * 这个方法肯定会在init方法之后调用,init方法调用之后servletConfig就有值了
	 */
	@Override
	public ServletConfig getServletConfig() {
		return this.servletConfig;
	}

	@Override
	public String getServletInfo() {
		return null;
	}

	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		this.servletConfig = servletConfig;//将tomcat帮我们创建的ServletConfig对象保存起来 
		init();
	}
	
	public void init() {
		System.out.println("提供一个无参的init方便子类调用，GenericServlet类中也是这么干的");
	}

	@Override
	public void service(ServletRequest arg0, ServletResponse arg1) throws ServletException, IOException {
		System.out.println("处理请求");
	}
	
	public ServletContext getServletContext() {
		return this.servletConfig.getServletContext();
	}
	
	public String getServletName() {
		return this.servletConfig.getServletName();
	}
	
	public String getInitParameter(String name){
		return this.servletConfig.getInitParameter(name);
	}
}
