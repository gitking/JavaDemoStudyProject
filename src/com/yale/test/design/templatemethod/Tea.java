package com.yale.design.templatemethod;

public class Tea extends Templatemethod {

	@Override
	void brew() {
		System.out.println("加茶叶泡茶");
	}

	@Override
	void addCondiments() {
		System.out.println("泡茶不需要加调料");
	}
	
	public boolean hook(){
		return false;
	}
}
