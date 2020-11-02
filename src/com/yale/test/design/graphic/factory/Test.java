package com.yale.test.design.graphic.factory;

public class Test {
	public static void main(String[] args) {
		/*
		 * 在客户端中，我们只需要和工厂接口NumberFactory以及抽象产品Number打交道：
		 * 调用方可以完全忽略真正的工厂NumberFactoryImpl和实际的产品BigDecimal，这样做的好处是允许创建产品的代码独立地变换，而不会影响到调用方。
		 */
		NumberFactory nf = NumberFactory.getFactory();
		Number num = nf.parse("1000.01");
	}
}
