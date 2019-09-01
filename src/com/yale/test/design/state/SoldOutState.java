package com.yale.test.design.state;

public class SoldOutState implements State {

	GumballMachine gumballMachine;
	
	public SoldOutState(GumballMachine gumballMachine){
		this.gumballMachine = gumballMachine;
	}
	
	@Override
	public void insertQuarter() {
		System.out.println("糖果已售罄,不接收投币");
	}

	@Override
	public void ejectQuarter() {
		System.out.println("你未投币,不能退币。");
	}

	@Override
	public void turnCrank() {
		System.out.println("糖果已售罄,不能点击购买按钮...");
	}

	@Override
	public void dispense() {
		System.out.println("糖果已售罄...");
	}

}
