package com.yale.test.spring.customize.service;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)//@Scope("prototype")
public class MailSession {
	public MailSession() {
		System.out.println("new MailSession(),Spring每次都会创建一个新的实例");
	}
}

