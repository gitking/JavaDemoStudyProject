package com.yale.design.strategypattern.vo;

import com.yale.design.strategypattern.interfacevo.impl.FlyWithWings;
import com.yale.design.strategypattern.interfacevo.impl.Quack;

/**
 * MallardDuck 是 Duck的子类
 * @author lenovo
 */
public class MallardDuck extends Duck {

	public MallardDuck(){
		quackBehavior = new Quack();
		flyBehavior = new FlyWithWings();
	}
	
	public void display() {
		System.out.println("I'M a real Mallard duck");
	}
}
