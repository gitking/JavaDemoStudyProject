package com.yale.test.web.servlet;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 实现servlet有三种方式
 * 	实现javax.servlet.Servlet接口
 *  继承javax.servlet.GenericServlet类
 *  继承:javax.servlet.http.HttpServlet类(HttpServlet类继承了GenericServlet抽象类)
 *  开发中经常用的是继承HttpServlet类,但是学习还得从Servlet接口开始学
 *  
 *  注意Servlet中的方法,99%的情况下是由tomcat来调用的,我们自己不调用.
 *  tomat的源码需要自己去https://tomcat.apache.org/download-80.cgi官网下载
 *  apache-tomcat-8.5.56-src.zip
 *  注意由于tomcat是通过反射调用Servlet的,所以Servlet必须有无参构造方法,否则tomcat就会调用失败,非常严重
 * @author dell
 */
public class ServletDeom implements Servlet {

	/**
	 * destroy方法会在Servlet被tomcat注销之前调用,并且只会调用一次
	 */
	@Override
	public void destroy() {
		System.out.println("destroy方法会在tomcat停止服务的时候调用");
	}

	/**
	 * 可以用来获取servlet在web.xml里面的配置信息
	 */
	@Override
	public ServletConfig getServletConfig() {
		return null;
	}

	/**
	 * 获取servlet本身自己的信息,这个方法几乎没用
	 */
	@Override
	public String getServletInfo() {
		System.out.println("别人调用这个方法的时候,我可以告诉他我这个servlet是用来处理什么的？");
		return "我是一个快乐的servlet";
	}

	/**
	 * init方法会在Servlet对象创建(Servlet对象由tomcat创建)之后马上执行,并只执行一次。
	 * tomcat会在你第一次访问这个servlet的时候调用init方法
	 * 注意tomcat调用init方法时,tomcat会传一个ServletConfig对象过来
	 */
	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		System.out.println("tomcat会在你第一次访问这个servlet的时候调用一次init方法,这说明servlet是单例模式,多个用户同时访问的时候,会有线程安全问题");
		System.out.println("你可以在这个方法里面记录用户是什么时候开始用这个功能的");
		//还有一种方式
		System.out.println("web.xml中load-on-startup的意思是在tomcat服务启动的时候创建servlet的对象,而不是在第一次方法servlet的时候创建load-on-startup的值必须为非负整数,值越小的越先创建");
		/**
		 * ServletConfig是什么?
		 * tomcat会将web.xml里面的servlet节点里面的内容解析出来放到ServletConfig里面
		 * ServletConfig只代表本Servlet的配置信息
		 */
		String name = servletConfig.getServletName();
		System.out.println("获取web.xml里面配置的servlet-name:" + name);
		
		String initParam = servletConfig.getInitParameter("p1");//获取
		System.out.println("获取在web.xml里面servlet下面配置的init-param下面的param-value的参数值:" + initParam);
		
		Enumeration<String> params = servletConfig.getInitParameterNames();
		while (params.hasMoreElements()) {
			System.out.println("获取在web.xml里面servlet下面配置的init-param下面的param-name:" + params.nextElement());
		}
		
		ServletContext sc = servletConfig.getServletContext();//获取servlet上下文对象
		System.out.println("一个项目只有一个ServletContext对象,所以我们可以在N多个Servlet中来获取这个唯一的对象,使用他可以给多个");
		System.out.println("servlet传递数据,所以大多数项目中都给ServletContext对象起名叫applicationContext");
		System.out.println("ServletContext这个对象在tomcat启动时就创建,在tomcat关闭时才会死去");

	}

	/**
	 * service方法会被调用多次,每次处理请求都是在调用这个方法
	 */
	@Override
	public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
		
	}
}
