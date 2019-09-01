package com.yale.test.design.strategypattern.interfacevo.impl;

import com.yale.test.design.strategypattern.interfacevo.QuackBehavior;

public class Squeak implements QuackBehavior {

	public void quack() {
		System.out.println("Squeak");
	}
}
