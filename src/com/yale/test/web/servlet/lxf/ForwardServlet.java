package com.yale.test.web.servlet.lxf;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 * 重定向与转发
 * 转发Forward
 * Forward是指内部转发。当一个Servlet处理请求的时候，它可以决定自己不继续处理，而是转发给另一个Servlet处理。
 * 例如，我们已经编写了一个能处理/hello的HelloServlet，继续编写一个能处理/morning的ForwardServlet：
 * ForwardServlet在收到请求后，它并不自己发送响应，而是把请求和响应都转发给路径为/hello的Servlet，即下面的代码：
 * 后续请求的处理实际上是由HelloServlet完成的。这种处理方式称为转发（Forward），我们用流程图画出来如下：
 *                            ┌────────────────────────┐
	                          │      ┌───────────────┐ │
	                          │ ────>│ForwardServlet │ │
	┌───────┐  GET /morning   │      └───────────────┘ │
	│Browser│ ──────────────> │              │         │
	│       │ <────────────── │              ▼         │
	└───────┘    200 <html>   │      ┌───────────────┐ │
	                          │ <────│ HelloServlet  │ │
	                          │      └───────────────┘ │
	                          │       Web Server       │
	                          └────────────────────────┘
	                          
 * 转发和重定向的区别在于，转发是在Web服务器内部完成的，对浏览器来说，它只发出了一个HTTP请求：
 * 注意到使用转发的时候，浏览器的地址栏路径仍然是/morning，浏览器并不知道该请求在Web服务器内部实际上做了一次转发。
 * 小结
 * 使用重定向时，浏览器知道重定向规则，并且会自动发起新的HTTP请求；
 * 使用转发时，浏览器并不知道服务器内部的转发逻辑。
 */
@WebServlet(urlPatterns="/morning")
public class ForwardServlet extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/helloLxf").forward(req, resp);
	}
}
