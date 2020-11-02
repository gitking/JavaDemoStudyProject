package com.yale.test.design.structural.bridge;

/*
 * 这样一来，最终的不同品牌继承自RefinedCar，例如BossCar：
 * 注意这个类是修正类,如果觉得后续需要扩展什么功能,就可以避免子类爆炸
 */
public abstract class RefinedCar extends Car{
	
	public RefinedCar(Engine engine) {
		super(engine);
	}
	
	@Override
	public void drive() {
		this.engine.start();
		System.out.println("Drive:" + getBrand() +  " car...");
	}
	
	public abstract String getBrand();
}
