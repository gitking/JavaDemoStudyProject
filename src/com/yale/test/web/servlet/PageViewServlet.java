package com.yale.test.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 一个简易的页面访问量统计Servlet
 * @author dell
 */
public class PageViewServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ServletContext sc = this.getServletContext();
		Integer integer = (Integer)sc.getAttribute("pvCount");
		if (integer == null) {
			integer = 1;
			sc.setAttribute("pvCount", integer);
		} else {
			sc.setAttribute("pvCount", integer + 1);
		}
		resp.setCharacterEncoding("UTF-8");//这里看java3y的文章
		resp.setContentType("text/html;charset=UTF-8");//告诉浏览器以html结果显示,html编码为UTF-8
		PrintWriter pw = resp.getWriter();
		pw.print("<span>页面访问量PageView:</span><h1>" + integer + "</h1>");
	}
}
