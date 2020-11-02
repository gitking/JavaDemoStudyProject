package com.yale.test.design.interpreter.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;

/*
 * https://www.liaoxuefeng.com/wiki/1252599548343744/1281319606681634#0
 * https://github.com/Snailclimb/JavaGuide/blob/master/docs/java/basis/%E7%94%A8%E5%A5%BDJava%E4%B8%AD%E7%9A%84%E6%9E%9A%E4%B8%BE%E7%9C%9F%E7%9A%84%E6%B2%A1%E6%9C%89%E9%82%A3%E4%B9%88%E7%AE%80%E5%8D%95.md
 * https://www.baeldung.com/jackson-serialize-enums
 * 这篇文章由 JavaGuide 翻译，公众号: JavaGuide,原文地址：https://www.baeldung.com/a-guide-to-java-enums 。
 */
public enum EnumDiscountStrategy {
	

	UserDiscountStrategy{
		@Override
		BigDecimal getDiscount(BigDecimal total) {
			//普通会员打九折
			return total.multiply(new BigDecimal("0.1").setScale(2, RoundingMode.DOWN));
		}
	},
	OverDiscountStrategy {
		@Override
		BigDecimal getDiscount(BigDecimal total) {
			//满100减20优惠
			return total.compareTo(BigDecimal.valueOf(100)) >=0 ? BigDecimal.valueOf(20) : BigDecimal.ZERO;
		}
	},
	PrimeDiscountStrategy{
		@Override
		BigDecimal getDiscount(BigDecimal total) {//Prime会员打七折
			return total.multiply(new BigDecimal("0.3")).setScale(2, RoundingMode.DOWN);
		}
	},
	OverPrimeDiscountStrategy{
		@Override
		BigDecimal getDiscount(BigDecimal total) {
			//在满100减20的基础上对Prime会员再打七折
			BigDecimal count = BigDecimal.valueOf(0);
			count = count.add(OverDiscountStrategy.getDiscount(total));
			count = count.add(PrimeDiscountStrategy.getDiscount(total.subtract(count)));
			return count;
		}
	};
	
	//这行代码必须放在最后
	abstract BigDecimal getDiscount(BigDecimal total);

}
