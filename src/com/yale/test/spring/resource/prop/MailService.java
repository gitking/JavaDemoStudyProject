package com.yale.test.spring.resource.prop;

import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/*
 * 注意观察#{}这种注入语法，它和${key}不同的是，#{}表示从JavaBean读取属性。"#{smtpConfig.host}"的意思是，从名称为smtpConfig的Bean读取host属性，即调用getHost()方法。
 * 一个Class名为SmtpConfig的Bean，它在Spring容器中的默认名称就是smtpConfig，除非用@Qualifier指定了名称。
 * 使用一个独立的JavaBean持有所有属性，然后在其他Bean中以#{bean.property}注入的好处是，多个Bean都可以引用同一个Bean的某个属性。
 * 例如，如果SmtpConfig决定从数据库中读取相关配置项，那么MailService注入的@Value("#{smtpConfig.host}")仍然可以不修改正常运行。
 */
@Component
public class MailService {
	@Value("#${smtpConfig.host}")
	private String smtpHost;
	
	@Value("#${smtpConfig.port}")
	private int smtpPort;
	
	@Autowired
	private ZoneId zoneId;
	
	public void sendWelcomeMail(){
		System.out.println("at zone: " + zoneId);
		System.out.println("sent by smtp host: " + smtpHost);
		System.out.println("sent by smtp prot: " + smtpPort);
	}
}
