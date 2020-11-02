package com.yale.test.design.graphic.factory;

import java.math.BigDecimal;

public class NumberFactoryImpl implements NumberFactory{
	@Override
	public Number parse(String s) {
		return new BigDecimal(s);//而产品接口是Number，NumberFactoryImpl返回的实际产品是BigDecimal。
	}
}
