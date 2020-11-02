package com.yale.test.spring.aop.lxf.bug;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogginAspect {
	@Before("execution(public * com.yale.test.spring.aop.lxf.bug.service.UserService.*(..))")
	public void doAccessCheck() {
		System.err.println("[Before] do access check...");
	}
}
