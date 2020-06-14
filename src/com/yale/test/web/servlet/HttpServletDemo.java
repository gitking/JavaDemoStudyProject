package com.yale.test.web.servlet;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * HttpServlet是一个抽象类,但是该类中没有任何抽象方法
 * HttpServlet如果你没有重写doGet或者doPost方法,但是tomcat调用了doGet或者doPost方法
 * HttpServlet会抛出400或者405错误,这个你看HttpServlet的源码就知道了
 * @author dell
 */
public class HttpServletDemo extends HttpServlet {
	
	@Override
	public void init() throws ServletException {//这个init不是servlet生命周期中的那个init方法
		System.out.println("web.xml中load-on-startup的意思是在tomcat服务启动的时候创建servlet的对象,而不是在第一次方法servlet的时候创建load-on-startup的值必须为非负整数,值越小的越先创建");
	}
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	}
	
	/**
	 * 要知道tomcat其实调用的还是Servlet的services方法,HttpServlet这个类干了什么事情呢？
	 * HttpServlet这个类在service方法里面又调用了自己的service方法,然后根据http的请求类型,又在自己的service方法里面
	 * 调用相应的doGet或者doPost方法,所以继承HttpServlet类必须重写doGet或者doPost方法,否则 t会抛出400或者405错误
	 * 你去看HttpServlet的源码就知道了
	 */
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("doPost方法");
		ServletContext sc = this.getServletConfig().getServletContext();
		ServletContext servletContext =this.getServletContext();
		ServletContextEvent sce = null;
		sce.getServletContext();//ServletContextEvent这个对象也能得到ServletContext对象
		HttpSession hs = null;
		hs.getServletContext();//HttpSession这个对象也能得到ServletContext对象
		System.out.println("这个俩个方法得到的是同一个ServletContext对象" + (sc == servletContext));
	}
}
