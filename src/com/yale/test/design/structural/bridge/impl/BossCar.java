package com.yale.test.design.structural.bridge.impl;

import com.yale.test.design.structural.bridge.Engine;
import com.yale.test.design.structural.bridge.RefinedCar;

public class BossCar extends RefinedCar {
	public BossCar(Engine engine) {
		super(engine);
	}
	
	public String getBrand() {
		return "Boss";
	}
}
