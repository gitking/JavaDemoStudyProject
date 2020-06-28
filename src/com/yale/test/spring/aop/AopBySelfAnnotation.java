package com.yale.test.spring.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * 自定义AOP
 * @author dell
 */
@Aspect//Aspect表示这个类是一个切面,AopBySelfAnnotation这个类要配置到Spring的bean里面
public class AopBySelfAnnotation {
	@Before("execution(* com.yale.test.spring.services.impl.CollgeServiceImpl.*(..))")
	public void before() {
		System.out.println("AopBySelfAnnotation注解 我没有实现Spring的接口,自定义AOP前置通知，只是我拿不到目标类的具体信息");
	}
	@After("execution(* com.yale.test.spring.services.impl.CollgeServiceImpl.*(..))")
	public void after() {
		System.out.println("AopBySelfAnnotation 我没有实现Spring的接口,自定义AOP后置通知，只是我拿不到目标类的具体信息");
	}
	@Around("execution(* com.yale.test.spring.services.impl.CollgeServiceImpl.*(..))")
	public Object around(ProceedingJoinPoint pj) throws Throwable {//环绕通知是把目标方法拦截了,在执行前打印一句话,打印完你要执行方法
		System.out.println("AopBySelfAnnotation注解环绕通知前");
		System.out.println("方法签名::" + pj.getSignature());
		Object obj = pj.proceed();//执行目标方法
		System.out.println("AopBySelfAnnotation注解环绕通知后,可以拿到你的返回值: " + obj);
		return obj;
	}
}
