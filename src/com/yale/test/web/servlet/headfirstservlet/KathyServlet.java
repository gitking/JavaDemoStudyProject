package com.yale.test.web.servlet.headfirstservlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/*
 * servlet是放在HTTP WEB服务器上面运行的Java程序。
* 如果你本来就熟悉使用Perl来编写的CGI,那你就会知道我们说的servlet是什么。网页开发者使用CGI或servlet来操作用户提交(submit)给服务器的信息。
* 而servlet也可以使用RMI,最常见的J2EE技术混合了servlet和EJB,前者是后者的用户。此时,servlet是通过RMI来与EJB通信的。
* Servlet并不是Java标准函数库的一部分,你需要包装成servlets.jar的文件。这可从java.sun.com下载,或者从默认好可执行Java的网页服务器(例如在apache.org网站的Apache Tomcat)。没有这些
* 你将无法编译servlet。
* JSP代表(Java Server Pages),jsp本质上也是一个模板引擎。
* @author dell
*/
public class KathyServlet extends HttpServlet{
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException{
		String title = "PhraseOMatic has generated the following phrase.";
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<HTML><HEAD><TITLE>");
		out.println("PhraseOmatic");
		out.println("</TITLE></HEAD><BODY>");
		out.println("<H1>" + title + "</H1>");
		out.println("<P><a href=\"KathyServlet\">make another phrase</a></p>");
		out.println("</BODY></HTML>");
		out.close();
	}
}
