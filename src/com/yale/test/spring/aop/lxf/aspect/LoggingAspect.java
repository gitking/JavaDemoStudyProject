package com.yale.test.spring.aop.lxf.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/*
 * 观察doAccessCheck()方法，我们定义了一个@Before注解，后面的字符串是告诉AspectJ应该在何处执行该方法，
 * 这里写的意思是：执行UserService的每个public方法前执行doAccessCheck()代码。
 * 再观察doLogging()方法，我们定义了一个@Around注解，它和@Before不同，@Around可以决定是否执行目标方法，因此，我们在doLogging()内部先打印日志，再调用方法，最后打印日志后返回结果。
 * 在LoggingAspect类的声明处，除了用@Component表示它本身也是一个Bean外，我们再加上@Aspect注解，表示它的@Before标注的方法需要注入到UserService的每个public方法执行前，
 * @Around标注的方法需要注入到MailService的每个public方法执行前后。
 * 有些童鞋会问，LoggingAspect定义的方法，是如何注入到其他Bean的呢？
 * 其实AOP的原理非常简单。我们以LoggingAspect.doAccessCheck()为例，要把它注入到UserService的每个public方法中，最简单的方法是编写一个子类，并持有原始实例的引用：
 * public UserServiceAopProxy extends UserService {
	    private UserService target;
	    private LoggingAspect aspect;
	
	    public UserServiceAopProxy(UserService target, LoggingAspect aspect) {
	        this.target = target;
	        this.aspect = aspect;
	    }
	
	    public User login(String email, String password) {
	        // 先执行Aspect的代码:
	        aspect.doAccessCheck();
	        // 再执行UserService的逻辑:
	        return target.login(email, password);
	    }
	
	    public User register(String email, String password, String name) {
	        aspect.doAccessCheck();
	        return target.register(email, password, name);
	    }
	
	    ...
	}
 * 这些都是Spring容器启动时为我们自动创建的注入了Aspect的子类，它取代了原始的UserService（原始的UserService实例作为内部变量隐藏在UserServiceAopProxy中）。
 * 如果我们打印从Spring容器获取的UserService实例类型，它类似UserService$$EnhancerBySpringCGLIB$$1f44e01c，实际上是Spring使用CGLIB动态创建的子类，但对于调用方来说，感觉不到任何区别。
 *  Spring对接口类型使用JDK动态代理，对普通类使用CGLIB创建子类。如果一个Bean的class是final，Spring将无法为其创建子类。 
 *  可见，虽然Spring容器内部实现AOP的逻辑比较复杂（需要使用AspectJ解析注解，并通过CGLIB实现代理类），但我们使用AOP非常简单，一共需要三步：
 *  1.定义执行方法，并在方法上通过AspectJ的注解告诉Spring应该在何处调用此方法；
 *  2.标记@Component和@Aspect；
 *  3.在@Configuration类上标注@EnableAspectJAutoProxy。
 *  至于AspectJ的注入语法则比较复杂，请参考Spring文档(https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#aop-pointcuts-examples)。
 *  Spring也提供其他方法来装配AOP，但都没有使用AspectJ注解的方式来得简洁明了，所以我们不再作介绍。
 * 拦截器类型
 * 顾名思义，拦截器有以下类型：
 * @Before：这种拦截器先执行拦截代码，再执行目标代码。如果拦截器抛异常，那么目标代码就不执行了；
 * @After：这种拦截器先执行目标代码，再执行拦截器代码。无论目标代码是否抛异常，拦截器代码都会执行；
 * @AfterReturning：和@After不同的是，只有当目标代码正常返回时，才执行拦截器代码；
 * @AfterThrowing：和@After不同的是，只有当目标代码抛出了异常时，才执行拦截器代码；
 * @Around：能完全控制目标代码是否执行，并可以在执行前后、抛异常后执行任意拦截代码，可以说是包含了上面所有功能。
 * 小结
 * 在Spring容器中使用AOP非常简单，只需要定义执行方法，并用AspectJ的注解标注应该在何处触发并执行。
 * Spring通过CGLIB动态创建子类等方式来实现AOP代理模式，大大简化了代码。
 */
@Aspect
@Component
public class LoggingAspect {
	//在执行UserService的每个方法前执行:
	@Before("execution(public * com.service.UserService.*(..))")
	public void doAccessCheck() {
		System.err.println("[Before] do access check..");
	}
	
	//在执行MailService的每个方法前后执行
	@Around("execution(public * com.service.MailService.*(..))")
	public Object doLogging(ProceedingJoinPoint pjp) throws Throwable {
		System.err.println("[Around] start " + pjp.getSignature());
		Object retVal = pjp.proceed();
		System.err.println("[Around] done " + pjp.getSignature());
		return retVal;
	}
}
