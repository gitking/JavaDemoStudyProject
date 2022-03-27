package com.yale.test.spring.lxf.demo.service.aop;

import java.lang.reflect.Method;

import org.springframework.aop.MethodBeforeAdvice;

public class LogAdvisor implements MethodBeforeAdvice {
	
	@Override
	public void before(Method m, Object[] args, Object target) throws Throwable {
		System.out.println("SpringAOP面向方面/切面编程[Log]" + target.getClass().getName() + "." + m.getName() + "()");
	}
}
