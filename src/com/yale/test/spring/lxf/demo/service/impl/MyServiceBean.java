package com.yale.test.spring.lxf.demo.service.impl;

import java.util.HashMap;
import java.util.Map;

import com.yale.test.spring.lxf.demo.service.ServiceBean;

public class MyServiceBean implements ServiceBean {
	private String dir;
	private Map map = new HashMap();
	
	public void setUserDir(String dir) {
		this.dir = dir;
		System.out.println("Set user dir to: " + dir);
	}
	
	@Override
	public void addUser(String username, String password) {
		if (!map.containsKey(username)) {
			map.put(username, password);
		} else {
			throw new RuntimeException("User already exist.");
		}
	}
	
	@Override
	public void deleteUser(String username) {
		if (map.remove(username) == null) {
			throw new RuntimeException("User not exist");
		}
	}
	
	@Override
	public boolean findUser(String username) {
		return map.containsKey(username);
	}
	
	@Override
	public String getPassword (String username) {
		return (String)map.get(username);
	}
}
