package com.yale.test.spring.aop.lxf.metrics.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yale.test.spring.aop.lxf.metrics.MetricTime;

@Component
public class UserService {
	@Autowired
	MailService mailService;
	private List<User> users = new ArrayList<User>();
	public UserService(@Autowired MailService mailService) {
		this.mailService = mailService;
		users.add(new User(1, "bob@example.com", "password", "Bob"));//bob
		users.add(new User(2, "alice@example.com", "password", "Alice"));//alice
		users.add(new User(3, "tom@example.com", "password", "Bob"));//tom
	}
	
	public User login(String email, String password) {
		for (User user : users) {
			if (user.getEmail().equalsIgnoreCase(email) && user.getPassword().equals(password)) {
				mailService.sendLoginMail(user);
				return user;
			}
		}
		throw new RuntimeException("login failed.");
	}
	
	public User getUser(long id) {
		//jdk8没有orElseThrow这个方法
		//return this.users.stream().filter(user -> user.getId() == id).findFirst().orElseThrow();
		return this.users.stream().filter(user -> user.getId() == id).findFirst().orElse(new User(1,"","",""));
	}
	
	@MetricTime("register")//在需要被监控的关键方法上标注该注解：监控register()方法性能:
	public User register(String email, String password, String name) {
		users.forEach((user)->{
			if (user.getEmail().equalsIgnoreCase(email)) {
				throw new RuntimeException("email exist.");
			}
		});
		User user = new User(users.stream().mapToLong(u->u.getId()).max().getAsLong(), email, password, name);
		users.add(user);
		mailService.sendRegistrationMail(user);
		return user;
	}
	
}
