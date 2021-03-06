package com.yale.test.design.structural.bridge;

/*
 * 桥接
 * 将抽象部分与它的实现部分分离，使它们都可以独立地变化。桥接模式的定义非常玄乎，直接理解不太容易，所以我们还是举例子。
 * 假设某个汽车厂商生产三种品牌的汽车：Big、Tiny和Boss，每种品牌又可以选择燃油、纯电和混合动力。如果用传统的继承来表示各个最终车型，一共有3个抽象类加9个最终子类：
 * 					   ┌───────┐
	                   │  Car  │
	                   └───────┘
	                       ▲
	    ┌──────────────────┼───────────────────┐
	    │                  │                   │
	┌───────┐          ┌───────┐          ┌───────┐
	│BigCar │          │TinyCar│          │BossCar│
	└───────┘          └───────┘          └───────┘
	    ▲                  ▲                  ▲
	    │                  │                  │
	    │ ┌───────────────┐│ ┌───────────────┐│ ┌───────────────┐
	    ├─│  BigFuelCar   │├─│  TinyFuelCar  │├─│  BossFuelCar  │
	    │ └───────────────┘│ └───────────────┘│ └───────────────┘
	    │ ┌───────────────┐│ ┌───────────────┐│ ┌───────────────┐
	    ├─│BigElectricCar │├─│TinyElectricCar│├─│BossElectricCar│
	    │ └───────────────┘│ └───────────────┘│ └───────────────┘
	    │ ┌───────────────┐│ ┌───────────────┐│ ┌───────────────┐
	    └─│ BigHybridCar  │└─│ TinyHybridCar │└─│ BossHybridCar │
	      └───────────────┘  └───────────────┘  └───────────────┘
 * 如果要新增一个品牌，或者加一个新的引擎（比如核动力），那么子类的数量增长更快。
 * 所以，桥接模式就是为了避免直接继承带来的子类爆炸。我们来看看桥接模式如何解决上述问题。
 * 在桥接模式中，首先把Car按品牌进行子类化，但是，每个品牌选择什么发动机，不再使用子类扩充，而是通过一个抽象的“修正”类，以组合的形式引入。我们来看看具体的实现。
 * 首先定义抽象类Car，它引用一个Engine：
 * 紧接着，在一个“修正”的抽象类RefinedCar中定义一些额外操作：
 * 使用桥接模式的好处在于，如果要增加一种引擎，只需要针对Engine派生一个新的子类，如果要增加一个品牌，只需要针对RefinedCar派生一个子类，
 * 任何RefinedCar的子类都可以和任何一种Engine自由组合，即一辆汽车的两个维度：品牌和引擎都可以独立地变化
 */
public abstract class Car {
	protected Engine engine;//引用Engine
	
	public Car(Engine engine) {
		this.engine = engine;
	}
	
	public abstract void drive();
}
