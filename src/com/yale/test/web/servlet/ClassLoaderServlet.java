package com.yale.test.web.servlet;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

public class ClassLoaderServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		/**
		 * ClassLoader是一个抽象类,但是却没有一个抽象方法需要子类实现,
		 * 这样的目的不想让别人直接实例化ClassLoader类
		 * ClassLoader获取类路径资源,资源其实就是文件的意思。类路径对一个Javaweb项目来说,就是/WEB-INF/classes和/WEB-INF/lib下面的每个jar包
		 * 对于一个javaweb项目来说,src下面的源码包括(你的创建的别的文件xml,txt,png文件等),编译之后都会跑到WebContent\WEB-INF\classes下面
		 * 有的项目WebContent叫WebRoot,我感觉叫WebRoot跟合适,WebRoot的意思就是web跟路径
		 * @author dell
		 */
		ClassLoader cl = this.getClass().getClassLoader();
		InputStream in = cl.getResourceAsStream("classloader.txt");
		String str =IOUtils.toString(in);
		System.out.println(str);

		System.out.println("**********************");
		
		InputStream ins = cl.getResourceAsStream("com/yale/test/web/servlet/classloader.txt");
		String insStr =IOUtils.toString(ins);
		System.out.println(insStr);
		
		Class cla = this.getClass();
		InputStream classIn = cla.getResourceAsStream("class.txt");
		System.out.println("**********************");

		String classStr =IOUtils.toString(classIn);
		System.out.println(classStr);
		
		/**
		 * /classSrc.txt的意思是相对于src(classes)目录
		 */
		Class claSec = this.getClass();
		InputStream classInSec = claSec.getResourceAsStream("/classSrc.txt");
		System.out.println("**********************");

		String classStrSec =IOUtils.toString(classInSec);
		System.out.println(classStrSec);
		
		/**
		 * /../../index.jsp的意思是,/这个杠是从classe目录开始,
		 * ../../这个俩个是上级目录的上级目录,要理解这个你得去看编译后的WebContent目录结构就懂了
		 */
		Class claTh = this.getClass();
		InputStream classInTh = claTh.getResourceAsStream("/../../index.jsp");
		System.out.println("**********************");

		String classStrTh =IOUtils.toString(classInTh);
		System.out.println(classStrTh);
		
		System.out.println("**********************");

		/**
		 * 利用ServletContext对象可以得到JavaWeb项目的根目录,
		 * getRealPath得到的是真实的服务器上面带盘符的路径:D:\GitWorkSpace\JavaDemoStudyProject\WebContent\
		 * ServletContext的实现类为：org.apache.catalina.core.ApplicationContextFacade
		 */
		ServletContext sc = this.getServletContext();
		System.out.println("ServletContext的实现类为：" + sc.getClass().getName());
		String realPath = sc.getRealPath("/");
		System.out.println("" + realPath);
	}
}
