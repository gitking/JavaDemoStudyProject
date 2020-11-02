package com.yale.test.design.structural.bridge.impl;

import com.yale.test.design.structural.bridge.Engine;
import com.yale.test.design.structural.bridge.RefinedCar;

public class BigCar extends RefinedCar {
	public BigCar(Engine engine) {
		super(engine);
	}
	
	@Override
	public String getBrand() {
		return "Big";
	}
}
