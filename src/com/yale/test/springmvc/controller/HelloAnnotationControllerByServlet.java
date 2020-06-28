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
异常处理，支持Resful风格，本地化，国际化，数据验证类型转换，拦截器
 */
@Controller
public class HelloAnnotationControllerByServlet {

	@RequestMapping("/helloSer")//这个也可以直接放到HelloAnnotationController类上面
	public void helloAnno(HttpServletRequest request, HttpServletResponse response) 
	throws Exception {
		//在页面上访问http://localhost:8080/pcis/helloSer.springmvc
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");//解决浏览器乱码
		//注意我这样写即使你springmvc-servlet.xml里面配置了InternalResourceViewResolver,我也用不到
		//因为我用的是原生的servlet来实现的
		response.getWriter().println("我也可以只使用SpringMVC的annotation注解,不适用SpringMVC的视图ModelAndView");
		//response.sendRedirect("/index.jsp");//重定向也是可以的
		//request.getRequestDispatcher("index.jsp").forward(request, response);//转发也可以
	}
}
