package com.yale.test.spring.services.impl;

import com.yale.test.spring.dao.UserDao;
import com.yale.test.spring.dao.impl.UserDaoTypeImpl;
import com.yale.test.spring.services.UserService;

public class SpecUserServiceImpl implements UserService{
	private UserDaoTypeImpl byType;//演示Spring的byType根据类型自动注入(自动装配).自动装配主要为了简化Spring的配置文件, 不推荐使用自动装配,而是使用Annoation代替,
	
	public SpecUserServiceImpl() {
		
	}
	
	public SpecUserServiceImpl(UserDaoTypeImpl byType) {
		System.out.println("（（（（（（（（（（（（（（注意观察我SpecUserServiceImpl的有参构造方法在什么时候被调用了.............");
		System.out.println("（（（（（（（（（（（（（（我在Spring加载配置文件的时候就已经被调用了");
		this.byType = byType;
	}
	
	
	public UserDaoTypeImpl getByType() {
		return byType;
	}

	public void setByType(UserDaoTypeImpl byType) {
		this.byType = byType;
	}

	@Override
	public void getUser() {
		System.out.println("我在使用spring的autowire=byName 自动注入特性...........");
		this.byType.getUser();
	}
}
