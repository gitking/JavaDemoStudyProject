package com.yale.test.springmvc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * 使用注解的方式,就不需要实现Spring的控制器Controller了
 * @author dell
 * SpringMvc是一个轻量级的，基于请求响应的mvc框架。特点：简单、便捷、易学，比Struts2性能好太多了,天生和SpringMvc无缝继承。使用约定优于配置。能够进行Junit测试，支持Restful风格
 * 异常处理，支持Resful风格，本地化，国际化，数据验证类型转换，拦截器
 */
@Controller
@RequestMapping("/springMethod")
public class HelloAnnotationControllerMethod {
	
	@RequestMapping(params="method=add", method=RequestMethod.GET)
	public String add() {
		System.out.println("method=RequestMethod.GET,这行代码不是必须的,这里如果你指定了Post的提交方式,那么就只能用Post的方式才能访问到");
		//http://localhost:8080/pcis/springMethod.springmvc?method=add
		System.out.println("add方法被调用了");
		return "redirect:/springmvc/index.jsp";
	}
	
	@RequestMapping(params="method=update")
	public String update() {
		System.out.println("update方法被调用了");
		return "redirect:/index.jsp";
	}
	
	@RequestMapping(params="method=delete")
	public String delete() {
		System.out.println("delete方法被调用了");
		return "redirect:/index.jsp";
	}
	
	@RequestMapping(params="method=select")
	public String select() {
		System.out.println("select方法被调用了");
		return "redirect:/index.jsp";
	}
}
