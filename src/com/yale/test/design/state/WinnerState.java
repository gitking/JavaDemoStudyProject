package com.yale.test.design.state;

public class WinnerState implements State {
	public GumballMachine gumballMachine;
	
	public WinnerState(GumballMachine gumballMachine){
		this.gumballMachine = gumballMachine;
	}
	
	@Override
	public void insertQuarter() {
		System.err.println("不能投币");
	}

	@Override
	public void ejectQuarter() {
		System.err.println("不能退币");
	}

	@Override
	public void turnCrank() {
		System.err.println("不能点击购买按钮");
	}

	@Override
	public void dispense() {
		System.out.println("你赢了,这次将出来俩个糖果");
		this.gumballMachine.releasBall();
		if(this.gumballMachine.getCount() == 0){
			System.out.println("糖果卖完了");
			this.gumballMachine.setState(this.gumballMachine.getSoldOutState());
		} else {
			this.gumballMachine.releasBall();
			if(this.gumballMachine.getCount() > 0){
				this.gumballMachine.setState(this.gumballMachine.getNoQuarterState());
			} else {
				System.out.println("糖果卖完了");
				this.gumballMachine.setState(this.gumballMachine.getSoldOutState());
			}
		}
	}

}
