package com.yale.test.springmvc.websocket.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Component
public class ChatHandshakeInterceptor extends HttpSessionHandshakeInterceptor {
	public static List<String> userList;
	
	static {
		userList = new ArrayList<>();
		userList.add(UserController.KEY_USER);
	}
	
	public ChatHandshakeInterceptor() {
		// 指定从HttpSession复制属性到WebSocketSession:
		super(userList);
		//如果是JDK1.9下面一行代码就搞定了,不需要上面的静态代码块
		//super(List.of(UserController.KEY_USER));
	}
}
