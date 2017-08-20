package com.yale.design.decorator;

/**
 * Decorator Pattern装饰者模式
 * CondimentDecorator 调料类
 */
public abstract class CondimentDecorator extends Beverage {
	
	public abstract String getDescription();
}
