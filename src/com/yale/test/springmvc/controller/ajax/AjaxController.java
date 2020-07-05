package com.yale.test.springmvc.controller.ajax;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AjaxController {
	@RequestMapping("/ajax")
	public void ajax(String name, HttpServletResponse hsp) throws IOException {
		/*
		 * http://localhost:8080/pcis/ajax.springmvc
		 * 直接在浏览器上请求可以,但是用ajax发get请求 会报错
		 * No mapping for GET /pcis/springmvc/ajax.springmvc
		 * 是因为ajax$.post发送的请求为http://localhost:8080/pcis/springmvc/ajax.springmvc
		 * 中间多了个springmvc/ajax.springmvc
		 * 修改ajax的请求为$.post("../ajax.springmvc")就行
		 */
		if ("siggy".equals(name)) {
			hsp.getWriter().print("true");
		} else {
			hsp.getWriter().print("false");
		}
	}
}
