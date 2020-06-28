package com.yale.test.web.filter;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class FilterAll implements Filter {
	private FilterConfig fliterConfig;
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		System.out.println("注意看tomcat启动的日志");
		this.fliterConfig = filterConfig;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		//使用servlet进行的转发,filter拦截不到,只能拦截浏览器上面的转发,但是使用servlet进行的重定向filter可以拦截到,因为即使你使用servlet进行重定向,也是在浏览器上重新
		//发送请求,所以就相当于浏览器发起的请求,filter是可以拦截到的
		HttpServletRequest htp=(HttpServletRequest)request;
		String requestUrl = htp.getRequestURL().toString();
		System.out.println("您的请求路径为:" + requestUrl);
		System.out.println("filter的执行顺序是,跟servlet一样,先精准匹配再模糊匹配");
		//ServletContext sc = request.getServletContext();
		ServletContext sc = this.fliterConfig.getServletContext();
		Map<String, Integer> map = (Map<String, Integer>)sc.getAttribute("map");
		String ipAddr = request.getRemoteAddr();//获取IP地址
		if (map.containsKey(ipAddr)) {
			int count  = map.get(ipAddr);
			map.put(ipAddr, count + 1);
		} else {
			map.put(ipAddr, 1);//第一次访问
		}
		sc.setAttribute("map", map);//其实这里放不放都行,因为map是引用对象,你改了sc对象里面的自动就改了
		chain.doFilter(request, response);//放行
		System.out.println("filter的执行顺序是,跟servlet一样,先精准匹配再模糊匹配");
	}

	@Override
	public void destroy() {
		System.out.println("正常关闭tomcat试试");
	}
}
