package com.yale.test.design.strategypattern.interfacevo.impl;

import com.yale.test.design.strategypattern.interfacevo.FlyBehavior;

public class FlyWithWings implements FlyBehavior {
	/**
	 * 具体的飞行动作
	 */
	public void fly() {
		System.out.println("我是会飞的鸭子");
	}
}
