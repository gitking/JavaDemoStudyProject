package com.yale.test.web.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 我们希望在一个servlet中可以有多个请求处理方法,要怎么做呢?
 * 思路跟HttpServlet一样,重写service方法,然后再service里面调用别的方法
 * @author dell
 */
public class UserServlet extends BaseServlet {
	
	public void addUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("用户调用了addUser...");
	}
	
	public void editUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("用户调用了editUser...");
	}
	
	public void delUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("用户调用了delUser...");
	}
	
	public void loadUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("用户调用了loadUser...");
		System.out.println("我又增加了loadUser方法,但是我没在if elseif里面添加,你可以通过反射调用");
	}
}
