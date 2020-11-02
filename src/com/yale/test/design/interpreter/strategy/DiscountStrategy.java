package com.yale.test.design.interpreter.strategy;

import java.math.BigDecimal;

public interface DiscountStrategy {
	BigDecimal getDiscount(BigDecimal total);//计算折扣额度
}
