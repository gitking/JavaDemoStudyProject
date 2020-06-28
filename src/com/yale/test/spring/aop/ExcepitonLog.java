package com.yale.test.spring.aop;

import java.lang.reflect.Method;

import org.springframework.aop.ThrowsAdvice;

/**
 * 异常通知,这个Spring的接口ThrowsAdvice没有抽象方法,是一个标识接口,继承AfterAdvice
 * @author dell
 */
public class ExcepitonLog implements ThrowsAdvice {
	public void afterThrowing(Method mthod, Object target, Exception e) {
		
	}
}
