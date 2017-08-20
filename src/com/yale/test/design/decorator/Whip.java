package com.yale.design.decorator;


public class Whip extends CondimentDecorator {
	Beverage beverage;
	public Whip(Beverage beverage){
		this.beverage = beverage;
	}
	
	public String getDescription(){
		return this.beverage.getDescription() + ", Whip";
	}

	public double cost(){
		return .99 + this.beverage.cost();
	}
}
