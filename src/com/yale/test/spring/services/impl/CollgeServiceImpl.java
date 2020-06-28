package com.yale.test.spring.services.impl;

import com.yale.test.spring.services.StuServices;

public class CollgeServiceImpl implements StuServices {

	@Override
	public void add() {
		System.out.println("CollgeServiceImpl添加学生,SpringAOP切入进来了----------------只是我拿不到目标类的具体信息");
	}

	@Override
	public void update() {
		System.out.println("CollgeServiceImpl修改学生信息,SpringAOP切入进来了----------------只是我拿不到目标类的具体信息");
	}

	@Override
	public void delete(int id) {
		System.out.println("删除学生信息,SpringAOP切入进来了----------------");
	}

	@Override
	public void serarch() {
		System.out.println("查询学生,SpringAOP切入进来了----------------");
	}

}
