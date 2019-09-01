package com.yale.test.design.strategypattern.interfacevo.impl;

import com.yale.test.design.strategypattern.interfacevo.QuackBehavior;

public class MuteQuack implements QuackBehavior {

	@Override
	public void quack() {
		System.out.println("<<Silence>>");
	}

}
