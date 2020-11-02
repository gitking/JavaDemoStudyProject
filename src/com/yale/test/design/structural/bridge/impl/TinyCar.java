package com.yale.test.design.structural.bridge.impl;

import com.yale.test.design.structural.bridge.Engine;
import com.yale.test.design.structural.bridge.RefinedCar;

public class TinyCar extends RefinedCar {
	public TinyCar(Engine engine) {
		super(engine);
	}
	
	@Override
	public String getBrand() {
		return "tiny";
	}
}
