package com.yale.test.java.meiju.javaguide.singleton;

/*
 * 7. 通过枚举实现一些设计模式
 * 单例模式
 * https://github.com/Snailclimb/JavaGuide/blob/master/docs/java/basis/%E7%94%A8%E5%A5%BDJava%E4%B8%AD%E7%9A%84%E6%9E%9A%E4%B8%BE%E7%9C%9F%E7%9A%84%E6%B2%A1%E6%9C%89%E9%82%A3%E4%B9%88%E7%AE%80%E5%8D%95.md
 */
public enum PizzaDeliverySystemConfiguration {
	INSTANCE;
	PizzaDeliverySystemConfiguration() {
		
	}
	
	public static PizzaDeliverySystemConfiguration getInstance() {
		return INSTANCE;
	}
}
