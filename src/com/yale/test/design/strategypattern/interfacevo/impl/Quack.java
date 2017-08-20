package com.yale.design.strategypattern.interfacevo.impl;

import com.yale.design.strategypattern.interfacevo.QuackBehavior;

public class Quack implements QuackBehavior {

	@Override
	public void quack() {
		System.out.println("Quack");
	}

}
