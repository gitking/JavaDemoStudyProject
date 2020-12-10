package com.yale.test.springmvc.i18n.web;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;

/*
 * 不要忘了在WebMvcConfigurer中注册MvcInterceptor。现在，就可以在View中调用MessageSource.getMessage()方法来实现多语言：
 */
@Component
public class MvcInterceptor implements HandlerInterceptor{
	
	@Autowired
	LocaleResolver localeResolver;
	
	@Autowired
	@Qualifier("i18n")// 注意注入的MessageSource名称是i18n:
	MessageSource messageSource;
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if (modelAndView != null) {
            // 解析用户的Locale:
			Locale locale = localeResolver.resolveLocale(request);
			 // 放入Model:
			modelAndView.addObject("__messageSource__", messageSource);
			modelAndView.addObject("__locale__", locale);
		}
	}
}
