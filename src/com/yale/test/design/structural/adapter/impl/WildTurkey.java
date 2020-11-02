package com.yale.test.design.structural.adapter.impl;

import com.yale.test.design.structural.adapter.Turkey;

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
