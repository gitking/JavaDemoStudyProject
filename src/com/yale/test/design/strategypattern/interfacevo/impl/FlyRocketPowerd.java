package com.yale.design.strategypattern.interfacevo.impl;

import com.yale.design.strategypattern.interfacevo.FlyBehavior;

public class FlyRocketPowerd implements FlyBehavior {

	public void fly() {
		System.out.println("I'm flying with a rocket!");
	}
}
