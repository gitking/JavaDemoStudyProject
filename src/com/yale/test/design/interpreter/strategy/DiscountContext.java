package com.yale.test.design.interpreter.strategy;

import java.math.BigDecimal;

import com.yale.test.design.interpreter.strategy.impl.UserDiscountStrategy;

public class DiscountContext {
	private DiscountStrategy strategy = new UserDiscountStrategy();
	
	private EnumDiscountStrategy enumStrategy;
	
	public void setStrategyEnum(EnumDiscountStrategy enumDis) {
		this.enumStrategy = enumDis;
	}
	
	public void setStrategy(DiscountStrategy strategy) {
		this.strategy = strategy;
	}
	
	public BigDecimal calculatePrice(BigDecimal total) {
		return total.subtract(this.strategy.getDiscount(total)).setScale(2);
	}
}
