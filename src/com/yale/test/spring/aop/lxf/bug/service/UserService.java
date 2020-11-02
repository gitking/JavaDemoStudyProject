package com.yale.test.spring.aop.lxf.bug.service;

import java.time.ZoneId;

import org.springframework.stereotype.Component;

@Component
public class UserService {
	public final ZoneId zoneId = ZoneId.systemDefault(); // 成员变量:
	
	public UserService() {// 构造方法:
		System.out.println("UserSevice(): init...");
		System.out.println("UserSevice(): zoneId = " + this.zoneId);
	}
	
	public ZoneId getZoneId() { // public方法:
		return zoneId;
	}
	
	public final ZoneId getFinalZoneId() {// public final方法:
		return zoneId;
	}
}
