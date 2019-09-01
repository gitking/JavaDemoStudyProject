package com.yale.test.design.templatemethod;

public class Coffee extends Templatemethod {

	@Override
	void brew() {
		System.out.println("加咖啡豆泡咖啡");
	}

	@Override
	void addCondiments() {
		System.out.println("加糖和牛奶");
	}

}
