package com.yale.test.design.structural.bridge;

import com.yale.test.design.structural.bridge.impl.BossCar;
import com.yale.test.design.structural.bridge.impl.ElectricEngine;
import com.yale.test.design.structural.bridge.impl.HybridEngine;
import com.yale.test.design.structural.bridge.impl.TinyCar;

public class Test {
	public static void main(String[] args) {
		/*
		 * 使用桥接模式的好处在于，如果要增加一种引擎，只需要针对Engine派生一个新的子类，如果要增加一个品牌，只需要针对RefinedCar派生一个子类，
		 * 任何RefinedCar的子类都可以和任何一种Engine自由组合，即一辆汽车的两个维度：品牌和引擎都可以独立地变化
		 * ┌───────────┐
	       │    Car    │
	       └───────────┘
	             ▲
	             │
	       ┌───────────┐       ┌─────────┐
	       │RefinedCar │ ─ ─ ─>│ Engine  │
	       └───────────┘       └─────────┘
	             ▲                  ▲
	    ┌────────┼────────┐         │ ┌──────────────┐
	    │        │        │         ├─│  FuelEngine  │
	┌───────┐┌───────┐┌───────┐     │ └──────────────┘
	│BigCar ││TinyCar││BossCar│     │ ┌──────────────┐
	└───────┘└───────┘└───────┘     ├─│ElectricEngine│
	                                │ └──────────────┘
	                                │ ┌──────────────┐
	                                └─│ HybridEngine │
                                      └──────────────┘
         * 桥接模式实现比较复杂，实际应用也非常少，但它提供的设计思想值得借鉴，即不要过度使用继承，而是优先拆分某些部件，使用组合的方式来扩展功能。
         * 小结
         * 桥接模式通过分离一个抽象接口和它的实现部分，使得设计可以按两个维度独立扩展。
         * https://www.liaoxuefeng.com/wiki/1252599548343744/1281319266943009
		 */
		RefinedCar car = new BossCar(new HybridEngine());
		car.drive();
		
		RefinedCar car2 = new TinyCar(new ElectricEngine());
		car2.drive();
	}
}
