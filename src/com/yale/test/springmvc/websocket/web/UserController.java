package com.yale.test.springmvc.websocket.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.yale.test.springmvc.websocket.entity.User;
import com.yale.test.springmvc.websocket.service.UserService;

@Controller
public class UserController {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	public static final String KEY_USER = "__user__";
	
	@Autowired
	UserService userService;
	
	@GetMapping("/")
	public ModelAndView index(HttpSession session) {
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
	public ModelAndView doRegister(@RequestParam("email")String email, @RequestParam("password")String password,
			@RequestParam("name") String name) {
		try {
			User user = userService.register(email, password, name);
			logger.info("user registerred: {}", user.getEmail());
		} catch (Exception e) {
			e.printStackTrace();
			Map<String, String> res = new HashMap<>();
			res.put("email", email);
			res.put("error", "Register failed");
			return new ModelAndView("register.html", res);
		}
		return new ModelAndView("redirect:/sigin");
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
	public ModelAndView doSignin(@RequestParam("email")String email, @RequestParam("password")String password, HttpSession session) {
		try {
			User user = userService.signin(email, password);
			session.setAttribute(KEY_USER, user);
		} catch (Exception e) {
			e.printStackTrace();
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
		if (user != null) {
			return new ModelAndView("redirect:/signin");
		}
		Map<String, Object> res = new HashMap<>();
		res.put("user", res);
		return new ModelAndView("profile.html", res);
	}
	
	@GetMapping("/sigout")
	public String sigout(HttpSession session) {
		session.removeAttribute(KEY_USER);
		return "redirect:/signin";
	}
}
