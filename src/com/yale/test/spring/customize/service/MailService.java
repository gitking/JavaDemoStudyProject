package com.yale.test.spring.customize.service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.yale.test.spring.lxf.service.User;

@Component
public class MailService {
	
	/*
	 * 可选注入
	 * 默认情况下，当我们标记了一个@Autowired后，Spring如果没有找到对应类型的Bean，它会抛出NoSuchBeanDefinitionException异常。
	 * 可以给@Autowired增加一个required = false的参数：
	 * 这个参数告诉Spring容器，如果找到一个类型为ZoneId的Bean，就注入，如果找不到，就忽略。
	 * 这种方式非常适合有定义就使用定义，没有就使用默认值的情况。
	 */
	@Autowired(required = false)
	@Qualifier("z")// 指定注入名称为"z"的ZoneId
	ZoneId zoneId = ZoneId.systemDefault();
	
	@PostConstruct
	public void init() {
		System.out.println("Init mail service with zoneId = " + this.zoneId);
	}
	
	@PreDestroy
	public void shutdown() {//要手动调用applicationContext.close()不能直接点结束进程
		System.out.println("Shutdown mail service。要手动调用applicationContext.close()不能直接点结束进程");
	}
	
	public String getTime() {
		return ZonedDateTime.now(this.zoneId).format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
	}
	
	public void sendLoginMail(User user) {
		System.out.println(String.format("Hi, %s! You are logged in at %s", user.getName(), getTime()));
	}
	
	public void sendRegistrationMail(User user) {
		System.err.println(String.format("Welcome, %s!", user.getName()));
	}
}
