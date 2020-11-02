package com.yale.test.design.interpreter.observer;

/*
 * 观察者模式也有很多变体形式。有的观察者模式把被观察者也抽象出接口：
 * 对应的实体被观察者就要实现该接口：
 * public class Store implements ProductObservable {
	    ...
	}
 * 
 */
public interface ProductObservable {//注意此处拼写是Observable不是Observer
	void addObserver(ProductObserver observer);
}
