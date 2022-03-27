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
 * 
 * 使用Cookie保持Session
 * 通常服务器使用Session来跟踪会话。Session的简单实现就是利用Cookie。当客户端第一次连接服务器时，服务器检测到客户端没有相应的Cookie字段，就发送一个包含一个识别码的Set-Cookie字段。
 * 在此后的会话过程中，客户端发送的请求都包含这个Cookie，因此服务器能够识别出客户端曾经连接过服务器。
 * 要实现与浏览器一样的效果，MIDP应用程序必须也能识别Cookie，并在每个请求头中包含此Cookie。
 * 在处理每次连接的响应中，我们都检查是否有Set-Cookie这个头，如果有，则是服务器第一次发送的Session ID，或者服务器认为会话超时，需要重新生成一个Session ID。如果检测到Set-Cookie头，就将其保存，并在随后的每次请求中附加它：
 * String session = null;
	String cookie = hc.getHeaderField("Set-Cookie");
	if(cookie!=null) {
	    int n = cookie.indexOf(';');
	    session = cookie.substring(0, n);
	}
 * 使用Sniffer程序可以捕获到不同的Web服务器发送的Session。WebLogic Server 7.0返回的Session如下：
 * Set-Cookie: JSESSIONID=CxP4FMwOJB06XCByBWfwZBQ0IfkroKO2W7FZpkLbmWsnERuN5u2L!-1200402410; path=/
 * 我们无须关心Session ID的内容，服务器自己会识别它。我们只需在随后的请求中附加上这个Session ID即可：
 * https://www.liaoxuefeng.com/article/895889887461120
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
