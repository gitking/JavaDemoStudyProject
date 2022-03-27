package com.yale.test.spring.lxf.demo.service;

public interface ServiceBean {
	void addUser(String username, String password);
	void deleteUser(String username);
	boolean findUser(String username);
	String getPassword(String username);
}
