package com.yale.test.spring.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.yale.test.spring.services.StuServices;
import com.yale.test.spring.services.UserService;
import com.yale.test.spring.services.impl.SimpleUserServiceImpl;
import com.yale.test.spring.services.impl.SpecUserServiceImpl;
import com.yale.test.spring.services.impl.UserServiceImpl;
import com.yale.test.spring.vo.Cat;
import com.yale.test.spring.vo.Hello;
import com.yale.test.spring.vo.Student;
import com.yale.test.spring.vo.Teacher;

/**
 * spring security spring的安全框架
shiro 比spring security要好一点
Spring的理念是:使现有技术更加实用,Spring本身是大杂烩整合现有的框架技术。
Spring的优点:1、轻量级框架2、IOC容器(控制翻转)3、AOP（面向切面编程）4、对事务的支持5、对别的框架的支持
Spring-桥梁,Spring可以和hibernate和Struts整合到一起
aop面向切面编程在Spring中的作用：1、提供声明式服务(声明式事务)2、允许用户实现自定义框架
 * @author dell
 *
 */
public class SpringTest {
	public static void main(String[] args) {
		/**
		 * 注意Spring框架依赖Apache的commons-logging-1.2.jar的jar包,所以你必须导入这个包
		 * ClassPathXmlApplicationContext("beans.xml")这个参数的具体传值如下:官方文档说明
		 * Standalone XML application context, taking the context definition files from the class path, interpreting plain paths as class path resource names that include the package path (e.g. "mypackage/myresource.txt"). Useful for test harnesses as well as for application contexts embedded within JARs.
		 * The config location defaults can be overridden via AbstractRefreshableConfigApplicationContext.getConfigLocations(), Config locations can either denote concrete files like "/myfiles/context.xml" or Ant-style patterns like "/myfiles/*-context.xml" (see the AntPathMatcher javadoc for pattern details). 
		 */
		//ClassPathXmlApplicationContext("beans.xml");这种方式beans.xml这个文件必须放在src目录下面
		//ApplicationContext springContext = new ClassPathXmlApplicationContext("beans.xml");

		//让ClassPathXmlApplicationContext去我指定的目录寻找beans.xml配置文件
		ApplicationContext springContext = new ClassPathXmlApplicationContext("com/yale/test/spring/vo/beans.xml");
		
		/*
		 * Web项目也可以通过 WebApplicationContextUtils获取ApplicationContext,WebApplicationContext继承了ApplicationContext接口
		 * 使用WebApplicationContext需要在web.xml中配置<context-param>
				<param-name>contextConfigLocation</param-name>
				<param-value>/WEB-INF/applicationContext.xml</param-value>
			</context-param> 这个参数
		 * WebApplicationContext webApp = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
		 */
		System.out.println("#############Spring配置文件记载完毕,你注意观察打印顺序###########");
		Hello hello = (Hello)springContext.getBean("hello");
		hello.show();
		hello.setAge(10);
		
		Hello hello1 = (Hello)springContext.getBean("hello");
		System.out.println("Spring默认创建的是单例模式:可以在配置文件里面通过scope修改创建方式:scope如果是prototype这里就是false-->" + (hello == hello1));
		hello1.show();
		System.out.println("注意我hello1没有设置age的值(Spring获取的是单例对象):" + hello1.getAge());
		
		UserService us = (UserService)springContext.getBean("service");
		us.getUser();
		
		Hello hello2 = (Hello)springContext.getBean("hello1");
		System.out.println("让Spring通过构造方法类创建对象：" + hello2.getName() + "," +  hello2.getAge());
		
		/*
		 * helloFactory,在Spring配置文件里面配置的是HelloFactory这个类
		 * 但是获取出来的是Hello这个类,这是工厂模式,
		 * org.springframework.jndi.JndiObjectFactoryBean就是一个工厂模式,
		 * 通过获取JndiObjectFactoryBean能获取到一个javax.sql.DataSource(数据源)的实现类
		 * JndiObjectFactoryBean实现了FactoryBean接口,所有实现FactoryBean接口的类都被当作工厂来使用，而不是简单的直接当作bean来使用，FactoryBean实现类里定义了要生产的对象，
		 * 并且由FactoryBean实现类来造该对象的实例，看到这里聪明的你大概已经能猜出个八九不离十了吧 
		 * https://blog.csdn.net/caolaosanahnu/article/details/8619385
		 */
		Hello helloFactory = (Hello)springContext.getBean("helloFactory");
		System.out.println("让Spring通过指定的静态工厂类来创建对象：" + helloFactory.getName() + "," +  helloFactory.getAge());
		
		System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
		
		Hello helloDynamic = (Hello)springContext.getBean("helloDynamic");
		System.out.println("让Spring通过指定的动态工厂类来创建对象：" + helloDynamic.getName() + "," +  helloDynamic.getAge());
		
		Hello hd = (Hello)springContext.getBean("hd");
		System.out.println("让Spring通过指定的别名创建对象：" + hd.getName() + "," +  hd.getAge());
		
		Hello h3 = (Hello)springContext.getBean("h3");
		System.out.println("让Spring通过指定的别名h3创建对象：" + h3.getName() + "," +  h3.getAge());
		
		
		UserServiceImpl usi = (UserServiceImpl)springContext.getBean(UserServiceImpl.class);
		usi.getUser();
		System.out.println("Spring还可以通过class来创建bean,前提是你被配置文件里面只配置了一个这个类的bean。");
		
		Student str = (Student)springContext.getBean("stu");
		System.out.println("这里显示了Spring的数组注入:");
		str.show();
		
		Teacher te = (Teacher)springContext.getBean("teac");
		te.show();
		
		Teacher t2e = (Teacher)springContext.getBean("teac2");
		t2e.show();
		
		Teacher te3 = (Teacher)springContext.getBean("teac3");
		te3.show();
		
		Teacher te4 = (Teacher)springContext.getBean("teac4");
		te4.show();
		
		System.out.println("*****************************");
		
		SimpleUserServiceImpl simSer = (SimpleUserServiceImpl)springContext.getBean("simService");
		simSer.getUser();
		
		System.out.println("***********byType******************");
		
		SpecUserServiceImpl susi = (SpecUserServiceImpl)springContext.getBean("susi");
		susi.getUser();
		
		System.out.println("***********byType******************");
		
		SpecUserServiceImpl susi1 = (SpecUserServiceImpl)springContext.getBean("susi1");
		susi1.getUser();
		
		
		System.out.println("***********aop来了******************");
		
		/**
		 * org/aspectj/weaver/reflect/ReflectionWorld$ReflectionWorldException
		 * org/aspectj/weaver/reflect/ReflectionWorld$ReflectionWorldException
		 * 报这个异常是因为没有引入aspectjweaver.jar jar包
		 */
		/**
		 * 这里只能用StuServices接口接收,不能用StuServiceImpl类接收,因为Spring在这里给你返回的是一个代理类,
		 * 等于Spring自己创建了一个类并且实现了StuServices接口,所以你用StuServiceImpl接收是有问题的
		 */
		StuServices stuSer = (StuServices)springContext.getBean("stuSer");
		stuSer.add();
		stuSer.delete(12);

		System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
		System.out.println();
		StuServices collgeSer = (StuServices)springContext.getBean("collgeSer");
		collgeSer.add();
		collgeSer.delete(12);
		
		Cat cat = (Cat)springContext.getBean("cat");
		System.out.println("Cat的真实类型:" +cat.getClass());
		System.out.println("猫的名称:" + cat.getName());
		System.out.println("通过BodyTemplate获取猫的名称:" + cat.getBt().getName());
		cat = null;
		System.out.println("Spring有调用cat的注销方法吗?");
	}
}
