package com.yale.test.spring.integration.mail;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/*
 * 集成JavaMail
 * 我们在发送Email和接收Email中已经介绍了如何通过JavaMail来收发电子邮件。在Spring中，同样可以集成JavaMail。
 * 因为在服务器端，主要以发送邮件为主，例如在注册成功、登录时、购物付款后通知用户，基本上不会遇到接收用户邮件的情况，所以本节我们只讨论如何在Spring中发送邮件。
 * 在Spring中，发送邮件最终也是需要JavaMail，Spring只对JavaMail做了一点简单的封装，目的是简化代码。为了在Spring中集成JavaMail，我们在pom.xml中添加以下依赖：
 * org.springframework:spring-context-support:5.2.0.RELEASE
 * javax.mail:javax.mail-api:1.6.2
 * com.sun.mail:javax.mail:1.6.2
 * 以及其他Web相关依赖。
 * 我们希望用户在注册成功后能收到注册邮件，为此，我们先定义一个JavaMailSender的Bean：
 * 这个JavaMailSender接口的实现类是JavaMailSenderImpl，初始化时，传入的参数与JavaMail是完全一致的。
 * 另外注意到需要注入的属性是从smtp.properties中读取的，因此，AppConfig导入的就不止一个.properties文件，可以导入多个：
 * @PropertySource({ "classpath:/jdbc.properties", "classpath:/smtp.properties" })
 * 下一步是封装一个MailService，并定义sendRegistrationMail()方法：
 * 在MVC的某个Controller方法中，当用户注册成功后，我们就启动一个新线程来异步发送邮件：
 * User user = userService.register(email, password, name);
	logger.info("user registered: {}", user.getEmail());
	// send registration mail:
	new Thread(() -> {
	    mailService.sendRegistrationMail(user);
	}).start();
 * 因为发送邮件是一种耗时的任务，从几秒到几分钟不等，因此，异步发送是保证页面能快速显示的必要措施。这里我们直接启动了一个新的线程，但实际上还有更优化的方法，我们在下一节讨论。
 * 小结
 * Spring可以集成JavaMail，通过简单的封装，能简化邮件发送代码。其核心是定义一个JavaMailSender的Bean，然后调用其send()方法。
 */
@Configuration
@ComponentScan
@EnableWebMvc
@EnableTransactionManagement
@PropertySource({"classpath:/jdbc.properties","classpath:/smtp.properties"})
public class AppConfig {
	
	
	@Bean
	JavaMailSender createJavaMailSender(@Value("${smtp.host}")String host, @Value("${smtp.port}")int port, @Value("${smtp.auth}") String auth,
			@Value("${smtp.username}")String username, @Value("${smtp.password}")String password,
			@Value("${smtp.debug:true}")String debug) {
		//这个JavaMailSender接口的实现类是JavaMailSenderImpl，初始化时，传入的参数与JavaMail是完全一致的。
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(host);
		mailSender.setPort(port);
		mailSender.setUsername(username);
		mailSender.setPassword(password);
		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", auth);
		if (port == 587) {
			props.put("mail.smtp.starttls.enable", "true");
		} else if (port == 465) {
			props.put("mail.smtp.socketFactory.port", "465");
			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		}
		props.put("mail.debug", debug);
		return mailSender;
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
	JdbcTemplate createJdbcTemplate(@Autowired DataSource dataSource){
		return new JdbcTemplate(dataSource);
	}
	
	@Bean
	PlatformTransactionManager createTxManager(@Autowired DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}
}
