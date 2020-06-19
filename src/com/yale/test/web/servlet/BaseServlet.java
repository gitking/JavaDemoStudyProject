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
 * http://localhost:8080/pcis/userServlet?method=addUser
 * @author dell
 */
public abstract class BaseServlet extends HttpServlet {
	/**
	 * 把这个通过反射调用的功能放到父类中,让别的servlet继承这个BaseServlet父类,这样每个子类就可以共用这个功能了
	 */
	@Override
	public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String methodName = req.getParameter("method");
		
		if (methodName == null || methodName.trim().isEmpty()) {
			System.out.println("你没有按照规范传递参数,请修改请求");
			//http://localhost:8080/pcis/userServlet?method=addUser
			throw new RuntimeException("你没有按照规范传递参数,请修改请求");
		}
		
		try {
			Class cls = this.getClass();
			Method method = cls.getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
			String returnVal = (String)method.invoke(this, req, resp);
			if (returnVal != null && !returnVal.trim().isEmpty()) {
				System.out.println("得到项目名字:" + req.getContextPath());
				if (returnVal.contains(":")) {
					int index = returnVal.indexOf(":");//得到冒号的位置
					String s = returnVal.substring(0, index);
					String path = returnVal.substring(index+1);//从冒号后面的字符开始截取到最后
					if (s.equalsIgnoreCase("redirect")) {//忽略大小写比较
						System.out.println("得到项目名字:" + req.getContextPath());
						resp.sendRedirect(req.getContextPath() + path);//重定向
					} else if (s.equalsIgnoreCase("forward")) {
						req.getRequestDispatcher(path).forward(req, resp);
					} else {
						throw new RuntimeException("您的操作" + s + "当前版本不能支持");
					}
				} else {
					req.getRequestDispatcher(returnVal).forward(req, resp);
				}
			}
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new RuntimeException("您要调用的方法" + methodName + "(HttpServletRequest req, HttpServletResponse resp)不存在.");
		} catch (SecurityException e) {
			e.printStackTrace();
			throw new RuntimeException("AA");
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new RuntimeException("AA");
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw new RuntimeException("您要调用的方法" + methodName + "内部抛出了异常.");
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			throw new RuntimeException("AA");
		}
	}
}
