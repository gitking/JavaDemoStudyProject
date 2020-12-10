package com.yale.test.springmvc.interceptor.web;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.yale.test.springmvc.rest.service.UserService;

@Controller
public class UserController {
	public static final String KEY_USER = "__user__";
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	UserService userService;
	
	/*
	 * 处理异常
	 * 在Controller中，Spring MVC还允许定义基于@ExceptionHandler注解的异常处理方法。我们来看具体的示例代码：
	 * 异常处理方法没有固定的方法签名，可以传入Exception、HttpServletRequest等，返回值可以是void，也可以是ModelAndView，
	 * 下面代码通过@ExceptionHandler(RuntimeException.class)表示当发生RuntimeException的时候，就自动调用此方法处理。
	 * 注意到我们返回了一个新的ModelAndView，这样在应用程序内部如果发生了预料之外的异常，可以给用户显示一个出错页面，
	 * 而不是简单的500 Internal Server Error或404 Not Found。例如B站的错误页：
	 * 可以编写多个错误处理方法，每个方法针对特定的异常。例如，处理LoginException使得页面可以自动跳转到登录页。
	 * 使用ExceptionHandler时，要注意它仅作用于当前的Controller，即ControllerA中定义的一个ExceptionHandler方法对ControllerB不起作用。
	 * 如果我们有很多Controller，每个Controller都需要处理一些通用的异常，例如LoginException，思考一下应该怎么避免重复代码？
	 * SNH48-刘慈欣 答:所有的思考题都是google题，靠自己瞎琢磨/瞎翻文档根本琢磨不出来，但是网上一搜就搜到了标准答案
	 * 我甚至在琢磨怎么用Interceptor拦截Exception：用postHandle()吧，没法捕获Controller方法的异常，根本拿不到异常信息；用afterCompletion()吧，ModelAndView已经渲染了，总不能自己手动再写个response吧。
	 * 琢磨半天我还是放弃了，还是google吧，发现办法居然如此简单：
	 * https://howtodoinjava.com/spring-core/spring-exceptionhandler-annotation/#4
	 * 直接写一个类里面弄上handleUnknowException方法，标注上@ControllerAdvice就行了：
	 * @ControllerAdvice
		public class InternalExceptionHandler {
		
			@ExceptionHandler(RuntimeException.class)
			public ModelAndView handleUnknowException(Exception ex) {
				return new ModelAndView("500.html", Map.of("error", ex.getClass().getSimpleName(), "message", ex.getMessage()));
			}
		
		}
	 * 等效于在所有的Controller里写上了
	 * @ExceptionHandler(RuntimeException.class)
		public ModelAndView handleUnknowException(Exception ex) {
			return new ModelAndView("500.html", Map.of("error", ex.getClass().getSimpleName(), "message", ex.getMessage()));
		}
	 * 廖雪峰点评:其实你在父类写个handleUnknowException()就可以了，因为controller实际上按url分类不会太多，统一继承base-controller是完全可行的，公共方法也放base
	 * https://www.liaoxuefeng.com/wiki/1252599548343744/1347180610715681#0
	 */
	@ExceptionHandler(RuntimeException.class)
	public ModelAndView handleUnknowException(Exception ex) {
		Map<String, String> res = new HashMap<String, String>();
		res.put("error", ex.getClass().getSimpleName());
		res.put("message", ex.getMessage());
		return new ModelAndView("500.html", res);
	}
	
	
	
}
