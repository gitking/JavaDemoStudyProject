package com.yale.design.decorator;

public class TestDecoratorPattern {
	
	/**
	 * 测试装饰者模式
	 * @param args
	 */
	public static void main(String[] args) {
		 Beverage beverage = new Espresso();
		 System.out.println(beverage.getDescription() + ",$" + beverage.cost());
		 
		 Beverage beverageThird = new DarkRoast();
		 beverageThird = new Mocha(beverageThird);
		 beverageThird = new Mocha(beverageThird);
		 beverageThird = new Whip(beverageThird);
		 System.out.println(beverageThird.getDescription() + ",$" + beverageThird.cost());

		 /**
		  * 装饰者模式
		  */
		 Beverage beverageSec = new HouseBlend();
		 beverageSec = new Soy(beverageSec);
		 beverageSec = new Mocha(beverageSec);
		 beverageSec = new Whip(beverageSec);
		 System.out.println(beverageSec.getDescription() + ",$" + beverageSec.cost());
	}
}
