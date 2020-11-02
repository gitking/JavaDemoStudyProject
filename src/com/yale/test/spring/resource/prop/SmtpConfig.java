package com.yale.test.spring.resource.prop;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/*
 * 另一种注入配置的方式是先通过一个简单的JavaBean持有所有的配置，例如，一个SmtpConfig：
 * 然后，在需要读取的地方，使用#{smtpConfig.host}注入：
 * 注意观察#{}这种注入语法，它和${key}不同的是，#{}表示从JavaBean读取属性。"#{smtpConfig.host}"的意思是，
 * 从名称为smtpConfig的Bean读取host属性，即调用getHost()方法。一个Class名为SmtpConfig的Bean，它在Spring容器中的默认名称就是smtpConfig，除非用@Qualifier指定了名称。
 */
@Component
public class SmtpConfig {
	@Value("${smtp.host}")
	private String host;
	
	@Value("${smtp.prot:25}")
	private int port;
	
	public String getHost() {
		return host;
	}
	
	public int getPort() {
		return port;
	}
}
