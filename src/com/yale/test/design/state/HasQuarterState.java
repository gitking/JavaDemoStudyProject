package com.yale.design.state;

import java.util.Random;

public class HasQuarterState implements State {

	GumballMachine gumballMachine;
	
	Random randomWinner = new Random(System.currentTimeMillis());
	
	public HasQuarterState(GumballMachine gumballMachine){
		this.gumballMachine = gumballMachine;
	}
	
	@Override
	public void insertQuarter() {
		System.out.println("已经投过硬币了,不能再次投入");
	}

	@Override
	public void ejectQuarter() {
		this.gumballMachine.setState(this.gumballMachine.getNoQuarterState());
		System.out.println("退出硬币...");
	}

	@Override
	public void turnCrank() {
		int winnder = randomWinner.nextInt(10);
		if((winnder == 0) && (this.gumballMachine.getCount() >1)){
			this.gumballMachine.setState(this.gumballMachine.getWinnerState());
		} else {
			this.gumballMachine.setState(this.gumballMachine.getSoldState());
		}
		System.out.println("点击购买按钮...");
	}

	@Override
	public void dispense() {
		System.out.println("还没点击购买按钮...");
	}
}
