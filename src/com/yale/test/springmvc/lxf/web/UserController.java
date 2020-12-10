package com.yale.test.springmvc.lxf.web;

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

import com.yale.test.springmvc.lxf.entity.User;
import com.yale.test.springmvc.lxf.service.UserService;

/*
 * 编写Controller
 * 有了Web应用程序的最基本的结构，我们的重点就可以放在如何编写Controller上。Spring MVC对Controller没有固定的要求，也不需要实现特定的接口。以UserController为例，编写Controller只需要遵循以下要点：
 * 总是标记@Controller而不是@Component：
 * 一个方法对应一个HTTP请求路径，用@GetMapping或@PostMapping表示GET或POST请求：
 * 需要接收的HTTP参数以@RequestParam()标注，可以设置默认值。如果方法参数需要传入HttpServletRequest、HttpServletResponse或者HttpSession，直接添加这个类型的参数即可，Spring MVC会自动按类型传入。
 * 返回的ModelAndView通常包含View的路径和一个Map作为Model，但也可以没有Model，例如：
 * return new ModelAndView("signin.html"); // 仅View，没有Model
 * 返回重定向时既可以写new ModelAndView("redirect:/signin")，也可以直接返回String：
 * 如果在方法内部直接操作HttpServletResponse发送响应，返回null表示无需进一步处理：
 * public ModelAndView download(HttpServletResponse response) {
	    byte[] data = ...
	    response.setContentType("application/octet-stream");
	    OutputStream output = response.getOutputStream();
	    output.write(data);
	    output.flush();
	    return null;
	}
 * 对URL进行分组，每组对应一个Controller是一种很好的组织形式，并可以在Controller的class定义出添加URL前缀，例如：
 * @Controller
	@RequestMapping("/user")
	public class UserController {
	    // 注意实际URL映射是/user/profile
	    @GetMapping("/profile")
	    public ModelAndView profile() {
	        ...
	    }
	
	    // 注意实际URL映射是/user/changePassword
	    @GetMapping("/changePassword")
	    public ModelAndView changePassword() {
	        ...
	    }
	}
 * 实际方法的URL映射总是前缀+路径，这种形式还可以有效避免不小心导致的重复的URL映射。
 * 可见，Spring MVC允许我们编写既简单又灵活的Controller实现。
 * 练习
 * 在注册、登录等功能的基础上增加一个修改口令的页面。
 * 小结
 * 使用Spring MVC时，整个Web应用程序按如下顺序启动：
 * 1.启动Tomcat服务器；
 * 2.Tomcat读取web.xml并初始化DispatcherServlet；
 * 3.DispatcherServlet创建IoC容器并自动注册到ServletContext中。
 * 启动后，浏览器发出的HTTP请求全部由DispatcherServlet接收，并根据配置转发到指定Controller的指定方法处理。
 */
@Controller
public class UserController {
	public static final String KEY_USER = "__user__";
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	UserService userService;
	
	@GetMapping("/")
	public ModelAndView index(HttpSession session) {
		User user = (User)session.getAttribute(KEY_USER);
		Map<String, Object> model = new HashMap<>();
		if (user != null) {
			model.put("user", user);
		}
		return new ModelAndView("index.html", model);
	}
	
	@GetMapping("/register")
	public ModelAndView register() {
		return new ModelAndView("register.html");// 仅View，没有Model
	}
	
	@PostMapping("/register")
	public ModelAndView doRegister(@RequestParam("email")String email, @RequestParam("password")String password,
			@RequestParam("name")String name) {
		try {
			User user = userService.register(email, password, name);
			logger.info("user registered: {}", user.getEmail());
		} catch (RuntimeException e) {
			Map<String, String> info = new HashMap<String, String>();
			info.put("eamil", email);
			info.put("error", "Register failed");
			return new ModelAndView("register.html", info);
		}//返回重定向时既可以写new ModelAndView("redirect:/signin")，也可以直接返回String：
		return new ModelAndView("redirect:/signin");
	}
	
	@GetMapping("/signin")
	public ModelAndView signin(HttpSession session) {
		User user = (User)session.getAttribute(KEY_USER);
		if (user != null) {
			return new ModelAndView("redirect:/profile");
		}
		return new ModelAndView("signin.html");// 仅View，没有Model
	}
	
	@PostMapping("/signin")
	public ModelAndView doSignin(@RequestParam("email")String email, @RequestParam("password")String password, HttpSession session) {
		try { 
			User user = userService.signin(email, password);
			session.setAttribute(KEY_USER, user);
		} catch(RuntimeException e) {
			Map<String, String> info = new HashMap<String, String>();
			info.put("email", email);
			info.put("error", "Signin failed");
			return new ModelAndView("signin.html", info);
		}
		return new ModelAndView("redirect:/profile");
	}
	
	@GetMapping("profile")
	public ModelAndView profile(HttpSession session) {
		User user = (User)session.getAttribute(KEY_USER);
		if (user == null) {
			return new ModelAndView("redirect:/signin");
		}
		Map<String, Object> info = new HashMap<String, Object>();
		info.put("user", user);
		return new ModelAndView("profile.html", info);
	}
	
	@GetMapping("/sigout")
	public String sigout(HttpSession session) {
		session.removeAttribute(KEY_USER);
		//返回重定向时既可以写new ModelAndView("redirect:/signin")，也可以直接返回String：
		return "redirect:/signin";
	}
	
	public ModelAndView download() {
		//如果在方法内部直接操作HttpServletResponse发送响应，返回null表示无需进一步处理：
		return null;
	}
}
