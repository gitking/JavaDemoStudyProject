package com.yale.test.design.structural.bridge.impl;

import com.yale.test.design.structural.bridge.Engine;

public class ElectricEngine implements Engine {
	@Override
	public void start() {
		System.out.println("Start Electric Engine...");
	}
}
