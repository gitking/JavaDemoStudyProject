package com.yale.test.web.filter.book;

import java.io.File;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yale.test.web.filter.response.StaticResponse;

/**
 * 把jsp转换成静态HTML
 * 阿里云  itcast传智播客 【Java Web开发】Filte（过滤器）、Listener（监听器） 课程
 * https://edu.aliyun.com/course/1709?spm=5176.10731542.0.0.6da52cacwoqZYm
 * @author dell
 */
public class StaticFilter implements Filter {
	private FilterConfig filterConfig;
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest hsp = (HttpServletRequest)request;
		HttpServletResponse hsr = (HttpServletResponse)response;
		/**
		 * 把bookServlet转发的jsp转换成静态的html
		 * 静态的html文件名字必须遵守一定的规则
		 * links.jsp这个页面的几个连接对应的以下几个html文件
		 * category有四种可能
		 * null --> null.html
		 * 1 --> 1.html
		 * 2 --> 2.html
		 * 3 --> 3.html
		 */
		String category = request.getParameter("category");
		String htmlPage = category + ".html";//拼成文件名称
		String htmlPath = this.filterConfig.getServletContext().getRealPath("/demo/bookmanager/htmls");//得到文件路径
		File destFile = new File(htmlPath, htmlPage);
		if (destFile.exists()) {//如果文件存在,把请求重定向到这个文件
			hsr.sendRedirect(hsp.getContextPath() + "/demo/bookmanager/htmls/" + htmlPage);
			return;
		} else {
			/**
			 * html文件不存在,filter要先放行,让BookServlet去处理并得到返回结果
			 * BookServlet拿到结果后不能让BookServlet把结果直接返回给浏览器,而是将结果输出到我们指定的html文件中
			 * 调包response,让他的getWriter()与一个html文件绑定,那么show.jsp的输出就到了html文件中
			 * 如果你生成的html页面打开乱码,那是因为你html文件里面没有这行代码<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
			 * 为什么你生成的html页面里面没有这句话呢?那是因为你的BookServlet转发的目标jsp页面里面没有这句话
			 */
			StaticResponse sr = new StaticResponse(hsr, destFile.getAbsolutePath());
			chain.doFilter(request, sr);//放行,让BookServlet拿着我们调包的StaticResponse去帮我们生成html文件
			hsr.sendRedirect(hsp.getContextPath() + "/demo/bookmanager/htmls/" + htmlPage);
		}
	}

	@Override
	public void destroy() {

	}
}
