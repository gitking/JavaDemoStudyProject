package com.yale.test.spring.integration.jms;

import java.io.File;

import javax.jms.ConnectionFactory;
import javax.servlet.ServletContext;
import javax.sql.DataSource;

import org.apache.activemq.artemis.jms.client.ActiveMQJMSConnectionFactory;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.ServletLoader;
import com.mitchellbosecke.pebble.spring.extension.SpringExtension;
import com.mitchellbosecke.pebble.spring.servlet.PebbleViewResolver;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/*
 * 集成JMS
 * JMS即Java Message Service，是JavaEE的消息服务接口。JMS主要有两个版本：1.1和2.0。2.0和1.1相比，主要是简化了收发消息的代码。
 * 所谓消息服务，就是两个进程之间，通过消息服务器传递消息：
 *  ┌────────┐    ┌──────────────┐    ┌────────┐
	│Producer│───>│Message Server│───>│Consumer│
	└────────┘    └──────────────┘    └────────┘
 * 使用消息服务，而不是直接调用对方的API，它的好处是：
 * 1.双方各自无需知晓对方的存在，消息可以异步处理，因为消息服务器会在Consumer离线的时候自动缓存消息；
 * 2.如果Producer发送的消息频率高于Consumer的处理能力，消息可以积压在消息服务器，不至于压垮Consumer；
 * 3.通过一个消息服务器，可以连接多个Producer和多个Consumer。
 * 因为消息服务在各类应用程序中非常有用，所以JavaEE专门定义了JMS规范。注意到JMS是一组接口定义，如果我们要使用JMS，还需要选择一个具体的JMS产品。
 * 常用的JMS服务器有开源的ActiveMQ(https://activemq.apache.org/)，商业服务器如WebLogic、WebSphere等也内置了JMS支持。这里我们选择开源的ActiveMQ作为JMS服务器，因此，在开发JMS之前我们必须首先安装ActiveMQ。
 * 现在问题来了：从官网下载ActiveMQ时，蹦出一个页面，让我们选择ActiveMQ Classic或者ActiveMQ Artemis，这两个是什么关系，又有什么区别？
 * 实际上ActiveMQ Classic原来就叫ActiveMQ，是Apache开发的基于JMS 1.1的消息服务器，目前稳定版本号是5.x，而ActiveMQ Artemis是由RedHat捐赠的HornetQ(https://hornetq.jboss.org/)服务器代码的基础上开发的，
 * 目前稳定版本号是2.x。和ActiveMQ Classic相比，Artemis版的代码与Classic完全不同，并且，它支持JMS 2.0，使用基于Netty的异步IO，大大提升了性能。此外，Artemis不仅提供了JMS接口，它还提供了AMQP接口，STOMP接口和物联网使用的MQTT接口。选择Artemis，相当于一鱼四吃。
 * 所以，我们这里直接选择ActiveMQ Artemis。从官网下载(https://activemq.apache.org/components/artemis/download/)最新的2.x版本，解压后设置环境变量ARTEMIS_HOME，指向Artemis根目录，例如C:\Apps\artemis，然后，把ARTEMIS_HOME/bin加入PATH环境变量：
 * 1.Windows下添加%ARTEMIS_HOME%\bin到Path路径；
 * 2.Mac和Linux下添加$ARTEMIS_HOME/bin到PATH路径。
 * Artemis有个很好的设计，就是它把程序和数据完全分离了。我们解压后的ARTEMIS_HOME目录是程序目录，要启动一个Artemis服务，还需要创建一个数据目录。
 * 我们把数据目录直接设定在项目spring-integration-jms的jms-data目录下。执行命令artemis create jms-data：+
 * 在创建过程中，会要求输入连接用户和口令，这里我们设定admin和password，以及是否允许匿名访问（这里选择N）。
 * 此数据目录jms-data不仅包含消息数据、日志，还自动创建了两个启动服务的命令bin/artemis和bin/artemis-service，前者在前台启动运行，按Ctrl+C结束，后者会一直在后台运行。
 * 我们把目录切换到jms-data/bin，直接运行artemis run即可启动Artemis服务：
 * 启动成功后，Artemis提示可以通过URLhttp://localhost:8161/console访问管理后台。注意不要关闭命令行窗口。
 * 在编写JMS代码之前，我们首先得理解JMS的消息模型。JMS把生产消息的一方称为Producer，处理消息的一方称为Consumer。有两种类型的消息通道，
 * 一种是Queue：
 *  ┌────────┐    ┌────────┐    ┌────────┐
	│Producer│───>│ Queue  │───>│Consumer│
	└────────┘    └────────┘    └────────┘
	一种是Topic：
	
	                            ┌────────┐
	                         ┌─>│Consumer│
	                         │  └────────┘
	┌────────┐    ┌────────┐ │  ┌────────┐
	│Producer│───>│ Topic  │─┼─>│Consumer│
	└────────┘    └────────┘ │  └────────┘
	                         │  ┌────────┐
	                         └─>│Consumer│
	                            └────────┘
 * 它们的区别在于，Queue是一种一对一的通道，如果Consumer离线无法处理消息时，Queue会把消息存起来，等Consumer再次连接的时候发给它。设定了持久化机制的Queue不会丢失消息。
 * 如果有多个Consumer接入同一个Queue，那么它们等效于以集群方式处理消息，例如，发送方发送的消息是A，B，C，D，E，F，两个Consumer可能分别收到A，C，E和B，D，F，即每个消息只会交给其中一个Consumer处理。
 * Topic则是一种一对多通道。一个Producer发出的消息，会被多个Consumer同时收到，即每个Consumer都会收到一份完整的消息流。那么问题来了：如果某个Consumer暂时离线，过一段时间后又上线了，那么在它离线期间产生的消息还能不能收到呢？
 * 这取决于消息服务器对Topic类型消息的持久化机制。如果消息服务器不存储Topic消息，那么离线的Consumer会丢失部分离线时期的消息，如果消息服务器存储了Topic消息，
 * 那么离线的Consumer可以收到自上次离线时刻开始后产生的所有消息。JMS规范通过Consumer指定一个持久化订阅可以在上线后收取所有离线期间的消息，如果指定的是非持久化订阅，那么离线期间的消息会全部丢失。
 * 细心的童鞋可以看出来，如果一个Topic的消息全部都持久化了，并且只有一个Consumer，那么它和Queue其实是一样的。实际上，很多消息服务器内部都只有Topic类型的消息架构，Queue可以通过Topic“模拟”出来。
 * 无论是Queue还是Topic，对Producer没有什么要求。多个Producer也可以写入同一个Queue或者Topic，此时消息服务器内部会自动排序确保消息总是有序的。
 * 以上是消息服务的基本模型。具体到某个消息服务器时，Producer和Consumer通常是通过TCP连接消息服务器，在编写JMS程序时，又会遇到ConnectionFactory、Connection、Session等概念，其实这和JDBC连接是类似的：
 * 1.ConnectionFactory：代表一个到消息服务器的连接池，类似JDBC的DataSource；
 * 2.Connection：代表一个到消息服务器的连接，类似JDBC的Connection；
 * 3.Session：代表一个经过认证后的连接会话；
 * 4.Message：代表一个消息对象。
 * 在JMS 1.1中，发送消息的典型代码如下：
 * try {
	    Connection connection = null;
	    try {
	        // 创建连接:
	        connection = connectionFactory.createConnection();
	        // 创建会话:
	        Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
	        // 创建一个Producer并关联到某个Queue:
	        MessageProducer messageProducer = session.createProducer(queue);
	        // 创建一个文本消息:
	        TextMessage textMessage = session.createTextMessage(text);
	        // 发送消息:
	        messageProducer.send(textMessage);
	    } finally {
	        // 关闭连接:
	        if (connection != null) {
	            connection.close();
	        }
	    }
	} catch (JMSException ex) {
	    // 处理JMS异常
	}
 * JMS 2.0改进了一些API接口，发送消息变得更简单：
 * try (JMSContext context = connectionFactory.createContext()) {
	    context.createProducer().send(queue, text);
	}
 * JMSContext实现了AutoCloseable接口，可以使用try(resource)语法，代码更简单。
 * 有了以上预备知识，我们就可以开始开发JMS应用了。
 * 首先，我们在pom.xml中添加如下依赖：
 * 1.org.springframework:spring-jms:5.2.0.RELEASE
 * 2.javax.jms:javax.jms-api:2.0.1
 * 3.org.apache.activemq:artemis-jms-client:2.13.0
 * 4.io.netty:netty-handler-proxy:4.1.45.Final
 * 在AppConfig中，通过@EnableJms让Spring自动扫描JMS相关的Bean，并加载JMS配置文件jms.properties：
 * 首先要创建的Bean是ConnectionFactory，即连接消息服务器的连接池：
 * 因为我们使用的消息服务器是ActiveMQ Artemis，所以ConnectionFactory的实现类就是消息服务器提供的ActiveMQJMSConnectionFactory，它需要的参数均由配置文件读取后传入，并设置了默认值。
 * 我们再创建一个JmsTemplate，它是Spring提供的一个工具类，和JdbcTemplate类似，可以简化发送消息的代码：
 * 下一步要创建的是JmsListenerContainerFactory，
 * 除了必须指定Bean的名称为jmsListenerContainerFactory外，这个Bean的作用是处理和Consumer相关的Bean。我们先跳过它的原理，继续编写MessagingService来发送消息：
 * JMS的消息类型支持以下几种：
 * 1.TextMessage：文本消息；
 * 2.BytesMessage：二进制消息；
 * 3.MapMessage：包含多个Key-Value对的消息；
 * 4.ObjectMessage：直接序列化Java对象的消息；
 * 5.StreamMessage：一个包含基本类型序列的消息。
 * 最常用的是发送基于JSON的文本消息，上述代码通过JmsTemplate创建一个TextMessage并发送到名称为jms/queue/mail的Queue。
 * 注意：Artemis消息服务器默认配置下会自动创建Queue，因此不必手动创建一个名为jms/queue/mail的Queue，但不是所有的消息服务器都会自动创建Queue，生产环境的消息服务器通常会关闭自动创建功能，需要手动创建Queue。
 * 再注意到MailMessage是我们自己定义的一个JavaBean，真正的JMS消息是创建的TextMessage，它的内容是JSON。
 * 当用户注册成功后，我们就调用MessagingService.sendMailMessage()发送一条JMS消息，此代码十分简单，这里不再贴出。
 * 下面我们要详细讨论的是如何处理消息，即编写Consumer。从理论上讲，可以创建另一个Java进程来处理消息，但对于我们这个简单的Web程序来说没有必要，直接在同一个Web应用中接收并处理消息即可。
 * 处理消息的核心代码是编写一个Bean，并在处理方法上标注@JmsListener：
 * 注意到@JmsListener指定了Queue的名称，因此，凡是发到此Queue的消息都会被这个onMailMessageReceived()方法处理，方法参数是JMS的Message接口，我们通过强制转型为TextMessage并提取JSON，反序列化后获得自定义的JavaBean，也就获得了发送邮件所需的所有信息。
 * 下面问题来了：Spring处理JMS消息的流程是什么？
 * 如果我们直接调用JMS的API来处理消息，那么编写的代码大致如下：
 * // 创建JMS连接:
	Connection connection = connectionFactory.createConnection();
	// 创建会话:
	Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	// 创建一个Consumer:
	MessageConsumer consumer = session.createConsumer(queue);
	// 为Consumer指定一个消息处理器:
	consumer.setMessageListener(new MessageListener() { 
	    public void onMessage(Message message) {
	        // 在此处理消息... 
	    }
	});
	// 启动接收消息的循环:
	connection.start();
 * 我们自己编写的MailMessageListener.onMailMessageReceived()相当于消息处理器：
 * consumer.setMessageListener(new MessageListener() { 
	    public void onMessage(Message message) {
	        mailMessageListener.onMailMessageReceived(message); 
	    }
	});
 * 所以，Spring根据AppConfig的注解@EnableJms自动扫描带有@JmsListener的Bean方法，并为其创建一个MessageListener把它包装起来。
 * 注意到前面我们还创建了一个JmsListenerContainerFactory的Bean，它的作用就是为每个MessageListener创建MessageConsumer并启动消息接收循环。
 * 再注意到@JmsListener还有一个concurrency参数，10表示可以最多同时并发处理10个消息，5-10表示并发处理的线程可以在5~10之间调整。
 * 因此，Spring在通过MessageListener接收到消息后，并不是直接调用mailMessageListener.onMailMessageReceived()，而是用线程池调用，因此，要时刻牢记，onMailMessageReceived()方法可能被多线程并发执行，一定要保证线程安全。
 * 我们总结一下Spring接收消息的步骤：
 * 通过JmsListenerContainerFactory配合@EnableJms扫描所有@JmsListener方法，自动创建MessageConsumer、MessageListener以及线程池，启动消息循环接收处理消息，最终由我们自己编写的@JmsListener方法处理消息，可能会由多线程同时并发处理。
 * 要验证消息发送和处理，我们注册一个新用户，可以看到如下日志输出：
 * 2020-06-02 08:04:27 INFO  c.i.learnjava.web.UserController - user registered: bob@example.com
2020-06-02 08:04:27 INFO  c.i.l.service.MailMessageListener - received message: ActiveMQMessage[ID:9fc5...]:PERSISTENT/ClientMessageImpl[messageID=983, durable=true, address=jms/queue/mail, ...]]
2020-06-02 08:04:27 INFO  c.i.learnjava.service.MailService - [send mail] sending registration mail to bob@example.com...
2020-06-02 08:04:30 INFO  c.i.learnjava.service.MailService - [send mail] registration mail was sent to bob@example.com.
 * 可见，消息被成功发送到Artemis，然后在很短的时间内被接收处理了。
 * 使用消息服务对发送Email进行改造的好处是，发送Email的能力通常是有限的，通过JMS消息服务，如果短时间内需要给大量用户发送Email，可以先把消息堆积在JMS服务器上慢慢发送，对于批量发送邮件、短信等尤其有用。
 * 小结
 * JMS是Java消息服务，可以通过JMS服务器实现消息的异步处理。
 * 消息服务主要解决Producer和Consumer生产和处理速度不匹配的问题。
 */
@Configuration
@ComponentScan
@EnableWebMvc
@EnableJms//启用JMS
@EnableTransactionManagement
@PropertySource({"classpath:/jdbc.properties","classpath:/jms.properties"})
public class AppConfig {
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Bean
	WebMvcConfigurer createWebMvcConfigurer(@Autowired HandlerInterceptor[] interceptors) {
		return new WebMvcConfigurer() {
			@Override
			public void addInterceptors(InterceptorRegistry registry) {
				for (HandlerInterceptor interceptor : interceptors) {
					registry.addInterceptor(interceptor);
				}
			}
			
			@Override
			public void addResourceHandlers(ResourceHandlerRegistry registry) {
				registry.addResourceHandler("/static/**").addResourceLocations("/static/");
			}
		};
	}
	
	@Bean
	ObjectMapper createObjectMapper() {
		ObjectMapper om = new ObjectMapper();
		om.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		return om;
	}
	
	@Bean
	ViewResolver createViewResolver(@Autowired ServletContext servletContext) {
		PebbleEngine engine = new PebbleEngine.Builder().autoEscaping(true)
				.cacheActive(false)
				.loader(new ServletLoader(servletContext))
				.extension(new SpringExtension())
				.build();
		PebbleViewResolver viewResolver = new PebbleViewResolver();
		viewResolver.setPrefix("/WEB-INF/templates");
		viewResolver.setSuffix("");
		viewResolver.setPebbleEngine(engine);
		return viewResolver;
	}
	
	/*
	 * 我们再创建一个JmsTemplate，它是Spring提供的一个工具类，和JdbcTemplate类似，可以简化发送消息的代码：
	 */
	@Bean
	JmsTemplate createJmsTemplate(@Autowired ConnectionFactory connectionFactory) {
		return new JmsTemplate(connectionFactory);
	}
	
	/*
	 * 下一步要创建的是JmsListenerContainerFactory，
	 * 除了必须指定Bean的名称为jmsListenerContainerFactory外，这个Bean的作用是处理和Consumer相关的Bean。我们先跳过它的原理，继续编写MessagingService来发送消息：
	 */
	@Bean("jmsListenerContainerFactory")
	DefaultJmsListenerContainerFactory createJmsListenerContainerFactory(@Autowired ConnectionFactory connectionFactory) {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory);
		return factory;
	}
	
	/*
	 * 首先要创建的Bean是ConnectionFactory，即连接消息服务器的连接池：
	 * 因为我们使用的消息服务器是ActiveMQ Artemis，所以ConnectionFactory的实现类就是消息服务器提供的ActiveMQJMSConnectionFactory，它需要的参数均由配置文件读取后传入，并设置了默认值。
	 */
	@Bean
	ConnectionFactory createJMSConnectionFactory(@Value("${jms.uri.tcp://localhost:61616}")String uri,@Value("${jms.username:admin}")String username,
			@Value("${jms.password:password}")String password) {
		logger.info("create JMS connection factory for standalone activemq artemis server...");
		return new ActiveMQJMSConnectionFactory(uri, username, password);
	}
	
	@Bean
	DataSource createDataSource(@Value("${jdbc.url}")String jdbcUrl, @Value("${jdbc.username}")String jdbcUsername, @Value("${jdbc.password}")String jdbcPassword) {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl(jdbcUrl);
		config.setUsername(jdbcUsername);
		config.setPassword(jdbcPassword);
		config.addDataSourceProperty("autoCommit", "false");
		config.addDataSourceProperty("connectionTimeout", "5");
		config.addDataSourceProperty("idleTimeout", "60");
		return new HikariDataSource(config);
	}
	
	
	@Bean
	JdbcTemplate createJdbcTemplate(@Autowired DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}
	
	@Bean
	PlatformTransactionManager createTxManager(@Autowired DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}
	
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
