package com.yale.test.design.decorator;

/**
 * Decorator Pattern装饰者模式
 * Beverage饮料类
 */
public abstract class Beverage {
	String description = "未命名饮料";
	
	public String getDescription(){
		return description;
	}
	
	/**
	 * 计算价钱
	 * @return
	 */
	public abstract double cost();
}
