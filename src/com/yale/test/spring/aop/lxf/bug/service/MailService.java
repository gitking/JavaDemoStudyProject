package com.yale.test.spring.aop.lxf.bug.service;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MailService {
	@Autowired
	UserService userService;
	
	public String sendMail() {
		ZoneId zoneId = userService.zoneId;//启动代理后这里会变成null
		String dt = ZonedDateTime.now(zoneId).toString();
		return "Hello, it is " + dt;
	}
}
