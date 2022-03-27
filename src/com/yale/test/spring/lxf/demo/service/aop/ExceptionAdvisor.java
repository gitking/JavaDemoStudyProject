package com.yale.test.spring.lxf.demo.service.aop;

import org.springframework.aop.ThrowsAdvice;

public class ExceptionAdvisor implements ThrowsAdvice {
	
	//@Override
	public void afterThrowing(RuntimeException re) throws Throwable {
		System.out.println("[Exception] " + re.getMessage());
	}
}
