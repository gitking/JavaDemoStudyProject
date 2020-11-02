package com.yale.test.spring.aop.lxf.bug;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import com.yale.test.spring.aop.lxf.bug.service.MailService;

/*
 * AOP避坑指南
 * 无论是使用AspectJ语法，还是配合Annotation，使用AOP，实际上就是让Spring自动为我们创建一个Proxy，使得调用方能无感知地调用指定方法，但运行期却动态“织入”了其他逻辑，因此，AOP本质上就是一个代理模式。
 * 因为Spring使用了CGLIB来实现运行期动态创建Proxy，如果我们没能深入理解其运行原理和实现机制，就极有可能遇到各种诡异的问题。
 * 我们来看一个实际的例子。
 * 假设我们定义了一个UserService的Bean：
 * 再写个MailService，并注入UserService：
 * 最后用main()方法测试一下：
 * 查看输出，一切正常：
 * 下一步，我们给UserService加上AOP支持，就添加一个最简单的LoggingAspect：
 * 别忘了在AppConfig上加上@EnableAspectJAutoProxy。再次运行，不出意外的话，会得到一个NullPointerException：
 * 仔细跟踪代码，会发现null值出现在MailService.sendMail()内部的这一行代码：
 * 我们还故意在UserService中特意用final修饰了一下成员变量：
 * 用final标注的成员变量为null？逗我呢？
 * 怎么肥四？
 * 为什么加了AOP就报NPE，去了AOP就一切正常？final字段不执行，难道JVM有问题？为了解答这个诡异的问题，我们需要深入理解Spring使用CGLIB生成Proxy的原理：
 * 第一步，正常创建一个UserService的原始实例，这是通过反射调用构造方法实现的，它的行为和我们预期的完全一致；
 * 第二步，通过CGLIB创建一个UserService的子类，并引用了原始实例和LoggingAspect：
 * public UserService$$EnhancerBySpringCGLIB extends UserService {
	    UserService target;
	    LoggingAspect aspect;
	
	    public UserService$$EnhancerBySpringCGLIB() {
	    }
	
	    public ZoneId getZoneId() {
	        aspect.doAccessCheck();
	        return target.getZoneId();
	    }
	}
 * 如果我们观察Spring创建的AOP代理，它的类名总是类似UserService$$EnhancerBySpringCGLIB$$1c76af9d（你没看错，Java的类名实际上允许$字符）。
 * 为了让调用方获得UserService的引用，它必须继承自UserService。然后，该代理类会覆写所有public和protected方法，并在内部将调用委托给原始的UserService实例。 
 * 这里出现了两个UserService实例：
 * 一个是我们代码中定义的原始实例，它的成员变量已经按照我们预期的方式被初始化完成：
 * UserService original = new UserService();
 * 第二个UserService实例实际上类型是UserService$$EnhancerBySpringCGLIB，它引用了原始的UserService实例：
 * UserService$$EnhancerBySpringCGLIB proxy = new UserService$$EnhancerBySpringCGLIB();
	proxy.target = original;
	proxy.aspect = ..
 * 注意到这种情况仅出现在启用了AOP的情况，此刻，从ApplicationContext中获取的UserService实例是proxy，注入到MailService中的UserService实例也是proxy。
 * 那么最终的问题来了：proxy实例的成员变量，也就是从UserService继承的zoneId，它的值是null。
 * 原因在于，UserService成员变量的初始化：
 * public class UserService {
	    public final ZoneId zoneId = ZoneId.systemDefault();
	    ...
	}
 * 在UserService$$EnhancerBySpringCGLIB中，并未执行。原因是，没必要初始化proxy的成员变量，因为proxy的目的是代理方法。
 * 实际上，成员变量的初始化是在构造方法中完成的。这是我们看到的代码：
 * public class UserService {
	    public final ZoneId zoneId = ZoneId.systemDefault();
	    public UserService() {
	    }
	}
 * 这是编译器实际编译的代码：
 * public class UserService {
	    public final ZoneId zoneId;
	    public UserService() {
	        super(); // 构造方法的第一行代码总是调用super()
	        zoneId = ZoneId.systemDefault(); // 继续初始化成员变量
	    }
	}
 * 然而，对于Spring通过CGLIB动态创建的UserService$$EnhancerBySpringCGLIB代理类，它的构造方法中，并未调用super()，因此，从父类继承的成员变量，包括final类型的成员变量，统统都没有初始化。
 * 有的童鞋会问：Java语言规定，任何类的构造方法，第一行必须调用super()，如果没有，编译器会自动加上，怎么Spring的CGLIB就可以搞特殊？
 * 这是因为自动加super()的功能是Java编译器实现的，它发现你没加，就自动给加上，发现你加错了，就报编译错误。但实际上，如果直接构造字节码，一个类的构造方法中，不一定非要调用super()。Spring使用CGLIB构造的Proxy类，是直接生成字节码，并没有源码-编译-字节码这个步骤，因此：
 *  Spring通过CGLIB创建的代理类，不会初始化代理类自身继承的任何成员变量，包括final类型的成员变量！ 
 *  再考察MailService的代码：
 *  @Component
	public class MailService {
	    @Autowired
	    UserService userService;
	
	    public String sendMail() {
	        ZoneId zoneId = userService.zoneId;
	        System.out.println(zoneId); // null
	        ...
	    }
	}
 *  如果没有启用AOP，注入的是原始的UserService实例，那么一切正常，因为UserService实例的zoneId字段已经被正确初始化了。
 *  如果启动了AOP，注入的是代理后的UserService$$EnhancerBySpringCGLIB实例，那么问题大了：获取的UserService$$EnhancerBySpringCGLIB实例的zoneId字段，永远为null。
 *  那么问题来了：启用了AOP，如何修复？
 *  修复很简单，只需要把直接访问字段的代码，改为通过方法访问：
 *  @Component
	public class MailService {
	    @Autowired
	    UserService userService;
	
	    public String sendMail() {
	        // 不要直接访问UserService的字段:
	        ZoneId zoneId = userService.getZoneId();
	        ...
	    }
	}
 * 无论注入的UserService是原始实例还是代理实例，getZoneId()都能正常工作，因为代理类会覆写getZoneId()方法，并将其委托给原始实例：
 * public UserService$$EnhancerBySpringCGLIB extends UserService {
	    UserService target = ...
	    ...
	
	    public ZoneId getZoneId() {
	        return target.getZoneId();
	    }
	}
 * 注意到我们还给UserService添加了一个public+final的方法：
 * @Component
	public class UserService {
	    ...
	    public final ZoneId getFinalZoneId() {
	        return zoneId;
	    }
	}
 * 如果在MailService中，调用的不是getZoneId()，而是getFinalZoneId()，又会出现NullPointerException，这是因为，代理类无法覆写final方法（这一点绕不过JVM的ClassLoader检查），该方法返回的是代理类的zoneId字段，即null。
 * 实际上，如果我们加上日志，Spring在启动时会打印一个警告：
 * 上面的日志大意就是，因为被代理的UserService有一个final方法getFinalZoneId()，这会导致其他Bean如果调用此方法，无法将其代理到真正的原始实例，从而可能发生NPE异常。
 * 因此，正确使用AOP，我们需要一个避坑指南：
 * 1.访问被注入的Bean时，总是调用方法而非直接访问字段；
 * 2.编写Bean时，如果可能会被代理，就不要编写public final方法。
 * 这样才能保证有没有AOP，代码都能正常工作。
 * 思考
 * 为什么Spring刻意不初始化Proxy继承的字段？
 * 如果一个Bean不允许任何AOP代理，应该怎么做来“保护”自己在运行期不会被代理？
 * 答:final 类
 * 小结
 * 由于Spring通过CGLIB实现代理类，我们要避免直接访问Bean的字段，以及由final方法带来的“未代理”问题。
 * 遇到CglibAopProxy的相关日志，务必要仔细检查，防止因为AOP出现NPE异常。
 * 问:请问老师，假如是在UserService的私有方法上打上了注解，代理类对这个私有方法怎么处理呢，切面的通知方法能正常执行吗？
 * 答:私有方法可以通过反射调用，但是AOP也是别人写代码写出来的逻辑，会不会代理私有方法呢？我认为不会，因为如果我要开发一个AOP框架，我不会管私有方法。你可以验证一下。
 */
@Configuration
@ComponentScan
@EnableAspectJAutoProxy
public class AppConfig {
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		MailService mailService = context.getBean(MailService.class);
		System.out.println(mailService.sendMail());
	}
}
