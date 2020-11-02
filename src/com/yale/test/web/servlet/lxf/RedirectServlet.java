package com.yale.test.web.servlet.lxf;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 * 重定向与转发
 * Redirect
 * 重定向是指当浏览器请求一个URL时，服务器返回一个重定向指令，告诉浏览器地址已经变了，麻烦使用新的URL再重新发送新请求。
 * 例如，我们已经编写了一个能处理/hello的HelloServlet，如果收到的路径为/hi，希望能重定向到/hello，可以再编写一个RedirectServlet：
 * @WebServlet(urlPatterns = "/hi")
	public class RedirectServlet extends HttpServlet {
	    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	        // 构造重定向的路径:
	        String name = req.getParameter("name");
	        String redirectToUrl = "/hello" + (name == null ? "" : "?name=" + name);
	        // 发送重定向响应:
	        resp.sendRedirect(redirectToUrl);
	    }
	}
 * 如果浏览器发送GET /hi请求，RedirectServlet将处理此请求。由于RedirectServlet在内部又发送了重定向响应，因此，浏览器会收到如下响应：
 * HTTP/1.1 302 Found
 * Location: /hello
 * 当浏览器收到302响应后，它会立刻根据Location的指示发送一个新的GET /hello请求，这个过程就是重定向：
 *  ┌───────┐   GET /hi     ┌───────────────┐
	│Browser│ ────────────> │RedirectServlet│
	│       │ <──────────── │               │
	└───────┘   302         └───────────────┘
	
	
	┌───────┐  GET /hello   ┌───────────────┐
	│Browser│ ────────────> │ HelloServlet  │
	│       │ <──────────── │               │
	└───────┘   200 <html>  └───────────────┘
 * 观察Chrome浏览器的网络请求，可以看到两次HTTP请求：
 * 并且浏览器的地址栏路径自动更新为/helloLxf。
 * 重定向有两种：一种是302响应，称为临时重定向，一种是301响应，称为永久重定向。两者的区别是，如果服务器发送301永久重定向响应，
 * 浏览器会缓存/hi到/hello这个重定向的关联，下次请求/hi的时候，浏览器就直接发送/hello请求了。
 * 重定向有什么作用？重定向的目的是当Web应用升级后，如果请求路径发生了变化，可以将原来的路径重定向到新路径，从而避免浏览器请求原路径找不到资源。
 * HttpServletResponse提供了快捷的redirect()方法实现302重定向。如果要实现301永久重定向，可以这么写：
 * resp.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY); // 301
 * resp.setHeader("Location", "/hello");
 */
@WebServlet(urlPatterns = "/hi")
public class RedirectServlet extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String name = req.getParameter("name");
		String redirectToUrl = "/pcis/helloLxf" + (name == null ? "" : "?name" + name);
		resp.sendRedirect(redirectToUrl);
	}
}
