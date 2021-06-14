package com.yale.test.java.fanshe.proxy.cglib;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
interface MsgInter {
	void msgDemo();
}
class Message implements MsgInter{
	public void send() {
		System.out.println("www.mldn.cn");
	}
	public void msgDemo() {
		System.out.println("不管Message这个类有没有实现接口,cglib都可以代理,但是JDK原生的代理,要求Message必须实现接口,.www.mldn.cn");
	}
}
//CGlib是基于继承重写实现的代理，因此要求Class必须是非final class 与此同时被代理的方法必须是非final方法，因此final方法无法被子类重写，因此就无法代理。
//Spring是否支持对静态方法进行Aop增强 https://blog.csdn.net/Dax1n/article/details/105684685
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
		System.out.println("CGLIB代理开始了,【ProxySubject】打开电脑");
	}
	
	public void over() {
		System.out.println("CGLIB代理结束了,【ProxySubject】关闭电源");
	}
}
public class ProxyCglibDemo {
	public static void main(String[] args) {
		Message msg = new Message();
		System.out.println("原始对象:" + msg.getClass());
		Enhancer enhancer = new Enhancer();//这是一个负责代理关系的操作程序类
		enhancer.setSuperclass(msg.getClass());//把本类的对象作为一个抽象
		enhancer.setCallback(new MyProxy(msg));//代理对象
		//以上就动态配置好了类之间的代理关系
		Message temp = (Message)enhancer.create();
		System.out.println("CGLIB动态生成的代理对象:" + temp.getClass());
		temp.send();
		temp.msgDemo();
		
		/*
		 * 下面这些用法参见spring文档《pring-reference.pdf》
		 * https://docs.spring.io/spring-framework/docs/2.5.6/spring-reference.pdf 第6章节:6.6. Proxying mechanisms
		 * 或者看我自己百度云盘上面的Spring.docx文档2021年5月10日21:15:30
		 * ProxyFactory factory = new ProxyFactory(new SimplePojo());
			factory.addInterface(Pojo.class);
			factory.addAdvice(new RetryAdvice());
			Pojo pojo = (Pojo) factory.getProxy();
			// this is a method call on the proxy!
			pojo.foo();
			((Pojo) AopContext.currentProxy()).bar();
		 * ProxyFactory factory = new ProxyFactory(new SimplePojo());
		 * factory.adddInterface(Pojo.class);
		 * factory.addAdvice(new RetryAdvice());
		 * factory.setExposeProxy(true);
		 */
	}
}
