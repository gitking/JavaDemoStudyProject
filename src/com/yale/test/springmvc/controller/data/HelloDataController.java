package com.yale.test.springmvc.controller.data;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.yale.test.springmvc.vo.User;

@Controller
public class HelloDataController {
	public HelloDataController(){
		System.out.println("**********HelloDataController这个类在服务器启动的时候就被Spring创建了,并且是单例的,注意看我是在什么时候被打印出来的****");
	}
	@RequestMapping("/helloData")
	public String getRequestData(String name){
		//http://localhost:8080/pcis/helloData.springmvc?name=zhangsan
		System.out.println("URL传过来的参数SpringMVC:" + name);
		System.out.println("只要你url的参数名称跟这里的参数名称保持一致就行:" + name);
		return "springdata";
	}
	
	@RequestMapping("/helloDifData")
	public String getRequestDifData(@RequestParam("diffname")String name){
		//http://localhost:8080/pcis/helloDifData.springmvc?diffname=zhangsan
		System.out.println("URL传过来的参数SpringMVC:" + name);
		System.out.println("url的参数名称跟这里的参数名称不一致时需要使用:@RequestParam:" + name);
		return "springdata";
	}
	
	@RequestMapping("/helloObjData")
	public String getRequestObjData(User user){
		//http://localhost:8080/pcis/helloObjData.springmvc?name=zhangsan&pwd=123456
		System.out.println("SpringMVC获取对象Obj数据，URL传过来的参数:" + user);
		System.out.println("SpringMVC非常强大,会自己根据URL的参数找对象的属性进行封装:所以就要求url的参数名称必须跟User的属性名称保持一致.");
		return "springdata";
	}
	
	@RequestMapping("/helloResData")
	public String getReqAndResData(String name, ModelMap model){
		/*
		 * 注意model必须放在getReqAndResData方法的参数里面,放在方法体内没有用,ModelAndView是放在方法体内使用的
		 * 还有一种方式可以将数据返回给前台,就是使用ModelAndView,代码见HelloController的方法handleRequest
		 */
		//http://localhost:8080/pcis/helloResData.springmvc?name=zhangsan&pwd=123456
		//addAttribute相当于request.setAttribute("msg", "hello springmvc")；
		model.addAttribute("msg", "SpringMVC从后台返回的数据");
		System.out.println("getReqAndResData:URL传过来的参数SpringMVC:" + name);
		return "springdata";
	}
	
	@RequestMapping("/helloResDataEncode")
	public String getReqAndResDataEncode(String name, ModelMap model){
		/*
		 * 注意model必须放在getReqAndResData方法的参数里面,放在方法体内没有用,ModelAndView是放在方法体内使用的
		 * 还有一种方式可以将数据返回给前台,就是使用ModelAndView,代码见HelloController的方法handleRequest
		 * 注意如果name传的是中文,那么这里就会乱码,解决办法是使用org.springframework.web.filter.CharacterEncodingFilter
		 */
		//http://localhost:8080/pcis/helloResData.springmvc?name=zhangsan&pwd=123456
		//addAttribute相当于request.setAttribute("msg", "hello springmvc")；
		model.addAttribute("msg", "SpringMVC从后台返回的数据"+name);
		System.out.println("getReqAndResData:URL传过来的参数SpringMVC:" + name);
		return "springdata";
	}
	
	@RequestMapping("/delete/{id}")//这个就是Restful风口
	public String getRestfulType(@PathVariable int id){
		//http://localhost:8080/pcis/delete/100.springmvc
		System.out.println("restful风格获取数据" + id);
		return "springdata";
	}
	
	@RequestMapping("/{id}/{uid}/delete/{num}")//这个就是Restful风口
	public String getRestfulTypeHigh(@PathVariable("num") int seq, @PathVariable int uid, @PathVariable int id){
		//http://localhost:8080/pcis/99/101/delete/888.springmvc
		System.out.println("restful风格获取数据uid:" + uid);
		System.out.println("restful风格获取数据id:" + id);
		System.out.println("restful风格获取数据num:" + seq);
		System.out.println("restful风格的url的优点:轻量级,安全,效率高");
		return "springdata";
	}
}
