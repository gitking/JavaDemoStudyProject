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
