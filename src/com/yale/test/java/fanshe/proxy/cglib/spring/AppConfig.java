package com.yale.test.java.fanshe.proxy.cglib.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/*
 * 添加AOP类LoggingAspect之后,在AppConfig上加上@EnableAspectJAutoProxy。
 * 再次运行，不出意外的话，会得到一个NullPointerException：
 * 仔细跟踪代码，会发现null值出现在MailService.sendMail()内部的这一行代码：
 * 
 * springboot默认cglib
 * 你真的深入用过Spring？如果你用xml配置当然可以指定接口，如果你写@ Component +autoscan只能标注在class，所有的bean都只能当具体类型用cglib代理
 * 只要不是springboot2，java类有接口，属性使用接口，使用@Aotuwired注入实现类，默认就是使用jdk的实现类，如果属性不使用向上造型，直接使用实现类，
 * 启动会报错，所以springboot2把默认的动态代理改成cglib，这样无论属性是使用接口还是实现类，都没有问题了。
 * 但是Spring 5.x 中也还是默认jdk动态代理。
 * 你说的是实现了接口的情况，接口没有字段不存在文中访问字段的问题，用jdk和cglib都没有问题。
 * 项目中service不使用接口怕是要被打死，所以说你这个对不知道spring有jdk动态代理的小白有误导，特别是要面试的人。一般讲这个知识点的，
 * 都是上来就明确有两种代理实现，否则小白还以为项目中用的都是cglib的代理（非springboot2），然而实际项目service一定会用接口，
 * 这是最基本的解藕规范了，否则切换一个业务实现类，还要把所有上游代码改一通不得崩溃。
 * 最近在看aop动态代理，说的就是如果有接口，优先用的就是jdk的动态代理，当然如果想用cglib可以在spring配置，如果是单独一个类，
 * 那么就用cglib，最近在看这块东西，小白一个，不知道理解这样对不对
 * 对的，只要记住springboot2 默认选择了cglib，也可以通过配置修改，spring全版本默认接口走jdk的代理，而一般项目都要使用接口解藕。
 */
@Configuration
@ComponentScan
@EnableAspectJAutoProxy
public class AppConfig {
	public static void main(String[] args) {
		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		MailService mailService = context.getBean(MailService.class);
		System.out.println(mailService.sendMail());
	}
}
