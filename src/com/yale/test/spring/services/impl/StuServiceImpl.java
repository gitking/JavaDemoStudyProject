package com.yale.test.spring.services.impl;

import com.yale.test.spring.services.StuServices;

public class StuServiceImpl implements StuServices {

	@Override
	public void add() {
		System.out.println("添加学生,SpringAOP切入进来了----------------");
	}

	@Override
	public void update() {
		System.out.println("修改学生信息,SpringAOP切入进来了----------------");
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
