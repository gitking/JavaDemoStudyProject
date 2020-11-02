package com.yale.test.web.servlet.session;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 * 如果用户已登录，可以通过访问/signout登出。登出逻辑就是从HttpSession中移除用户相关信息：
 */
@WebServlet(urlPatterns="/signout")
public class SignOutServlet extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getSession().removeAttribute("user");
		resp.sendRedirect("/pcis/indexServlet");
	}
}
