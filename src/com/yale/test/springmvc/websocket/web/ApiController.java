package com.yale.test.springmvc.websocket.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yale.test.springmvc.websocket.entity.User;
import com.yale.test.springmvc.websocket.service.UserService;

@RestController
@RequestMapping("/api")
public class ApiController {
	
	@Autowired
	UserService userService;
	
	@GetMapping("/users")
	public List<User> users() {
		return userService.getUsers();
	}
	
	@GetMapping("/users/{id}")
	public User user(@PathVariable("id")long id) {
		return userService.getUserById(id);
	}
}
