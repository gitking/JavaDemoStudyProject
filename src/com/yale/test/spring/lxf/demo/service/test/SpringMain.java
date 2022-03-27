package com.yale.test.spring.lxf.demo.service.test;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

import com.yale.test.spring.lxf.demo.service.ServiceBean;

/**
 * https://www.liaoxuefeng.com/article/895887872094400
 * Spring入门
 * Spring是一个非常优秀的轻量级框架，通过Spring的IoC容器，我们的关注点便放到了需要实现的业务逻辑上。对AOP的支持则能让我们动态增强业务方法。
 * 编写普通的业务逻辑Bean是非常容易而且易于测试的，因为它能脱离J2EE容器（如Servlet，JSP环境）单独进行单元测试。最后的一步便是在Spring框架中将这些业务Bean以XML配置文件的方式组织起来，它们就按照我们预定的目标正常工作了！非常容易！
 * 本文将给出一个基本的Spring入门示例，并演示如何使用Spring的AOP将复杂的业务逻辑分离到每个方面中。
 * 开发环境配置
 * 首先，需要正确配置Java环境。推荐安装JDK1.4.2，并正确配置环境变量：
 * 新建一个Java Project，将Spring的发布包spring.jar以及commons-logging-1.0.4.jar复制到Project目录下，并在Project>Properties中配置好Java Build Path：
 * 编写Bean接口及其实现
 * 我们实现一个管理用户的业务Bean。首先定义一个ServiceBean接口，声明一些业务方法：,然后在MyServiceBean中实现接口：为了简化逻辑，我们使用一个Map保存用户名和口令。
 * 在Spring中配置Bean并获得Bean的实例
 * 我们已经在一个main方法中实现了业务，不过，将对象的生命周期交给容器管理是更好的办法，我们就不必为初始化对象和销毁对象进行硬编码，从而获得更大的灵活性和可测试性。
 * 想要把ServiceBean交给Spring来管理，我们需要一个XML配置文件。新建一个beans.xml，放到src目录下，确保在classpath中能找到此配置文件，输入以下内容：
 * <?xml version="1.0" encoding="UTF-8"?>
	<!DOCTYPE beans PUBLIC "-//SPRING//DTD BEAN//EN"
	 "http://www.springframework.org/dtd/spring-beans.dtd">
	<beans>
	    <bean id="service" class="com.yale.test.spring.lxf.demo.service.impl.MyServiceBean" />
	</beans>
 * 以上XML声明了一个id为service的Bean，默认地，Spring为每个声明的Bean仅创建一个实例，并通过id来引用这个Bean。下面，我们修改main方法，让Spring来管理业务Bean：
 * 由于我们要通过main方法启动Spring环境，因此，首先需要初始化一个BeanFactory。红色部分是初始化Spring的BeanFactory的典型代码，只需要保证beans.xml文件位于classpath中。
 * 然后，在BeanFactory中通过id查找，即可获得相应的Bean的实例，并将其适当转型为合适的接口。
 * 接着，实现一系列业务操作，在应用程序结束前，让Spring销毁所有的Bean实例。
 * 对比上一个版本的com.yale.test.spring.lxf.demo.service.test.Main.java，可以看出，最大的变化是不需要自己管理Bean的生命周期。另一个好处是在不更改实现类的前提下，动态地为应用程序增加功能。
 * 编写Advisor以增强ServiceBean
 * 所谓AOP即是将分散在各个方法处的公共代码提取到一处，并通过类似拦截器的机制实现代码的动态织入。可以简单地想象成，在某个方法的调用前、返回前、调用后和抛出异常时，动态插入自己的代码。在弄清楚Pointcut、Advice之类的术语前，不如编写一个最简单的AOP应用来体验一下。
 * 考虑一下通常的Web应用程序都会有日志记录，我们来编写一个com.yale.test.spring.lxf.demo.service.aop.LogAdvisor.java，对每个业务方法调用前都作一个记录：
 * 然后，修改springbeanslxf.xml：
 * <?xml version="1.0" encoding="UTF-8"?>
	<!DOCTYPE beans PUBLIC "-//SPRING//DTD BEAN//EN"
	 "http://www.springframework.org/dtd/spring-beans.dtd">
	<beans>
	    <bean id="serviceTarget" class="com.yale.test.spring.lxf.demo.service.impl.MyServiceBean" />
	    <bean id="logAdvisor" class="com.yale.test.spring.lxf.demo.service.aop.LogAdvisor"/>
	    
	    <bean id="service" class="org.springframework.aop.framework.ProxyFactoryBean">
	    	<property name="proxyInterfaces"><value>com.yale.test.spring.lxf.demo.service.ServiceBean</value></property>
	    	<property name="target"><ref local="serviceTarget"/></property>
	    	<property name="interceptorNames">
	    		<list>
	    			<value>logAdvisor</value>
	    		</list>
	    	</property>
	    </bean>
	</beans>
 * <!-- <ref local="serviceTarget"/> local这个关键字现在用不了拉 -->
 * 注意观察修改后的配置文件，我们使用了一个ProxyFactoryBean作为service来与客户端打交道，而真正的业务Bean即MyServiceBean被声明为serviceTarget并作为参数对象传递给ProxyFactoryBean，proxyInterfaces指定了返回的接口类型。
 * 对于客户端而言，将感觉不出任何变化，但却动态加入了LogAdvisor，关系如下：
 * 运行结果如下，可以很容易看到调用了哪些方法：
 * SpringAOP面向方面/切面编程[Log]com.yale.test.spring.lxf.demo.service.impl.MyServiceBean.addUser()
	SpringAOP面向方面/切面编程[Log]com.yale.test.spring.lxf.demo.service.impl.MyServiceBean.addUser()
	SpringAOP面向方面/切面编程[Log]com.yale.test.spring.lxf.demo.service.impl.MyServiceBean.addUser()
	SpringAOP面向方面/切面编程[Log]com.yale.test.spring.lxf.demo.service.impl.MyServiceBean.getPassword()
	tom's password is "goodbye"
	SpringAOP面向方面/切面编程[Log]com.yale.test.spring.lxf.demo.service.impl.MyServiceBean.findUser()
	SpringAOP面向方面/切面编程[Log]com.yale.test.spring.lxf.demo.service.impl.MyServiceBean.deleteUser()
 * 要截获指定的某些方法也是可以的。下面的例子com.yale.test.spring.lxf.demo.service.aop.PasswordAdvisor.java将修改getPassword()方法的返回值：
 * 这个PasswordAdvisor将截获ServiceBean的getPassword()方法的返回值，并将其改为"***"。继续修改springbeanslxf.xml：
 * <?xml version="1.0" encoding="UTF-8"?>
	<!DOCTYPE beans PUBLIC "-//SPRING//DTD BEAN//EN"
	 "http://www.springframework.org/dtd/spring-beans.dtd">
	<beans>
	    <bean id="serviceTarget" class="com.yale.test.spring.lxf.demo.service.impl.MyServiceBean" />
	    <bean id="logAdvisor" class="com.yale.test.spring.lxf.demo.service.aop.LogAdvisor"/>
	    <bean id="passwordAdvisorTarget" class="com.yale.test.spring.lxf.demo.service.aop.PasswordAdvisor"/>
	    
	    <bean id="passwordAdvisor" class="org.springframework.aop.support.RegexpMethodPointcutAdvisor">
	    	<property name="advice">
	    		<ref bean="passwordAdvisorTarget"/>
	    	</property>
	    	<property name="patterns">
	    		<list>
	    			<value>.*getPassword</value>
	    		</list>
	    	</property>
	    </bean>
	    <bean id="service" class="org.springframework.aop.framework.ProxyFactoryBean">
	    	<property name="proxyInterfaces"><value>com.yale.test.spring.lxf.demo.service.ServiceBean</value></property>
	    	<!-- <ref local="serviceTarget"/> local这个关键字现在用不了拉 -->
	    	<property name="target"><ref bean="serviceTarget"/></property>
	    	<property name="interceptorNames">
	    		<list>
	    			<value>logAdvisor</value>
	    			<value>passwordAdvisor</value>
	    		</list>
	    	</property>
	    </bean>
	</beans>
 * 利用Spring提供的一个RegexMethodPointcutAdvisor可以非常容易地指定要截获的方法。运行结果如下，可以看到返回结果变为"******"：
 * 还需要继续增强ServiceBean？我们编写一个com.yale.test.spring.lxf.demo.service.aop.ExceptionAdvisor，在业务方法抛出异常时能做一些处理：
 * 将此Advice添加到springbeanslxf.xml中，然后在业务Bean中删除一个不存在的用户，故意抛出异常：
 * <?xml version="1.0" encoding="UTF-8"?>
	<!DOCTYPE beans PUBLIC "-//SPRING//DTD BEAN//EN"
 	"http://www.springframework.org/dtd/spring-beans.dtd">
	<beans>
	    <bean id="serviceTarget" class="com.yale.test.spring.lxf.demo.service.impl.MyServiceBean" />
	    
	    <bean id="logAdvisor" class="com.yale.test.spring.lxf.demo.service.aop.LogAdvisor"/>
	    <bean id="passwordAdvisorTarget" class="com.yale.test.spring.lxf.demo.service.aop.PasswordAdvisor"/>
	    <bean id="exceptionAdviorTarget" class="com.yale.test.spring.lxf.demo.service.aop.ExceptionAdvisor"/>
	    
	    <bean id="passwordAdvisor" class="org.springframework.aop.support.RegexpMethodPointcutAdvisor">
	    	<property name="advice">
	    		<ref bean="passwordAdvisorTarget"/>
	    	</property>
	    	<property name="patterns">
	    		<list>
	    			<value>.*getPassword</value>
	    		</list>
	    	</property>
	    </bean>
	    <bean id="service" class="org.springframework.aop.framework.ProxyFactoryBean">
	    	<property name="proxyInterfaces"><value>com.yale.test.spring.lxf.demo.service.ServiceBean</value></property>
	    	<!-- <ref local="serviceTarget"/> local这个关键字现在用不了拉 -->
	    	<property name="target"><ref bean="serviceTarget"/></property>
	    	<property name="interceptorNames">
	    		<list>
	    			<value>logAdvisor</value>
	    			<value>passwordAdvisor</value>
	    			<value>exceptionAdviorTarget</value>
	    		</list>
	    	</property>
	    </bean>
	</beans>
 * service.deleteUser("not-exist");
 * 再次运行，注意到ExceptionAdvisor记录下了异常：
 * 总结
 * 利用Spring非常强大的IoC容器和AOP功能，我们能实现非常灵活的应用，让Spring容器管理业务对象的生命周期，利用AOP增强功能，却不影响业务接口，从而避免更改客户端代码。
 * 为了实现这一目标，必须始终牢记：面向接口编程。而Spring默认的AOP代理也是通过Java的代理接口实现的。虽然Spring也可以用CGLIB实现对普通类的代理，
 * 但是，业务对象只要没有接口，就会变得难以扩展、维护和测试。
 * @author issuser
 */
public class SpringMain {
	public static void main(String[] args) {
		//init factory:初始化Spring的工厂类factory
		XmlBeanFactory factory = new XmlBeanFactory(new ClassPathResource("springbeanslxf.xml"));
		ServiceBean service = (ServiceBean)factory.getBean("service");
		service.addUser("bill", "hello");
		service.addUser("tom", "goodbye");
		service.addUser("tracy", "morning");
		System.out.println("tom's password is \"" + service.getPassword("tom") + "\"");
		if (service.findUser("tom")) {
			service.deleteUser("tom");
		}
		service.deleteUser("not-exist");
		//close factory,让Spring销毁所有的Bean实例
		factory.destroySingletons();
	}
}
