package com.yale.test.springmvc.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yale.test.springmvc.vo.User;

@Controller
public class LoginAnnotationController {
	
	@RequestMapping("/login")
	public String login(User user, HttpSession session){
		System.out.println("LoginAnnotationController被执行了");
		if ("admin".equals(user.getName()) && "1111".equals(user.getPwd())) {
			session.setAttribute("user", user);
			return "redirect:/springmvc/index.jsp";
		}
		return "redirect:/springmvc/login.jsp";
	}
	
	@RequestMapping("/add")
	public String add(){
		System.out.println("add方法被执行了");
		return "redirect:/springmvc/index.jsp";
	}
}
