package com.yale.test.springmvc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

/**
 * 实现Spring的控制器Controller
 * @author dell
 * SpringMvc是一个轻量级的，基于请求响应的mvc框架。特点：简单、便捷、易学，比Struts2性能好太多了,天生和SpringMvc无缝继承。使用约定优于配置。能够进行Junit测试，支持Restful风格
 * 异常处理，支持Resful风格，本地化，国际化，数据验证类型转换，拦截器
 */
public class HelloController implements Controller {

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mv = new ModelAndView();//封装要显示到视图中的数据
		//addObject相当于request.setAttribute("msg", "hello springmvc")；
		mv.addObject("msg", "hello springmvc");
		/*
		 * 还有一种方式可以将数据返回给前台,就是使用ModelMap,代码见HelloDataController的方法getReqAndResData
		 */
		mv.setViewName("hello");//视图名,根据配置文件里面的配置找web-inf/jsp下面的hello.jsp
		return mv;
	}
}
