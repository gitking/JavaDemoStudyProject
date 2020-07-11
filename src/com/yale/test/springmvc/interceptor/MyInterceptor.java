package com.yale.test.springmvc.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class MyInterceptor implements HandlerInterceptor {
	/*
	 * 在请求处理的方法之前执行
	 * @see org.springframework.web.servlet.HandlerInterceptor#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//return HandlerInterceptor.super.preHandle(request, response, handler);
		System.out.println("-------------------SpringMVC拦截器处理前preHandle--------------------");
		return true;
		//response.sendRedirect(request.getContextPath()+"/index.jsp");//拦截请求,并将其重定向到一个错误的页面
		//return false;//如果返回false后面的流程就会被阻断后面的代码就不再执行了,返回true
	}
	
	/*
	 * 在url请求处理之后执行
	 * @see org.springframework.web.servlet.HandlerInterceptor#postHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.web.servlet.ModelAndView)
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		//HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
		System.out.println("-------------------SpringMVC拦截器处理后postHandle--------------------");
	}
	
	/*
	 * 在DispatcherServlet处理后执行-----清理工作
	 * @see org.springframework.web.servlet.HandlerInterceptor#afterCompletion(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		//HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
		System.out.println("-------------------SpringMVC拦截器处理后afterCompletion--------------------");

	}
}
