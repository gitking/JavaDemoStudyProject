package com.yale.test.java.fanshe.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

interface Hello {
	void morning(String name);
}
public class ProxyEasyDemo {
	public static void main(String[] args) {
		InvocationHandler handler = new InvocationHandler(){
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				System.out.println("方法名字:" + method.getName());
				if (method.getName().equals("morning")) {
					System.out.println("Good morning, " + args[0]);
				}
				return null;
			}
		};
		Hello hello = (Hello)Proxy.newProxyInstance(Hello.class.getClassLoader(), new Class[]{Hello.class}, handler);
		hello.morning("廖雪峰");
	}
}
