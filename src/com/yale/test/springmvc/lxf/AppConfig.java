package com.yale.test.springmvc.lxf;

import java.io.File;

import javax.servlet.ServletContext;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.ServletLoader;
import com.mitchellbosecke.pebble.spring.extension.SpringExtension;
import com.mitchellbosecke.pebble.spring.servlet.PebbleViewResolver;

/*
 * 开发Web应用
 * 在Web开发一章中，我们已经详细介绍了JavaEE中Web开发的基础：Servlet。具体地说，有以下几点：
 * 1.Servlet规范定义了几种标准组件：Servlet、JSP、Filter和Listener；
 * 2.Servlet的标准组件总是运行在Servlet容器中，如Tomcat、Jetty、WebLogic等。
 * 直接使用Servlet进行Web开发好比直接在JDBC上操作数据库，比较繁琐，更好的方法是在Servlet基础上封装MVC框架，基于MVC开发Web应用，大部分时候，不需要接触Servlet API，开发省时省力。
 * 我们在MVC开发和MVC高级开发已经由浅入深地介绍了如何编写MVC框架。当然，自己写的MVC主要是理解原理，要实现一个功能全面的MVC需要大量的工作以及广泛的测试。
 * 因此，开发Web应用，首先要选择一个优秀的MVC框架。常用的MVC框架有：
 * 1.Struts(https://struts.apache.org/)：最古老的一个MVC框架，目前版本是2，和1.x有很大的区别；
 * 2.WebWork：一个比Struts设计更优秀的MVC框架，但不知道出于什么原因，从2.0开始把自己的代码全部塞给Struts 2了
 * 3.Turbine(https://turbine.apache.org/)：一个重度使用Velocity，强调布局的MVC框架；
 * 4.其他100+MVC框架……（略）
 * Spring虽然都可以集成任何Web框架，但是，Spring本身也开发了一个MVC框架，就叫Spring MVC。这个MVC框架设计得足够优秀以至于我们已经不想再费劲去集成类似Struts这样的框架了。
 * 本章我们会详细介绍如何基于Spring MVC开发Web应用。
 * 使用Spring MVC
 * 我们在前面介绍Web开发时已经讲过了Java Web的基础：Servlet容器，以及标准的Servlet组件：
 * 1.Servlet：能处理HTTP请求并将HTTP响应返回；
 * 2.JSP：一种嵌套Java代码的HTML，将被编译为Servlet；
 * 3.Filter：能过滤指定的URL以实现拦截功能；
 * 4.Listener：监听指定的事件，如ServletContext、HttpSession的创建和销毁。
 * 此外，Servlet容器为每个Web应用程序自动创建一个唯一的ServletContext实例，这个实例就代表了Web应用程序本身。
 * 在MVC高级开发中，我们手撸了一个MVC框架，接口和Spring MVC类似。如果直接使用Spring MVC，我们写出来的代码类似：
 * @Controller
	public class UserController {
	    @GetMapping("/register")
	    public ModelAndView register() {
	        ...
	    }
	
	    @PostMapping("/signin")
	    public ModelAndView signin(@RequestParam("email") String email, @RequestParam("password") String password) {
	        ...
	    }
	    ...
	}
 * 但是，Spring提供的是一个IoC容器，所有的Bean，包括Controller，都在Spring IoC容器中被初始化，而Servlet容器由JavaEE服务器提供（如Tomcat），Servlet容器对Spring一无所知，
 * 他们之间到底依靠什么进行联系，又是以何种顺序初始化的？
 * 在理解上述问题之前，我们先把基于Spring MVC开发的项目结构搭建起来。首先创建基于Web的Maven工程，引入如下依赖：
 * org.springframework:spring-context:5.2.0.RELEASE
 * org.springframework:spring-webmvc:5.2.0.RELEASE
 * org.springframework:spring-jdbc:5.2.0.RELEASE
 * javax.annotation:javax.annotation-api:1.3.2
 * io.pebbletemplates:pebble-spring5:3.1.2
 * ch.qos.logback:logback-core:1.2.3
 * ch.qos.logback:logback-classic:1.2.3
 * com.zaxxer:HikariCP:3.4.2
 * org.hsqldb:hsqldb:2.5.0
 * 以及provided依赖：
 * org.apache.tomcat.embed:tomcat-embed-core:9.0.26
 * org.apache.tomcat.embed:tomcat-embed-jasper:9.0.26
 * 这个标准的Maven Web工程目录结构如下：
 * spring-web-mvc
	├── pom.xml
	└── src
	    └── main
	        ├── java
	        │   └── com
	        │       └── itranswarp
	        │           └── learnjava
	        │               ├── AppConfig.java
	        │               ├── DatabaseInitializer.java
	        │               ├── entity
	        │               │   └── User.java
	        │               ├── service
	        │               │   └── UserService.java
	        │               └── web
	        │                   └── UserController.java
	        ├── resources
	        │   ├── jdbc.properties
	        │   └── logback.xml
	        └── webapp
	            ├── WEB-INF
	            │   ├── templates
	            │   │   ├── _base.html
	            │   │   ├── index.html
	            │   │   ├── profile.html
	            │   │   ├── register.html
	            │   │   └── signin.html
	            │   └── web.xml
	            └── static
	                ├── css
	                │   └── bootstrap.css
	                └── js
	                    └── jquery.js
 * 其中，src/main/webapp是标准web目录，WEB-INF存放web.xml，编译的class，第三方jar，以及不允许浏览器直接访问的View模版，static目录存放所有静态文件。
 * 在src/main/resources目录中存放的是Java程序读取的classpath资源文件，除了JDBC的配置文件jdbc.properties外，我们又新增了一个logback.xml，这是Logback的默认查找的配置文件：
 * <?xml version="1.0" encoding="UTF-8"?>
	<configuration>
		<appender name="STDOUT"
			class="ch.qos.logback.core.ConsoleAppender">
			<layout class="ch.qos.logback.classic.PatternLayout">
				<Pattern>%d{yyyy-MM-dd HH:mm:ss} %-5level %logger{36} - %msg%n</Pattern>
			</layout>
		</appender>
	
		<logger name="com.itranswarp.learnjava" level="info" additivity="false">
			<appender-ref ref="STDOUT" />
		</logger>
	
		<root level="info">
			<appender-ref ref="STDOUT" />
		</root>
	</configuration>
 * 上面给出了一个写入到标准输出的Logback配置，可以基于上述配置添加写入到文件的配置。
 * 在src/main/java中就是我们编写的Java代码了。
 * 配置Spring MVC
 * 和普通Spring配置一样，我们编写正常的AppConfig后，只需加上@EnableWebMvc注解，就“激活”了Spring MVC：
 * 除了创建DataSource、JdbcTemplate、PlatformTransactionManager外，AppConfig需要额外创建几个用于Spring MVC的Bean：
 * WebMvcConfigurer,ViewResolver
 * WebMvcConfigurer并不是必须的，但我们在这里创建一个默认的WebMvcConfigurer，只覆写addResourceHandlers()，目的是让Spring MVC自动处理静态文件，并且映射路径为/static/**。
 * 另一个必须要创建的Bean是ViewResolver，因为Spring MVC允许集成任何模板引擎，使用哪个模板引擎，就实例化一个对应的ViewResolver：
 * 如果是普通的Java应用程序，我们通过main()方法可以很简单地创建一个Spring容器的实例：
 * public static void main(String[] args) {
    	ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
	}
 * 但是问题来了，现在是Web应用程序，而Web应用程序总是由Servlet容器创建，那么，Spring容器应该由谁创建？在什么时候创建？Spring容器中的Controller又是如何通过Servlet调用的？
 * 在Web应用中启动Spring容器有很多种方法，可以通过Listener启动，也可以通过Servlet启动，可以使用XML配置，也可以使用注解配置。这里，我们只介绍一种最简单的启动Spring容器的方式。
 * 第一步，我们在web.xml中配置Spring MVC提供的DispatcherServlet：
 * <!DOCTYPE web-app PUBLIC
 "-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN"
 "http://java.sun.com/dtd/web-app_2_3.dtd" >
	<web-app>
	    <servlet>
	        <servlet-name>dispatcher</servlet-name>
	        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
	        <init-param>
	            <param-name>contextClass</param-name>
	            <param-value>org.springframework.web.context.support.AnnotationConfigWebApplicationContext</param-value>
	        </init-param>
	        <init-param>
	            <param-name>contextConfigLocation</param-name>
	            <param-value>com.itranswarp.learnjava.AppConfig</param-value>
	        </init-param>
	        <load-on-startup>0</load-on-startup>
	    </servlet>
	
	    <servlet-mapping>
	        <servlet-name>dispatcher</servlet-name>
	        <url-pattern>/*</url-pattern>
	    </servlet-mapping>
	</web-app>
 * 初始化参数contextClass指定使用注解配置的AnnotationConfigWebApplicationContext，配置文件的位置参数contextConfigLocation指向AppConfig的完整类名，
 * 最后，把这个Servlet映射到/*，即处理所有URL。
 * 上述配置可以看作一个样板配置，有了这个配置，Servlet容器会首先初始化Spring MVC的DispatcherServlet，在DispatcherServlet启动时，
 * 它根据配置AppConfig创建了一个类型是WebApplicationContext的IoC容器，完成所有Bean的初始化，并将容器绑到ServletContext上。
 * 因为DispatcherServlet持有IoC容器，能从IoC容器中获取所有@Controller的Bean，因此，DispatcherServlet接收到所有HTTP请求后，
 * 根据Controller方法配置的路径，就可以正确地把请求转发到指定方法，并根据返回的ModelAndView决定如何渲染页面。
 * 最后，我们在AppConfig中通过main()方法启动嵌入式Tomcat：
 * 编写Controller
 * 有了Web应用程序的最基本的结构，我们的重点就可以放在如何编写Controller上。Spring MVC对Controller没有固定的要求，也不需要实现特定的接口。
 * 以UserController为例，编写Controller只需要遵循以下要点：
 * 总是标记@Controller而不是@Component：
 * 一个方法对应一个HTTP请求路径，用@GetMapping或@PostMapping表示GET或POST请求：
 */
@Configuration
@ComponentScan
@EnableWebMvc//启用SpringMVV
@EnableTransactionManagement
@PropertySource("classpath:/jdbc.properties")
public class AppConfig {
	/*
	 * WebMvcConfigurer并不是必须的，但我们在这里创建一个默认的WebMvcConfigurer，只覆写addResourceHandlers()，
	 * 目的是让Spring MVC自动处理静态文件，并且映射路径为/static/**。
	 */
	@Bean
	WebMvcConfigurer createWebMvcConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addResourceHandlers(ResourceHandlerRegistry registry) {
				registry.addResourceHandler("/static/**").addResourceLocations("/static/");
			}
		};
	}
	
	/*
	 * ViewResolver
	 * 另一个必须要创建的Bean是ViewResolver，因为Spring MVC允许集成任何模板引擎，使用哪个模板引擎，就实例化一个对应的ViewResolver：
	 * ViewResolver通过指定prefix和suffix来确定如何查找View。上述配置使用Pebble引擎，指定模板文件存放在/WEB-INF/tempaltes/目录下。
	 * 剩下的Bean都是普通的@Component，但Controller必须标记为@Controller，例如：UserController 
	 */
	@Bean
	ViewResolver createViewResolver(@Autowired ServletContext servletContext) {
		PebbleEngine engine = new PebbleEngine.Builder().autoEscaping(true).cacheActive(false).loader(new ServletLoader(servletContext))
				.extension(new SpringExtension()).build();
		PebbleViewResolver viewResolver = new PebbleViewResolver();
		viewResolver.setPrefix("/WEB-INF/templates/");
		viewResolver.setSuffix("");
		viewResolver.setPebbleEngine(engine);
		return viewResolver;
	}
	
	/*
	 * 上述Web应用程序就是我们使用Spring MVC时的一个最小启动功能集。由于使用了JDBC和数据库，用户的注册、登录信息会被持久化：
	 */
	public static void main(String[] args) throws LifecycleException {
		Tomcat tomcat = new Tomcat();
		tomcat.setPort(Integer.getInteger("port", 8080));
		tomcat.getConnector();
		Context ctx = tomcat.addWebapp("", new File("src/main/webapp").getAbsolutePath());
		WebResourceRoot resources = new StandardRoot(ctx);
		resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes", new File("target/classes").getAbsolutePath(), "/"));
		ctx.setResources(resources);
		tomcat.start();
		tomcat.getServer().await();
	}
}
