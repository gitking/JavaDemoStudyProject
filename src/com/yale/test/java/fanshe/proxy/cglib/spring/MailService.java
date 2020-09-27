package com.yale.test.java.fanshe.proxy.cglib.spring;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@Component
public class MailService {
	
	/*
	 * 因为我们启用了AOP,用使用LoggingAspect对UserService这个目标了增强,所以Spring在往MailService类了注入UserService时
	 * 不能注入原始的UserService类,而是注入Spring的代理类UserService$$EnhancerBySpringCGLIB,所以MailService这里的userService
	 * 是Spring的代理类不是原始的UserService类了。
	 */
	@Autowired
	UserService userService;
	
	public String sendMail() {
		//ZoneId zoneId = userService.zoneId;这行代码在Spring启用AOP之后会报错,不要直接访问类的成员变量
		/*
		 * 这行代码无论启不启用Spring的AOP都不会报错
		 * 无论注入的UserService是原始实例还是代理实例，getZoneId()都能正常工作，因为代理类会覆写getZoneId()方法，并将其委托给原始实例：
		 * 注意到我们还给UserService添加了一个public+final的方法：
		 * 如果在MailService中，调用的不是getZoneId()，而是getFinalZoneId()，又会出现NullPointerException，
		 * 这是因为，代理类无法覆写final方法（这一点绕不过JVM的ClassLoader检查），该方法返回的是代理类的zoneId字段，即null
		 * 实际上，如果我们加上日志，Spring在启动时会打印一个警告：
		 * 10:43:09.929 [main] DEBUG org.springframework.aop.framework.CglibAopProxy - Final method [public final java.time.ZoneId xxx.UserService.getFinalZoneId()] cannot get proxied via CGLIB: Calls to this method will NOT be routed to the target instance and might lead to NPEs against uninitialized fields in the proxy instance.
		 * 上面的日志大意就是，因为被代理的UserService有一个final方法getFinalZoneId()，这会导致其他Bean如果调用此方法，无法将其代理到真正的原始实例，从而可能发生NPE异常。
		 * 因此，正确使用AOP，我们需要一个避坑指南：
    	 * 访问被注入的Bean时，总是调用方法而非直接访问字段；
    	 * 编写Bean时，如果可能会被代理，就不要编写public final方法。
    	 * 这样才能保证有没有AOP，代码都能正常工作。
    	 * 思考
    	 * 问:为什么Spring刻意不初始化Proxy继承的字段？
    	 * 我自己的思考:1，规范问题,本来java的封装特性就不鼓励直接通过类名.属性 直接访问属性,而是要求把属性用private封装起来,提供get方法来访问
    	 * 2、因为你初始化的时候很可能会用到注入的其他类
    	 * @Component
			public class MailService {
			    @Value("${smtp.from:xxx}")
			    String mailFrom;
			
			    SmtpSender sender;
			
			    @PostConstruct
			    public void init() {
			        sender = new SmtpSender(mailFrom, ...);
			    }
			
			    public void sentMail(String to) {
			        ...
			    }
			}
		 * 你看，MailService的字段sender初始化需要依赖其他注入，并且已经初始化了一次，proxy类没法正确初始化sender
		 * 主要原因就是spring无法在逻辑上正常初始化proxy的字段，所以干脆不初始化，并通过NPE直接暴露出来
		 * 问:如果一个Bean不允许任何AOP代理，应该怎么做来“保护”自己在运行期不会被代理？
		 * 答:将类设置为 final 的防止 cglib 创建Proxy，并且不继承接口防止 JDK 自带的动态代理
		 * 小结
		 * 由于Spring通过CGLIB实现代理类，我们要避免直接访问Bean的字段，以及由final方法带来的“未代理”问题。
		 * 遇到CglibAopProxy的相关日志，务必要仔细检查，防止因为AOP出现NPE异常。
		 */
		ZoneId zoneId = userService.getZoneId();
		System.out.println("UserService的成员变量zoneId是用final修饰的,竟然为变成null?" + zoneId);
		System.out.println("原因是Spring的AOP动态代理搞的鬼,我们来看看userService的真身长什么样?" + userService.getClass());
		System.out.println("你可以把AppConfig类上面的@EnableAspectJAutoProxy去掉和加上分别运行一次,看看userService的真身");
		String dt = ZonedDateTime.now(zoneId).toString();
		return " Hello, it is " + dt;
	}
}
