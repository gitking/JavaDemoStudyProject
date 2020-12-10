package com.yale.test.springmvc.interceptor.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.yale.test.springmvc.rest.entity.User;
import com.yale.test.springmvc.rest.service.UserService;
import com.yale.test.springmvc.rest.web.UserController;

/*
 * 使用Interceptor
 * 在Web程序中，注意到使用Filter的时候，Filter由Servlet容器管理，它在Spring MVC的Web应用程序中作用范围如下：
 *    		 │   ▲
	         ▼   │
	       ┌───────┐
	       │Filter1│
	       └───────┘
	         │   ▲
	         ▼   │
	       ┌───────┐
	┌ ─ ─ ─│Filter2│─ ─ ─ ─ ─ ─ ─ ─ ┐
	       └───────┘
	│        │   ▲                  │
	         ▼   │
	│ ┌─────────────────┐           │
	  │DispatcherServlet│<───┐
	│ └─────────────────┘    │      │
	   │              ┌────────────┐
	│  │              │ModelAndView││
	   │              └────────────┘
	│  │                     ▲      │
	   │    ┌───────────┐    │
	│  ├───>│Controller1│────┤      │
	   │    └───────────┘    │
	│  │                     │      │
	   │    ┌───────────┐    │
	│  └───>│Controller2│────┘      │
	        └───────────┘
	└ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┘
 * 上图虚线框就是Filter2的拦截范围，Filter组件实际上并不知道后续内部处理是通过Spring MVC提供的DispatcherServlet还是其他Servlet组件，因为Filter是Servlet规范定义的标准组件，它可以应用在任何基于Servlet的程序中。
 * 如果只基于Spring MVC开发应用程序，还可以使用Spring MVC提供的一种功能类似Filter的拦截器：Interceptor。和Filter相比，Interceptor拦截范围不是后续整个处理流程，而是仅针对Controller拦截：
 *  	   │   ▲
	       ▼   │
	     ┌───────┐
	     │Filter1│
	     └───────┘
	       │   ▲
	       ▼   │
	     ┌───────┐
	     │Filter2│
	     └───────┘
	       │   ▲
	       ▼   │
	┌─────────────────┐
	│DispatcherServlet│<───┐
	└─────────────────┘    │
	 │              ┌────────────┐
	 │              │ModelAndView│
	 │              └────────────┘
	 │ ┌ ─ ─ ─ ─ ─ ─ ─ ─ ┐ ▲
	 │    ┌───────────┐    │
	 ├─┼─>│Controller1│──┼─┤
	 │    └───────────┘    │
	 │ │                 │ │
	 │    ┌───────────┐    │
	 └─┼─>│Controller2│──┼─┘
	      └───────────┘
	   └ ─ ─ ─ ─ ─ ─ ─ ─ ┘
 * 上图虚线框就是Interceptor的拦截范围，注意到Controller的处理方法一般都类似这样：
 * @Controller
	public class Controller1 {
	    @GetMapping("/path/to/hello")
	    ModelAndView hello() {
	        ...
	    }
	}
 * 所以，Interceptor的拦截范围其实就是Controller方法，它实际上就相当于基于AOP的方法拦截。因为Interceptor只拦截Controller方法，所以要注意，返回ModelAndView后，后续对View的渲染就脱离了Interceptor的拦截范围。
 * 使用Interceptor的好处是Interceptor本身是Spring管理的Bean，因此注入任意Bean都非常简单。此外，可以应用多个Interceptor，并通过简单的@Order指定顺序。我们先写一个LoggerInterceptor：
 * 一个Interceptor必须实现HandlerInterceptor接口，可以选择实现preHandle()、postHandle()和afterCompletion()方法。
 * preHandle()是Controller方法调用前执行，postHandle()是Controller方法正常返回后执行，而afterCompletion()无论Controller方法是否抛异常都会执行，参数ex就是Controller方法抛出的异常（未抛出异常是null）。
 * 在preHandle()中，也可以直接处理响应，然后返回false表示无需调用Controller方法继续处理了，通常在认证或者安全检查失败时直接返回错误响应。
 * 在postHandle()中，因为捕获了Controller方法返回的ModelAndView，所以可以继续往ModelAndView里添加一些通用数据，很多页面需要的全局数据如Copyright信息等都可以放到这里，无需在每个Controller方法中重复添加。
 * 我们再继续添加一个AuthInterceptor，用于替代上一节使用AuthFilter进行Basic认证的功能：
 * 这个AuthInterceptor是由Spring容器直接管理的，因此注入UserService非常方便。
 * 最后，要让拦截器生效，我们在WebMvcConfigurer中注册所有的Interceptor：
 * 如果拦截器没有生效，请检查是否忘了在WebMvcConfigurer中注册。 
 * 小结
 * Spring MVC提供了Interceptor组件来拦截Controller方法，使用时要注意Interceptor的作用范围。
 */
@Order(2)
@Component
public class AuthInterceptor implements HandlerInterceptor {
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	UserService userService;
	
	/*
	 * 在preHandle()中，也可以直接处理响应，然后返回false表示无需调用Controller方法继续处理了，通常在认证或者安全检查失败时直接返回错误响应。
	 * @see org.springframework.web.servlet.HandlerInterceptor#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		logger.info("pre authenticate {}...", request.getRequestURI());
		try {
			authenticateByHeader(request);
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("login by authorization header failed.", e);
		}
		return true;
	}
	
	private void authenticateByHeader(HttpServletRequest req) throws UnsupportedEncodingException {
		String authHeader = req.getHeader("Authorization");
		if (authHeader != null && authHeader.startsWith("Basic ")) {
			logger.info("try authenticate by authorization header...");
			String up = new String(Base64.getDecoder().decode(authHeader.substring(6)), "UTF-8");
			int pos = up.indexOf(':');
			if (pos > 0) {
				String email = URLDecoder.decode(up.substring(0, pos), "UTF-8");
				String password = URLDecoder.decode(up.substring(pos +1), "UTF-8");
				User user = userService.signin(email, password);
				req.getSession().setAttribute(UserController.KEY_USER, user);
				logger.info("user {} login by authorization header ok.", email);
			}
		}
	}
}
