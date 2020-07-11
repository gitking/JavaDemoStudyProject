package com.yale.test.springmvc.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import com.yale.test.springmvc.vo.User;

public class LoginInterceptor implements HandlerInterceptor {
	
	private List<String> allowList;//允许通过的URL列表
	/*
	 * 允许哪些URL不被拦截,哪些需要被拦截
	 * @see org.springframework.web.servlet.HandlerInterceptor#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		System.out.println("SrpingMVC登录拦截,LoginInterceptor");
		User user = (User)request.getSession().getAttribute("user");
		if (user == null) {
			String url = request.getRequestURL().toString();
			for (String temp: allowList) {
				if (url.endsWith(temp)) {//url以temp结尾
					return true;
				}
			}
			response.sendRedirect(request.getContextPath() + "/index.jsp");
			return false;		
		} else {
			return true;
		}
	}
	
	public void setAllowList(List<String> allowList) {
		this.allowList = allowList;
	}
}
