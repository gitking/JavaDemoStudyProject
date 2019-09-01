package com.yale.test.design.strategypattern.test;

import com.yale.test.design.strategypattern.interfacevo.impl.FlyRocketPowerd;
import com.yale.test.design.strategypattern.vo.Duck;
import com.yale.test.design.strategypattern.vo.MallardDuck;
import com.yale.test.design.strategypattern.vo.ModelDuck;

/**
 * 策略模式
 * 设计原则:多用组合,少用继承。"有一个" 比 "是一个" 更好
 * 策略模式定义了算法族,分别封装起来,让它们之间可以相互替换。
 * 此模式让算法的变化独立于使用算法的客户。
 * @author lenovo
 */
public class DuckTest {
	public static void main(String[] args) {
		Duck mallard = new MallardDuck();
		mallard.performFly();
		mallard.performQuack();
		
		Duck modelDuck = new ModelDuck();
		modelDuck.performFly();
		modelDuck.performQuack();
		/**
		 * 我们可以动态的改变鸭子的飞行行为,如果只是继承父类并重写父类的方法就没办法动态改变飞行行为了。
		 * 设计原则:多用组合,少用继承。"有一个" 比 "是一个" 更好
		 */
		modelDuck.setFlyBehavior(new FlyRocketPowerd());
		modelDuck.performFly();
	}
}
