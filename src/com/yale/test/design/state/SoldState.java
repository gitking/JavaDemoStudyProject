package com.yale.test.design.state;

public class SoldState implements State {

	GumballMachine gumballMachine;
	
	public SoldState(GumballMachine gumballMachine){
		this.gumballMachine = gumballMachine;
	}
	
	@Override
	public void insertQuarter() {
		System.out.println("正在出售糖果,不能再次投币");
	}

	@Override
	public void ejectQuarter() {
		System.out.println("正在出售糖果,不能进行退币");
	}

	@Override
	public void turnCrank() {
		System.out.println("正在出售糖果,不能再次点击购买按钮");
	}

	@Override
	public void dispense() {
		this.gumballMachine.releasBall();
		if(this.gumballMachine.getCount() > 0){
			this.gumballMachine.setState(this.gumballMachine.getNoQuarterState());
		} else {
			this.gumballMachine.setState(this.gumballMachine.getSoldOutState());
		}
		
	}

}
