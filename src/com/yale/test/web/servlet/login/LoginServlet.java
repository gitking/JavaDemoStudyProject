package com.yale.test.web.servlet.login;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginServlet extends HttpServlet {
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=UTF-8");
		String username = req.getParameter("username");
		if (username.contains("itcast")) {
			/**
			 * 用户信息一定要保存在session中
			 */
			req.getSession().setAttribute("admin", username);
		} else {
			req.getSession().setAttribute("username", username);
		}
		req.getRequestDispatcher("/index.jsp").forward(req, resp);
	}
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("doget...");
	}
}
