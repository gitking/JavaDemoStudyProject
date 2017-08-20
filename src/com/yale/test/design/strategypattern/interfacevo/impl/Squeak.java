package com.yale.design.strategypattern.interfacevo.impl;

import com.yale.design.strategypattern.interfacevo.QuackBehavior;

public class Squeak implements QuackBehavior {

	public void quack() {
		System.out.println("Squeak");
	}
}
