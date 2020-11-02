package com.yale.test.web.servlet.lxf.mvc.controller;

import javax.servlet.http.HttpSession;

import com.yale.test.web.servlet.lxf.mvc.bean.User;
import com.yale.test.web.servlet.lxf.mvc.framework.GetMapping;
import com.yale.test.web.servlet.lxf.mvc.framework.ModelAndView;

public class IndexController {
	@GetMapping("/")
	public ModelAndView index(HttpSession session) {
		User user = (User) session.getAttribute("user");
		return new ModelAndView("/index.html", "user", user);
	}
	
	@GetMapping("/hello")
	public ModelAndView hello(String name) {
		if (name == null) {
			name = "World";
		}
		return new ModelAndView("/hello.html", "name", name);
	}
}
