package com.yale.test.springmvc.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/*
 * 过滤器和拦截器是的底层实现方式不大相同,过滤器是基于函数回调的,拦截器是基于Java的反射机制(动态代理)实现的.
 * 拦截器有点面向切面编程(AOP)的感觉.
 * 过滤器的使用要依赖于Tomcat等容器,导致他只能在web程序中使用.
 * 而拦截器是Struts,Spring的一个组件,并不依赖Tomcat等容器,是可以单独使用的。不仅能应用在web程序中,也可以用在
 * Application,Swing等程序中.
 * 过滤器和拦截器的触发时机也不同：
 * 1、过滤器Filter是在请求进入容器之后,但在进入servlet之前进行预处理,请求结束是在servlet处理完以后
 * 2、拦截器Interceptor是在请求进入servlet之后,在进入Controller之前进行预处理,Controller中渲染了对应的视图之后请求结束.
 * 
 */
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
