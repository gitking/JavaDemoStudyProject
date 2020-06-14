package com.yale.test.web.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class SubGenricServletDemo extends GenericServletDemo{

	public void init () {
		System.out.println("我这里重写的是GenericServletDemo的init无参的方法");
		System.out.println("tomcat会先调用父类中的init(ServletConfig servletConfig)方法,然后init(ServletConfig servletConfig)再调用这里的init方法");
		System.out.println("写在父类中的方法,相当于写在子类中了");
	}
	@Override
	public void service(ServletRequest arg0, ServletResponse arg1) throws ServletException, IOException {
		super.service(arg0, arg1);
	}
}
