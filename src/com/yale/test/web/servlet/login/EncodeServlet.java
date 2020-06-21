package com.yale.test.web.servlet.login;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EncodeServlet extends HttpServlet {
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html;charset=UTF-8");//设置servlet返回给浏览器的内容的编码
		String username = req.getParameter("username");
		System.out.println("get乱码:" + username);
		resp.getWriter().println(username);
		
		byte[] strByte = username.getBytes("ISO-8859-1");//变回字节码
		String str = new String(strByte, "UTF-8");//重新编码
		System.out.println("不乱码了:" + str);
	}
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");//处理POST请求乱码问题
		resp.setContentType("text/html;charset=UTF-8");//设置servlet返回给浏览器的内容的编码
		String username = req.getParameter("username");
		System.out.println("post乱码:" + username);
		resp.getWriter().println(username);
	}
}
