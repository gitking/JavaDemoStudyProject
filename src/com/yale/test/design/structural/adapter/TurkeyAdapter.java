package com.yale.test.design.structural.adapter;

/**
 * 鸭子适配器
 * 将鸭子的飞行和叫唤 换成 火鸡的飞行好叫唤
 * 适配器其实跟装饰者模式有点类似
 */
public class TurkeyAdapter implements Duck {
	private Turkey turkey;//火鸡接口
	
	public TurkeyAdapter(Turkey turkey){
		this.turkey = turkey;
	}
	
	@Override
	public void guack() {
		this.turkey.gobble();
	}

	@Override
	public void fly() {
		this.turkey.fly();
	}

}
