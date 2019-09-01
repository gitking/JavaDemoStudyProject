package com.yale.test.design.templatemethod;

/**
 * 模版方法模式:在一个方法中定义个算法的骨架,而将一些步骤延迟的子类中。
 * 模版方法使得子类可以在不改变算法结构的情况下,重新定义算法中的某些步骤。
 * @author lenovo
 *
 */
public abstract class Templatemethod {
	/**
	 * 模版方法,不能被子类覆盖或重写
	 */
	final void prepareRecipe(){
		boilWater();
		brew();
		pourInCup();
		if(hook()){//由子类决定要不要调用addCondiments()方法
			addCondiments();
		}
	}
	
	abstract void brew();
	
	abstract void addCondiments();
	
	void boilWater(){
		System.out.println("把水烧开");
	}
	
	void pourInCup(){
		System.out.println("倒进杯子");
	}
	
	/**
	 * 钩子方法
	 * 子类可以视情况决定要不要覆盖他们
	 * 别调用我们,我们会调用你
	 * @return
	 */
	boolean hook(){
		return true;
	}
}
