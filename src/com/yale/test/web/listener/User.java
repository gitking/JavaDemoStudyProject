package com.yale.test.web.listener;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

/**
 * 感知监听(都与HttpSession相关)
 * 它用来添加到JavaBean上,而不是添加到三大域上
 * 这个俩个监听器都不需要在web.xml里面配置
 * @author dell
 *
 */
public class User implements HttpSessionBindingListener{
	private String userName;
	private String password;
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * HttpSessionBindingEvent继承HttpSessionEvent类
	 */
	@Override
	public void valueBound(HttpSessionBindingEvent event) {
		System.out.println("session添加了我,感知监听,");
	}

	@Override
	public void valueUnbound(HttpSessionBindingEvent event) {
		System.out.println("session删除我了,");
	}
}
