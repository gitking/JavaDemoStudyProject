package com.yale.test.spring.aop;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * 自定义AOP
 * @author dell
 */
public class AopBySelf {
	public void before() {
		System.out.println("AopBySelf,我没有实现Spring的接口,自定义AOP前置通知，只是我拿不到目标类的具体信息");
	}
	public void after() {
		System.out.println("AopBySelf,我没有实现Spring的接口,自定义AOP后置通知，只是我拿不到目标类的具体信息");
	}
}
