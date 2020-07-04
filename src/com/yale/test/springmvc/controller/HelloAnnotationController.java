package com.yale.test.springmvc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 使用注解的方式,就不需要实现Spring的控制器Controller了
 * @author dell
 * SpringMvc是一个轻量级的，基于请求响应的mvc框架。特点：简单、便捷、易学，比Struts2性能好太多了,天生和SpringMvc无缝继承。使用约定优于配置。能够进行Junit测试，支持Restful风格
 * 异常处理，支持Resful风格，本地化，国际化，数据验证类型转换，拦截器
 */
@Controller
public class HelloAnnotationController {

	@RequestMapping("/helloAnno")//这个也可以直接放到HelloAnnotationController类上面
	public ModelAndView helloAnno(HttpServletRequest request, HttpServletResponse response) 
	throws Exception {
		//在页面上访问http://localhost:8080/pcis/helloAnno.springmvc
		ModelAndView mv = new ModelAndView();//封装要显示到视图中的数据
		mv.addObject("msg", "hello annotation(注解) springmvc");
		mv.setViewName("helloAnno");//视图名,根据配置文件里面的配置InternalResourceViewResolver找web-inf/jsp下面的hello.jsp
		return mv;
	}
	
	@RequestMapping("/helloDis")
	public String dispatcher() {
		//SpringMVC实现转发,就是这么简单
		//这种方式的意思是你配了SpringMvc的视图解析InternalResourceViewResolver就用SpringMvc的视图解析InternalResourceViewResolver
		//没配置就按照servlet的方式转发,这个时候你需要使用后缀的方式return index.jsp 
		//return "index";
		//return "forward:index.jsp";//也可以使用这种方式转发,这种方式的意思是不使用SpringMvc的视图解析InternalResourceViewResolver
		return "redirect:index.jsp";//也可以使用这种方式转发,这种方式的意思是不使用SpringMvc的视图解析InternalResourceViewResolver
	}
	
	@RequestMapping("/helloRe")
	public String redirect() {
		/*
		 * SpringMVC重定向,注意其实Springmvc的重定向用不到SpringMvc的视图解析InternalResourceViewResolver
		 * 因为重定向就相当于在页面上重新发起一个请求,你注意,重新发起的请求一样会被Spring的DispatcherServlet拦截到,然后又走一遍Spring的请求流程
		 */
		return "redirect:index.jsp";
	}
}
