package com.yale.test.web.servlet.session;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 * 在IndexServlet中，可以从HttpSession取出SignInServlet设置的用户名：
 * 对于Web应用程序来说，我们总是通过HttpSession这个高级接口访问当前Session。如果要深入理解Session原理，可以认为Web服务器在内存中自动维护了一个ID到HttpSession的映射表，我们可以用下图表示：
 * 小结
 * Servlet容器提供了Session机制以跟踪用户；
 * 默认的Session机制是以Cookie形式实现的，Cookie名称为JSESSIONID；
 * 通过读写Cookie可以在客户端设置用户偏好等。
 * Cookie 保存在客户端，Session 是保存在服务端的 Cookie
 * Session 就是Cookie. 只不过，Session特殊一点，需要持久化。 如果你愿意Cookie也可以持久化，只不过多是重复信息。
 */
@WebServlet(urlPatterns="/indexServlet")
public class IndexServlet extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//从HttpSession获取当前用户名:
		String user = (String)req.getSession().getAttribute("user");
		String lang = parseLanguageFromCookie(req);
		
		resp.setContentType("text/html");
		resp.setCharacterEncoding("UTF-8");
		resp.setHeader("X-Powered-By", "JavaEE Servlet");
		PrintWriter pw = resp.getWriter();
		if ("zh".equals(lang)) {
			pw.write("<h1>你好, " + (user != null ? user : "Guest") + "</h1>");
			if (user == null) {
				//未登录,显示登录链接
				pw.write("<p><a href=\"/pcis/signin\">登录</a></p>");
			} else {
				//已登录,显示登出连接:
				pw.write("<p><a href=\"/pcis/signout\">登出</a></p>");
			}
		} else {
			pw.write("<h1>Welcome, " + (user != null ? user : "Guest") + "</h1>");
			if (user == null) {
				//未登录,显示登录链接
				pw.write("<p><a href=\"/pcis/signin\">SignIn</a></p>");
			} else {
				//已登录,显示登出连接:
				pw.write("<p><a href=\"/pcis/signout\">Sign Out</a></p>");
			}
		}
		pw.write("<p><a href=\"/pcis/pref?lang=en\">English</a>|<a href=\"/pcis/pref?lang=zh\">中文</a>");
		pw.flush();
	}
	
	
	private String parseLanguageFromCookie(HttpServletRequest req) {
		Cookie[] cookies = req.getCookies();// 获取请求附带的所有Cookie:可见，读取Cookie主要依靠遍历HttpServletRequest附带的所有Cookie。
		if (cookies != null) {// 如果获取到Cookie:
			for(Cookie cookie: cookies) {// 循环每个Cookie:
				if(cookie.getName().equals("lang")) {// 如果Cookie名称为lang:
					return cookie.getValue();// 返回Cookie的值:
				}
			}
		}
		return "en";// 返回默认值:
	}
}
