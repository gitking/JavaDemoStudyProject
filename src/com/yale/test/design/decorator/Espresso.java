package com.yale.test.design.decorator;

public class Espresso extends Beverage {

	public Espresso(){
		description = "Espresso";//饮料名称
	}
	@Override
	public double cost() {
		return 1.99;
	}

}
