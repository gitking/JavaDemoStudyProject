package com.yale.test.java.fanshe.proxy.cglib;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

class Message {
	public void send() {
		System.out.println("www.mldn.cn");
	}
}
class MyProxy implements MethodInterceptor {//定义一个拦截
	private Object target;
	public MyProxy(Object object) {
		this.target = object;
	}
	/**
	 * proxy代理对象
	 * mProxy方法代理
	 */
	@Override
	public Object intercept(Object proxy, Method method, Object[] arg2, MethodProxy mProxy) throws Throwable {
		this.prepare();
		Object ret = method.invoke(this.target, arg2);
		this.over();
		return null;
	}
	
	public void prepare() {
		System.out.println("【ProxySubject】打开电脑");
	}
	
	public void over() {
		System.out.println("【ProxySubject】关闭电源");
	}
}
public class ProxyCglibDemo {

	public static void main(String[] args) {
		Message msg = new Message();
		Enhancer enhancer = new Enhancer();//这是一个负责代理关系的操作程序类
		enhancer.setSuperclass(msg.getClass());//把本类的对象作为一个抽象
		enhancer.setCallback(new MyProxy(msg));//代理对象
		//以上就动态配置好了类之间的代理关系
		Message temp = (Message)enhancer.create();
		temp.send();
	}
}
