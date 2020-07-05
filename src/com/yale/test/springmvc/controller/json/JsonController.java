package com.yale.test.springmvc.controller.json;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yale.test.springmvc.vo.User;

@Controller
public class JsonController {
	@RequestMapping("/json")
	@ResponseBody//这个是必须的
	public List<User> returnJson(){
		/*
		 * http://localhost:8080/pcis/springmvc/ajaxjson.jsp
		 * 使用SpringMVC往前台返回JSON格式的数据,都不需要自己将对象转换为JSON,SpringMVC自己就帮我们转换了,非常舒服
		 * 那SpringMVC是怎么知道我要给前台返回的是JSON格式的数据呢？难道因为前台浏览器的JS根本不支持List<User>对象?
		 * 还是因为我在配置文件里面配置了stringConverter,jsonConverter,RequestMappingHandlerAdapter
		 */
		List<User> list = new ArrayList<User>();
		list.add(new User(1, "zhangsan", "123"));
		list.add(new User(2, "nico", "female"));
		list.add(new User(1, "jackson", "456"));
		return list;
	}
}
