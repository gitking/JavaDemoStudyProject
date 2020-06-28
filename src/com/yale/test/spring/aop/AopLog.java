package com.yale.test.spring.aop;

import java.lang.reflect.Method;

import org.springframework.aop.MethodBeforeAdvice;

/**
 * MethodBeforeAdvice Spring提供的前置通知
 * @author dell
 */
public class AopLog implements MethodBeforeAdvice{
	@Override
	public void before(Method method, Object[] args, Object target) throws Throwable {
		System.out.println("###请注意Spring的前置通知过来了###" + target.getClass().getName() + "的" + method.getName() + "方法被执行了");
	}
}
