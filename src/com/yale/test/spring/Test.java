package com.yale.test.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.yale.test.spring.lxf.service.User;
import com.yale.test.spring.lxf.service.UserService;

/*
 * Spring开发
 * 什么是Spring？
 * Spring是一个支持快速开发Java EE应用程序的框架。它提供了一系列底层容器和基础设施，并可以和大量常用的开源框架无缝集成，可以说是开发Java EE应用程序的必备。
 * Spring最早是由Rod Johnson这哥们在他的《Expert One-on-One J2EE Development without EJB》一书中提出的用来取代EJB的轻量级框架。随后这哥们又开始专心开发这个基础框架，并起名为Spring Framework。
 * 随着Spring越来越受欢迎，在Spring Framework基础上，又诞生了Spring Boot、Spring Cloud、Spring Data、Spring Security等一系列基于Spring Framework的项目。本章我们只介绍Spring Framework，即最核心的Spring框架。后续章节我们还会涉及Spring Boot、Spring Cloud等其他框架。
 * Spring Framework
 * Spring Framework主要包括几个模块：
 * 1.支持IoC和AOP的容器；
 * 2.支持JDBC和ORM的数据访问模块；
 * 3.支持声明式事务的模块；
 * 4.支持基于Servlet的MVC开发；
 * 5.支持基于Reactive的Web开发；
 * 6.以及集成JMS、JavaMail、JMX、缓存等其他模块。
 * IoC容器
 * 在学习Spring框架时，我们遇到的第一个也是最核心的概念就是容器。
 * 什么是容器？容器是一种为某种特定组件的运行提供必要支持的一个软件环境。例如，Tomcat就是一个Servlet容器，它可以为Servlet的运行提供运行环境。类似Docker这样的软件也是一个容器，它提供了必要的Linux环境以便运行一个特定的Linux进程。
 * 通常来说，使用容器运行组件，除了提供一个组件运行环境之外，容器还提供了许多底层服务。例如，Servlet容器底层实现了TCP连接，
 * 解析HTTP协议等非常复杂的服务，如果没有容器来提供这些服务，我们就无法编写像Servlet这样代码简单，功能强大的组件。
 * 早期的JavaEE服务器提供的EJB容器最重要的功能就是通过声明式事务服务，使得EJB组件的开发人员不必自己编写冗长的事务处理代码，所以极大地简化了事务处理。
 * Spring的核心就是提供了一个IoC容器，它可以管理所有轻量级的JavaBean组件，提供的底层服务包括组件的生命周期管理、配置和组装服务、AOP支持，以及建立在AOP基础上的声明式事务服务等。
 * 本章我们讨论的IoC容器，主要介绍Spring容器如何对组件进行生命周期管理和配置组装服务。
 * 廖老师的教程很棒，我写了一篇《造轮子：实现一个简易的 Spring IoC 容器》，配合看应该可以更好的理解 Spring。
 * https://depp.wang/2020/04/19/realize-a-simple-spring-ioc-container/
 * IoC原理
 * Spring提供的容器又称为IoC容器，什么是IoC？
 * IoC全称Inversion of Control，直译为控制反转。那么何谓IoC？在理解IoC之前，我们先看看通常的Java组件是如何协作的。
 * 我们假定一个在线书店，通过BookService获取书籍：
 * public class BookService {
	    private HikariConfig config = new HikariConfig();
	    private DataSource dataSource = new HikariDataSource(config);
	
	    public Book getBook(long bookId) {
	        try (Connection conn = dataSource.getConnection()) {
	            ...
	            return book;
	        }
	    }
	}
 * 为了从数据库查询书籍，BookService持有一个DataSource。为了实例化一个HikariDataSource，又不得不实例化一个HikariConfig。
 * 现在，我们继续编写UserService获取用户：
 * public class UserService {
	    private HikariConfig config = new HikariConfig();
	    private DataSource dataSource = new HikariDataSource(config);
	
	    public User getUser(long userId) {
	        try (Connection conn = dataSource.getConnection()) {
	            ...
	            return user;
	        }
	    }
	}
 * 因为UserService也需要访问数据库，因此，我们不得不也实例化一个HikariDataSource。
 * 在处理用户购买的CartServlet中，我们需要实例化UserService和BookService：
 * public class CartServlet extends HttpServlet {
    	private BookService bookService = new BookService();
	    private UserService userService = new UserService();
	
	    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	        long currentUserId = getFromCookie(req);
	        User currentUser = userService.getUser(currentUserId);
	        Book book = bookService.getBook(req.getParameter("bookId"));
	        cartService.addToCart(currentUser, book);
	        ...
	    }
	}
 * 类似的，在购买历史HistoryServlet中，也需要实例化UserService和BookService：
 * public class HistoryServlet extends HttpServlet {
	    private BookService bookService = new BookService();
	    private UserService userService = new UserService();
	}
 * 上述每个组件都采用了一种简单的通过new创建实例并持有的方式。仔细观察，会发现以下缺点：
 * 1.实例化一个组件其实很难，例如，BookService和UserService要创建HikariDataSource，实际上需要读取配置，才能先实例化HikariConfig，再实例化HikariDataSource。
 * 2.没有必要让BookService和UserService分别创建DataSource实例，完全可以共享同一个DataSource，但谁负责创建DataSource，谁负责获取其他组件已经创建的DataSource，不好处理。类似的，CartServlet和HistoryServlet也应当共享BookService实例和UserService实例，但也不好处理。
 * 3.很多组件需要销毁以便释放资源，例如DataSource，但如果该组件被多个组件共享，如何确保它的使用方都已经全部被销毁？
 * 4.随着更多的组件被引入，例如，书籍评论，需要共享的组件写起来会更困难，这些组件的依赖关系会越来越复杂。
 * 5.测试某个组件，例如BookService，是复杂的，因为必须要在真实的数据库环境下执行。
 * 从上面的例子可以看出，如果一个系统有大量的组件，其生命周期和相互之间的依赖关系如果由组件自身来维护，不但大大增加了系统的复杂度，而且会导致组件之间极为紧密的耦合，继而给测试和维护带来了极大的困难。
 * 因此，核心问题是：
 * 1.谁负责创建组件？
 * 2.谁负责根据依赖关系组装组件？
 * 3.销毁时，如何按依赖顺序正确销毁？
 * 解决这一问题的核心方案就是IoC。
 * 传统的应用程序中，控制权在程序本身，程序的控制流程完全由开发者控制，例如：
 * CartServlet创建了BookService，在创建BookService的过程中，又创建了DataSource组件。这种模式的缺点是，一个组件如果要使用另一个组件，必须先知道如何正确地创建它。
 * 在IoC模式下，控制权发生了反转，即从应用程序转移到了IoC容器，所有组件不再由应用程序自己创建和配置，而是由IoC容器负责，这样，应用程序只需要直接使用已经创建好并且配置好的组件。
 * 为了能让组件在IoC容器中被“装配”出来，需要某种“注入”机制，例如，BookService自己并不会创建DataSource，而是等待外部通过setDataSource()方法来注入一个DataSource：
 * public class BookService {
	    private DataSource dataSource;
	
	    public void setDataSource(DataSource dataSource) {
	        this.dataSource = dataSource;
	    }
	}
 * 不直接new一个DataSource，而是注入一个DataSource，这个小小的改动虽然简单，却带来了一系列好处：
 * 1.BookService不再关心如何创建DataSource，因此，不必编写读取数据库配置之类的代码；
 * 2.DataSource实例被注入到BookService，同样也可以注入到UserService，因此，共享一个组件非常简单；
 * 3. 测试BookService更容易，因为注入的是DataSource，可以使用内存数据库，而不是真实的MySQL配置。
 * 因此，IoC又称为依赖注入（DI：Dependency Injection），它解决了一个最主要的问题：将组件的创建+配置与组件的使用相分离，并且，由IoC容器负责管理组件的生命周期。
 * 因为IoC容器要负责实例化所有的组件，因此，有必要告诉容器如何创建组件，以及各组件的依赖关系。一种最简单的配置是通过XML文件来实现，例如：
 * <beans>
	    <bean id="dataSource" class="HikariDataSource" />
	    <bean id="bookService" class="BookService">
	        <property name="dataSource" ref="dataSource" />
	    </bean>
	    <bean id="userService" class="UserService">
	        <property name="dataSource" ref="dataSource" />
	    </bean>
	</beans>
 * 上述XML配置文件指示IoC容器创建3个JavaBean组件，并把id为dataSource的组件通过属性dataSource（即调用setDataSource()方法）注入到另外两个组件中。
 * 在Spring的IoC容器中，我们把所有组件统称为JavaBean，即配置一个组件就是配置一个Bean。
 * 依赖注入方式
 * 我们从上面的代码可以看到，依赖注入可以通过set()方法实现。但依赖注入也可以通过构造方法实现。
 * 很多Java类都具有带参数的构造方法，如果我们把BookService改造为通过构造方法注入，那么实现代码如下：
 * public class BookService {
	    private DataSource dataSource;
	
	    public BookService(DataSource dataSource) {
	        this.dataSource = dataSource;
	    }
	}
 * Spring的IoC容器同时支持属性注入和构造方法注入，并允许混合使用。
 * 无侵入容器
 * 在设计上，Spring的IoC容器是一个高度可扩展的无侵入容器。所谓无侵入，是指应用程序的组件无需实现Spring的特定接口，或者说，组件根本不知道自己在Spring的容器中运行。这种无侵入的设计有以下好处：
 * 1.应用程序组件既可以在Spring的IoC容器中运行，也可以自己编写代码自行组装配置；
 * 2.测试的时候并不依赖Spring容器，可单独进行测试，大大提高了开发效率。
 * 装配Bean
 * 我们前面讨论了为什么要使用Spring的IoC容器，因为让容器来为我们创建并装配Bean能获得很大的好处，那么到底如何使用IoC容器？装配好的Bean又如何使用？
 * 我们来看一个具体的用户注册登录的例子。整个工程的结构如下：
 * spring-ioc-appcontext
	├── pom.xml
	└── src
	    └── main
	        ├── java
	        │   └── com
	        │       └── itranswarp
	        │           └── learnjava
	        │               ├── Main.java
	        │               └── service
	        │                   ├── MailService.java
	        │                   ├── User.java
	        │                   └── UserService.java
	        └── resources
	            └── application.xml
 * 首先，我们用Maven创建工程并引入spring-context依赖：
 * 
 * <project xmlns="http://maven.apache.org/POM/4.0.0"
	    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	    <modelVersion>4.0.0</modelVersion>
	
	    <groupId>com.itranswarp.learnjava</groupId>
	    <artifactId>spring-ioc-appcontext</artifactId>
	    <version>1.0-SNAPSHOT</version>
	    <packaging>jar</packaging>
	
	    <properties>
	        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
	        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
	        <maven.compiler.source>11</maven.compiler.source>
	        <maven.compiler.target>11</maven.compiler.target>
	        <java.version>11</java.version>
	
	        <spring.version>5.2.3.RELEASE</spring.version>
	    </properties>
	
	    <dependencies>
	        <dependency>
	            <groupId>org.springframework</groupId>
	            <artifactId>spring-context</artifactId>
	            <version>${spring.version}</version>
	        </dependency>
	    </dependencies>
	</project>
 * 我们先编写一个MailService，用于在用户登录和注册成功后发送邮件通知：
 * 再编写一个UserService，实现用户注册和登录：
 * 注意到UserService通过setMailService()注入了一个MailService。
 * 然后，我们需要编写一个特定的application.xml配置文件，告诉Spring的IoC容器应该如何创建并组装Bean：
 * <?xml version="1.0" encoding="UTF-8"?>
	<beans xmlns="http://www.springframework.org/schema/beans"
	    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	    xsi:schemaLocation="http://www.springframework.org/schema/beans
	        https://www.springframework.org/schema/beans/spring-beans.xsd">
	
	    <bean id="userService" class="com.itranswarp.learnjava.service.UserService">
	        <property name="mailService" ref="mailService" />
	    </bean>
	
	    <bean id="mailService" class="com.itranswarp.learnjava.service.MailService" />
	</beans>
 * 注意观察上述配置文件，其中与XML Schema相关的部分格式是固定的，我们只关注两个<bean ...>的配置：
 * 1.每个<bean ...>都有一个id标识，相当于Bean的唯一ID；
 * 2.在userServiceBean中，通过<property name="..." ref="..." />注入了另一个Bean；
 * 3.Bean的顺序不重要，Spring根据依赖关系会自动正确初始化。
 * 把上述XML配置文件用Java代码写出来，就像这样：
 * UserService userService = new UserService();
	MailService mailService = new MailService();
	userService.setMailService(mailService);
 * 只不过Spring容器是通过读取XML文件后使用反射完成的。
 * 如果注入的不是Bean，而是boolean、int、String这样的数据类型，则通过value注入，例如，创建一个HikariDataSource：
 * <bean id="dataSource" class="com.zaxxer.hikari.HikariDataSource">
	    <property name="jdbcUrl" value="jdbc:mysql://localhost:3306/test" />
	    <property name="username" value="root" />
	    <property name="password" value="password" />
	    <property name="maximumPoolSize" value="10" />
	    <property name="autoCommit" value="true" />
	</bean>
 * 最后一步，我们需要创建一个Spring的IoC容器实例，然后加载配置文件，让Spring容器为我们创建并装配好配置文件中指定的所有Bean，这只需要一行代码：
 * ApplicationContext context = new ClassPathXmlApplicationContext("application.xml");
 * 接下来，我们就可以从Spring容器中“取出”装配好的Bean然后使用它：
 * // 获取Bean:
	UserService userService = context.getBean(UserService.class);
	// 正常调用:
	User user = userService.login("bob@example.com", "password");
 * 完整的main()方法如下：
 * 练习
 * 在上述示例的基础上，继续给UserService注入DataSource，并把注册和登录功能通过数据库实现。
 * 小结
 * Spring的IoC容器接口是ApplicationContext，并提供了多种实现类；
 * 通过XML配置文件创建IoC容器时，使用ClassPathXmlApplicationContext；
 * 持有IoC容器后，通过getBean()方法获取Bean的引用。
 */
public class Test {
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		/*
		 * 我们从创建Spring容器的代码：ApplicationContext
		 * 可以看到，Spring容器就是ApplicationContext，它是一个接口，有很多实现类，这里我们选择ClassPathXmlApplicationContext，表示它会自动从classpath中查找指定的XML配置文件。
		 * 获得了ApplicationContext的实例，就获得了IoC容器的引用。从ApplicationContext中我们可以根据Bean的ID获取Bean，但更多的时候我们根据Bean的类型获取Bean的引用：
		 * UserService userService = context.getBean(UserService.class);
		 * Spring还提供另一种IoC容器叫BeanFactory，使用方式和ApplicationContext类似：
		 * BeanFactory factory = new XmlBeanFactory(new ClassPathResource("application.xml"));
		 * MailService mailService = factory.getBean(MailService.class);
		 * BeanFactory和ApplicationContext的区别在于，BeanFactory的实现是按需创建，即第一次获取Bean时才创建这个Bean，而ApplicationContext会一次性创建所有的Bean。
		 * 实际上，ApplicationContext接口是从BeanFactory接口继承而来的，并且，ApplicationContext提供了一些额外的功能，包括国际化支持、事件和通知机制等。
		 * 通常情况下，我们总是使用ApplicationContext，很少会考虑使用BeanFactory。
		 * 问:BeanFactory的实现是按需创建不是更省内存吗？为什么优先选择ApplicationContext会一次性创建所有的Bean
		 * 答:按需创建的时候，发现依赖有问题再报个错，还不如启动就报错
		 */
        ApplicationContext context = new ClassPathXmlApplicationContext("application.xml");
        UserService userService = context.getBean(UserService.class);
        User user = userService.login("bob@example.com", "password");
        System.out.println(user.getName());
	}
}
