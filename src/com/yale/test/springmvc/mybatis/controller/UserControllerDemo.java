package com.yale.test.springmvc.mybatis.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yale.test.springmvc.mybatis.vo.User;

@Controller
@RequestMapping("/mybatis")//窄化--分模块开发,团队协作--注意页面跳转
public class UserControllerDemo {
	private List<User> list = new ArrayList<User>();
	
	public UserControllerDemo(){
		list.add(new User(1, "张三", "1111"));
		list.add(new User(2, "李四", "2222"));
		list.add(new User(3, "王五", "3333"));
	}
	
	@RequestMapping("/list")
	public String list(ModelMap map){
		map.addAttribute("list", list);
		/*
		 * --注意页面跳转,现在跳转的是"/mybatis/userList.jsp"这个页面,因为你UserControllerDemo类上面
		 * 的@RequestMapping指定的是mybatis模块
		 */
		return "userList.jsp";
		//return "/mybatis/userList.jsp";跟上面的写法是等价的
	}
	
	@RequestMapping("/addUser")
	public String add(User user){
		user.setId(list.get(list.size()-1).getId() + 1);
		list.add(user);
		return "list.springmvc";
	}
	
	@RequestMapping("/delUser")
	public String delUser(int id){
		for(int i=0;i<list.size();i++) {
			if (list.get(i).getId() == id) {
				list.remove(i);
				break;
			}
		}
		return "list.springmvc";//跳转到这个请求上面去等于跳转到list这个方法里面去了
	}
	
	@RequestMapping("/delUserRes/{id}")//这个就是Restful风格
	public String delUserRes(@PathVariable int id){//这个就是Restful风格
		for(int i=0;i<list.size();i++) {
			if (list.get(i).getId() == id) {
				list.remove(i);
				break;
			}
		}
		return "list.springmvc";
	}
}
