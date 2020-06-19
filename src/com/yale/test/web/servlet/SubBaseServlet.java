package com.yale.test.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SubBaseServlet extends BaseServlet {
	
	public String forward(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		System.out.println("用户调用了forward方法....");
		//req.getRequestDispatcher("").forward(req, resp);//转发
		//resp.sendRedirect(req.getContextPath() + "/index.jsp");//重定向
		return "forward:/index.jsp";
	}
	
	public String redirect(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		System.out.println("用户调用了redirect方法....");
		//req.getRequestDispatcher("").forward(req, resp);//转发
		//resp.sendRedirect(req.getContextPath() + "/index.jsp");//重定向
		return "redirect:/index.jsp";
	}
	
	public String download(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		System.out.println("用户调用了fun方法....");
		//req.getRequestDispatcher("").forward(req, resp);//转发
		//resp.sendRedirect(req.getContextPath() + "/index.jsp");//重定向
		return "download:/WEB-INF/file/a.jpg";
	}
}
