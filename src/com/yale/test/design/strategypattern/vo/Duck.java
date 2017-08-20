package com.yale.design.strategypattern.vo;

import com.yale.design.strategypattern.interfacevo.FlyBehavior;
import com.yale.design.strategypattern.interfacevo.QuackBehavior;

public abstract class Duck {
	public Duck(){
	}
	
	public FlyBehavior flyBehavior;//父类只声明抽象的飞行动作,具体的实现交给子类自己去指定。而不是直接在这里写好具体的飞行动作
	public QuackBehavior quackBehavior;//父类只声明抽象的叫动作,具体的实现交给子类自己去指定。而不是直接在这里写好具体的叫动作
	/**
	 * 详细描述
	 */
	public abstract void display();
	
	public void performFly(){
		flyBehavior.fly();
	}
	
	public void performQuack(){
		quackBehavior.quack();
	}
	
	/**
	 * 游泳
	 */
	public void swim(){
		System.out.println("所有的鸭子都会游泳。");
	}

	public FlyBehavior getFlyBehavior() {
		return flyBehavior;
	}

	public void setFlyBehavior(FlyBehavior flyBehavior) {
		this.flyBehavior = flyBehavior;
	}

	public QuackBehavior getQuackBehavior() {
		return quackBehavior;
	}

	public void setQuackBehavior(QuackBehavior quackBehavior) {
		this.quackBehavior = quackBehavior;
	}
}
