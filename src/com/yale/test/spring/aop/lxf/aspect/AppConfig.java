package com.yale.test.spring.aop.lxf.aspect;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/*
 * 装配AOP
 * 在AOP编程中，我们经常会遇到下面的概念：
 * 1.Aspect：切面，即一个横跨多个核心逻辑的功能，或者称之为系统关注点；
 * 2.Joinpoint：连接点，即定义在应用程序流程的何处插入切面的执行；
 * 3.Pointcut：切入点，即一组连接点的集合；
 * 4.Advice：增强，指特定连接点上执行的动作；
 * 5.Introduction：引介，指为一个已有的Java对象动态地增加新的接口；
 * 6.Weaving：织入，指将切面整合到程序的执行流程中；
 * 7.Interceptor：拦截器，是一种实现增强的方式；
 * 8.Target Object：目标对象，即真正执行业务的核心逻辑对象；
 * 9.AOP Proxy：AOP代理，是客户端持有的增强后的对象引用。
 * 看完上述术语，是不是感觉对AOP有了进一步的困惑？其实，我们不用关心AOP创造的“术语”，只需要理解AOP本质上只是一种代理模式的实现方式，在Spring的容器中实现AOP特别方便。
 * 我们以UserService和MailService为例，这两个属于核心业务逻辑，现在，我们准备给UserService的每个业务方法执行前添加日志，给MailService的每个业务方法执行前后添加日志，在Spring中，需要以下步骤：
 * 首先，我们通过Maven引入Spring对AOP的支持：
 * <dependency>
	    <groupId>org.springframework</groupId>
	    <artifactId>spring-aspects</artifactId>
	    <version>${spring.version}</version>
	</dependency>
 * 上述依赖会自动引入AspectJ，使用AspectJ实现AOP比较方便，因为它的定义比较简单。
 * 然后，我们定义一个LoggingAspect：
 * 紧接着，我们需要给@Configuration类加上一个@EnableAspectJAutoProxy注解：
 * Spring的IoC容器看到这个注解，就会自动查找带有@Aspect的Bean，然后根据每个方法的@Before、@Around等注解把AOP注入到特定的Bean中。执行代码，我们可以看到以下输出
 * [Before] do access check...
	[Around] start void com.itranswarp.learnjava.service.MailService.sendRegistrationMail(User)
	Welcome, test!
	[Around] done void com.itranswarp.learnjava.service.MailService.sendRegistrationMail(User)
	[Before] do access check...
	[Around] start void com.itranswarp.learnjava.service.MailService.sendLoginMail(User)
	Hi, Bob! You are logged in at 2020-02-14T23:13:52.167996+08:00[Asia/Shanghai]
	[Around] done void com.itranswarp.learnjava.service.MailService.sendLoginMail(User)
 * 这说明执行业务逻辑前后，确实执行了我们定义的Aspect（即LoggingAspect的方法）。
 * 有些童鞋会问，LoggingAspect定义的方法，是如何注入到其他Bean的呢？
 */
@Configuration
@ComponentScan
@EnableAspectJAutoProxy
public class AppConfig {
	public static void main(String[] args) {
		
	}
}
