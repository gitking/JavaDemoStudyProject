package com.yale.test.spring.aop;

import java.lang.reflect.Method;

import org.springframework.aop.AfterReturningAdvice;

/**
 * AfterReturningAdvice Spring的后置通知
 * @author dell
 */
public class AfterLog implements AfterReturningAdvice {

	@Override
	public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
		System.out.println("###请注意Spring的前置通知过来了###，你的返回值是:" + returnValue + "你是什么类:" + target.getClass().getName() + "的" + method.getName() + "方法被执行了");
	}
}
