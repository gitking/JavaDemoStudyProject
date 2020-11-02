package com.yale.test.design.interpreter.strategy.impl;

import java.math.BigDecimal;

import com.yale.test.design.interpreter.strategy.DiscountStrategy;

public class OverDiscountStrategy implements DiscountStrategy {

	@Override
	public BigDecimal getDiscount(BigDecimal total) {
		//满100减20优惠
		return total.compareTo(BigDecimal.valueOf(100)) >=0 ? BigDecimal.valueOf(20) : BigDecimal.ZERO;
	}
	
}
