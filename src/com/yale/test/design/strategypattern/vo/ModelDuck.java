package com.yale.test.design.strategypattern.vo;

import com.yale.test.design.strategypattern.interfacevo.impl.FlyNoWay;
import com.yale.test.design.strategypattern.interfacevo.impl.Quack;

public class ModelDuck extends Duck {
	
	public ModelDuck(){
		flyBehavior = new FlyNoWay();
		quackBehavior = new Quack();
	}

	@Override
	public void display() {
		System.out.println("我可以做火箭飞行");
	}

}
