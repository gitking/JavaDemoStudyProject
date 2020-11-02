package com.yale.test.web.servlet.lxf.mvc.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.yale.test.web.servlet.lxf.mvc.bean.SignInBean;
import com.yale.test.web.servlet.lxf.mvc.bean.User;
import com.yale.test.web.servlet.lxf.mvc.framework.GetMapping;
import com.yale.test.web.servlet.lxf.mvc.framework.ModelAndView;
import com.yale.test.web.servlet.lxf.mvc.framework.PostMapping;

public class UserController {
	
//这个匿名内部类的写法搞不懂
//	private Map<String, User> userDatabase = new HashMap<String, User>(){
//		List<User> users = List.of( //JDK8不支持这种语法
//				new User("bob@example.com", "bob123", "Bob", "This is bob."),
//				new User("tom@example.com", "tomcat", "Tom", "This is tom."));
//		users.forEach(user -> {
//			put(user.email, user);
//		});
//	};

	private Map<String, User> userDatabase = new HashMap<String, User>();

	public UserController() {
		User bobUser = new User("tom@example.com", "tomcat", "Tom", "This is tom.");
		User tomUser = new User("bob@example.com", "bob123", "Bob", "This is boo.");
		userDatabase.put(bobUser.email, bobUser);
		userDatabase.put(tomUser.email, tomUser);
	}
	
	@GetMapping("/signin")
	public ModelAndView signin() {
		return new ModelAndView("/signin.html");
	}
	
	@PostMapping("/sigin")
	public ModelAndView doSignin(SignInBean bean, HttpServletResponse response, HttpSession session)
	throws IOException {
		User user = userDatabase.get(bean.email);
		if (user == null || !user.password.equals(bean.password)) {
			response.setContentType("application/json");
			PrintWriter pw = response.getWriter();
			pw.write("{\"error\":\"Bad emali or password\"}");
			pw.flush();
		} else {
			session.setAttribute("user", user);
			response.setContentType("application/json");
			PrintWriter pw = response.getWriter();
			pw.write("{\"result\":true}");
			pw.flush();
		}
		return null;
	}
	
	@GetMapping("/signout")
	public ModelAndView signout(HttpSession session){
		session.removeAttribute("user");
		return new ModelAndView("redirect:/");
	}
	
	@GetMapping("/user/profile")
	public ModelAndView profile(HttpSession session) {
		User user = (User)session.getAttribute("user");
		if (user == null) {
			return new ModelAndView("redirect:/signin");
		}
		return new ModelAndView("/profile.html", "user", user);
	}
}
