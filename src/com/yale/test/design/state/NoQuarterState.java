package com.yale.test.design.state;


public class NoQuarterState implements State {
	GumballMachine gumballMachine;
	
	public NoQuarterState(GumballMachine gumballMachine){
		this.gumballMachine = gumballMachine;
	}

	@Override
	public void insertQuarter() {
		System.out.println("你投入了硬币...");
		gumballMachine.setState(gumballMachine.getHasQuarterState());
	}

	@Override
	public void ejectQuarter() {
		System.out.println("你还未投入硬币");
	}

	@Override
	public void turnCrank() {
		System.out.println("你还未投入硬币,不能点击购买按钮");
	}

	@Override
	public void dispense() {
		System.out.println("你还未投入硬币,不能拿到糖果");
	}

}
