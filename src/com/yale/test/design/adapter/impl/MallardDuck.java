package com.yale.test.design.adapter.impl;

import com.yale.test.design.adapter.Duck;

public class MallardDuck implements Duck {

	@Override
	public void guack() {
		System.out.println("鸭子呱呱叫");
	}

	@Override
	public void fly() {
		System.out.println("鸭子的飞行");
	}

}
