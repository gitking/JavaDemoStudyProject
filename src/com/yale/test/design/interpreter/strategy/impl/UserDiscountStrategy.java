package com.yale.test.design.interpreter.strategy.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.yale.test.design.interpreter.strategy.DiscountStrategy;

public class UserDiscountStrategy implements DiscountStrategy {
	@Override
	public BigDecimal getDiscount(BigDecimal total) {
		//普通会员打九折:
		return total.multiply(new BigDecimal("0.1").setScale(2, RoundingMode.DOWN));
	}
}
