package com.yale.test.java.fanshe.proxy.cglib.spring.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/*
 * 下一步，我们给UserService加上AOP支持，就添加一个最简单的LoggingAspect：
 * 然后在AppConfig上加上@EnableAspectJAutoProxy。再次运行，不出意外的话，会得到一个NullPointerException：
 * 怎么回事？
 * 为什么加了AOP就报NPE，去了AOP就一切正常？final字段不执行，难道JVM有问题？为了解答这个诡异的问题，我们需要深入理解Spring使用CGLIB生成Proxy的原理：
 * 第 一步，正常创建一个UserService的原始实例，这是通过反射调用构造方法实现的，它的行为和我们预期的完全一致；
 * 第二步，通过CGLIB创建一个UserService的子类，并引用了原始实例和LoggingAspect
 * public UserService$$EnhancerBySpringCGLIB extends UserService {//Spring动态生成的UserService的代理类
	    UserService target;//真实的目标类
	    LoggingAspect aspect;//AOP的类
	
	    public UserService$$EnhancerBySpringCGLIB() {
	    }
	
	    public ZoneId getZoneId() {//代理类重写父类的方法
	        aspect.doAccessCheck();//AOP增强代码
	        return target.getZoneId();//调用真实目标类的方法
	    }
	}
 * 如果我们观察Spring创建的AOP代理，它的类名总是类似UserService$$EnhancerBySpringCGLIB$$1c76af9d（你没看错，Java的类名实际上允许$字符）。为了让调用方获得UserService的引用，它必须继承自UserService。然后，该代理类会覆写所有public和protected方法，并在内部将调用委托给原始的UserService实例。
 *	这里出现了两个UserService实例：
 *	一个是我们代码中定义的原始实例，它的成员变量已经按照我们预期的方式被初始化完成：UserService original = new UserService();
 *  第二个UserService实例实际上类型是UserService$$EnhancerBySpringCGLIB，它引用了原始的UserService实例：
 *  UserService$$EnhancerBySpringCGLIB proxy = new UserService$$EnhancerBySpringCGLIB();
 *	proxy.target = original;
 *	proxy.aspect = ...
 *  注意到这种情况仅出现在启用了AOP的情况，此刻，从ApplicationContext中获取的UserService实例是proxy，注入到MailService中的UserService实例也是proxy。
 *  那么最终的问题来了：proxy实例的成员变量，也就是从UserService继承的zoneId，它的值是null。
 *  在UserService$$EnhancerBySpringCGLIB中，并未执行。原因是，没必要初始化proxy的成员变量，因为proxy的目的是代理方法。
 * https://zhuanlan.zhihu.com/p/131584403
 */
@Aspect
@Component
public class LoggingAspect {
	
	@Before("execution(public * com..*.UserService.*(..))")//切入UserService类
	public void doAccessCheck() {
		System.err.println("[Before] do access check...");
	}
}
