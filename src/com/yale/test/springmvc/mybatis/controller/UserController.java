package com.yale.test.springmvc.mybatis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yale.test.springmvc.mybatis.services.UserService;

@Controller
public class UserController {
	@Autowired
	private UserService userService;

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	@RequestMapping("/list")
	public String list(ModelMap map){
		map.addAttribute("list", userService.list());
		return "/mybatis/list.jsp";
	}
	
}
