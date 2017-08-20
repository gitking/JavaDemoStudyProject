package com.yale.design.strategypattern.interfacevo.impl;

import com.yale.design.strategypattern.interfacevo.FlyBehavior;

public class FlyNoWay implements FlyBehavior {
	
	/**
	 * 具体的飞行动作
	 */
	public void fly() {
		System.out.println("我是不会飞的鸭子");
	}

}
