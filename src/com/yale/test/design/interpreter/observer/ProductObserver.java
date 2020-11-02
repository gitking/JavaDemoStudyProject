package com.yale.test.design.interpreter.observer;

/*
 * 有些观察者模式把通知变成一个Event对象，从而不再有多种方法通知，而是统一成一种：
 * public interface ProductObserver {
	    void onEvent(ProductEvent event);
	}
 * 让观察者自己从Event对象中读取通知类型和通知数据。
 */
public interface ProductObserver {
	void onPublished(Product product);
	void onPriceChanged(Product product);
}
