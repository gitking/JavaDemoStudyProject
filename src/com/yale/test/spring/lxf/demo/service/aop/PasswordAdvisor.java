package com.yale.test.spring.lxf.demo.service.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class PasswordAdvisor implements MethodInterceptor {
	
	//这个PasswordAdvisor将截获ServiceBean的getPassword()方法的返回值，并将其改为"***"。继续修改beans.xml：
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Object ret = invocation.proceed();
		if (ret == null) {
			return null;
		}
		String password = (String)ret;
		StringBuilder encrypt = new StringBuilder(password.length());
		for (int i=0; i<password.length(); i++) {
			encrypt.append("*");
		}
		return encrypt.toString();
	}
}
