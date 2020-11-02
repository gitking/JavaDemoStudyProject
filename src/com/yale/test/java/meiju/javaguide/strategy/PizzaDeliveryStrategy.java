package com.yale.test.java.meiju.javaguide.strategy;

import com.yale.test.java.meiju.javaguide.Pizza;

/*
 * 7. 通过枚举实现一些设计模式
 * 策略模式
 * 通常，策略模式由不同类实现同一个接口来实现的。
 * 这也就意味着添加新策略意味着添加新的实现类。使用枚举，可以轻松完成此任务，添加新的实现意味着只定义具有某个实现的另一个实例。
 * 下面的代码段显示了如何使用枚举实现策略模式：
 * https://github.com/Snailclimb/JavaGuide/blob/master/docs/java/basis/%E7%94%A8%E5%A5%BDJava%E4%B8%AD%E7%9A%84%E6%9E%9A%E4%B8%BE%E7%9C%9F%E7%9A%84%E6%B2%A1%E6%9C%89%E9%82%A3%E4%B9%88%E7%AE%80%E5%8D%95.md
 */
public enum PizzaDeliveryStrategy {
	EXPRESS{
		@Override
		public void deliver(Pizza pz) {
			System.out.println("Pizza will be delivered in express mode");
		}
	},
	NORMAL{
		@Override
		public void deliver(Pizza pz) {
			System.out.println("Pizza will be delivered in normal mode");
		}
	};
	
	public abstract void deliver(Pizza pz);
}
