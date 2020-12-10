package com.yale.test.springmvc.rest.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.yale.test.springmvc.rest.entity.User;
import com.yale.test.springmvc.rest.service.UserService;

@Controller
public class UserController {
	public static final String KEY_USER = "__user__";
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	UserService userService;
	
	/*
	 * 直接在Controller中处理JSON是可以的，因为Spring MVC的@GetMapping和@PostMapping都支持指定输入和输出的格式。如果我们想接收JSON，输出JSON，那么可以这样写：
	 * 对应的Maven工程需要加入Jackson这个依赖：com.fasterxml.jackson.core:jackson-databind:2.11.0
	 * 注意到@PostMapping使用consumes声明能接收的类型，使用produces声明输出的类型，并且额外加了@ResponseBody表示返回的String无需额外处理，
	 * 直接作为输出内容写入HttpServletResponse。输入的JSON则根据注解@RequestBody直接被Spring反序列化为User这个JavaBean。
	 */
	@PostMapping(value="/reset", consumes="application/json;charset=utf-8", produces = "application/json;charset=utf-8")
	@ResponseBody
	public String rest(@RequestBody User user) {
		return "{\"restSupport\":true}";
	}
	
	@GetMapping("/")
	public ModelAndView index(HttpSession session){
		User user = (User)session.getAttribute(KEY_USER);
		Map<String, Object> model = new HashMap<>();
		if (user != null) {
			model.put("user", model);
		}
		return new ModelAndView("index.html", model);
	}
	
	@GetMapping("/register")
	public ModelAndView register() {
		return new ModelAndView("register.html");
	}
	
	@PostMapping("/register")
	public ModelAndView doRegister(@RequestParam("email") String email, @RequestParam("password")String password,
			@RequestParam("name")String name) {
		try {
			User user = userService.register(email, password, name);
			logger.info("user registered: {}", user.getEmail());
		} catch (Exception e) {
			e.printStackTrace();
			Map<String, String> res = new HashMap<String, String>();
			res.put("email", email);
			res.put("error", "Register failed");
			return new ModelAndView("register.html", res);
		}
		return new ModelAndView("redirect:/signin");
	}
	
	@GetMapping("/signin")
	public ModelAndView signin(HttpSession session) {
		User user = (User)session.getAttribute(KEY_USER);
		if (user != null) {
			return new ModelAndView("redirect:/profile");
		}
		return new ModelAndView("signin.html");
	}
	
	@PostMapping("/signin")
	public ModelAndView doSignin(@RequestParam("email") String email, @RequestParam("password")String password, HttpSession session) {
		try {
			User user = userService.signin(email, password);
			session.setAttribute(KEY_USER, user);
		} catch(RuntimeException e){
			Map<String, String> res = new HashMap<String, String>();
			res.put("email", email);
			res.put("error", "Signin failed");
			return new ModelAndView("signin.html", res);
		}
		return new ModelAndView("redirect:/profile");
	}
	
	@GetMapping("/profile")
	public ModelAndView profile(HttpSession session) {
		User user = (User)session.getAttribute(KEY_USER);
		if (user == null) {
			return new ModelAndView("redirect:/signin");
		}
		Map<String, Object> res = new HashMap<String, Object>();
		res.put("user", user);
		return new ModelAndView("profile.html", res);
	}
	
	@GetMapping("/signout")
	public String sigout(HttpSession session) {
		session.removeAttribute(KEY_USER);
		return "redirect:/signin";
	}
}
