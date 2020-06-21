package com.yale.test.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.yale.test.web.filter.request.EncodingRequestWrapper;

public class EncodeFilter implements Filter {
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	/**
	 * 用一个顾虑求解决全站乱码问题
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
			/**
			 * get请求有点不好处理,因为你不知道get传过的参数名字叫啥?
			 * 这个怎么办？自己写个类,然后将chain的request调包就行
			 * 1、写一个request的装饰类,然后增强request中的getParameter方法
			 * 2、在放行使用我们自己的request,
			 */
			HttpServletRequest hsp = (HttpServletRequest)request;
			if (hsp.getMethod().equalsIgnoreCase("get")) {//get方法
				//EncodingRequest eq = new EncodingRequest(hsp);//装饰者模式
				EncodingRequestWrapper eq = new EncodingRequestWrapper(hsp);//装饰者模式
				chain.doFilter(eq, response);//调包request对象
			} else {
				request.setCharacterEncoding("UTF-8");//解决post请求乱码问题
				chain.doFilter(request, response);//这里使用原来的request对象
			}
	}

	@Override
	public void destroy() {

	}
}
