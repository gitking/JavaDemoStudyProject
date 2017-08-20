package com.yale.test.design.adapter.impl;

import com.yale.test.design.adapter.Turkey;

public class WildTurkey implements Turkey {

	@Override
	public void gobble() {
		System.out.println("火鸡的叫法");
	}

	@Override
	public void fly() {
		System.out.println("火鸡的飞行");
	}
}
