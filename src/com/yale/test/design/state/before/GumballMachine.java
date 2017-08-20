package com.yale.design.state.before;

import com.yale.design.state.State;


public class GumballMachine {
	final static int SOLD_OUT = 0;//糖果售罄
	final static int NO_QUARTER = 1;//没有投入25美分
	final static int HAS_QUARTER = 2;//投入了25美分
	final static int SOLD = 3;//出售糖果
	
	int state = SOLD_OUT;
	int count = 0;
	
	public GumballMachine(int count){
		this.count = count;
		if(count > 0){
			state = NO_QUARTER;
		} else {
			state = SOLD_OUT;
		}
	}
	
	/**
	 * 当投入硬币时,执行此方法
	 */
	public void insertQuarter(){
		if(state == HAS_QUARTER){
			System.out.println("你已经投过硬币了...");
		} else if(state == NO_QUARTER){
			state = HAS_QUARTER;
			System.out.println("硬币已投入...");
		} else if(state == SOLD_OUT){
			System.out.println("不能投入硬币,糖果已售罄,硬币退回中....");
		} else if(state == SOLD){
			System.out.println("稍等,正在出售糖果");
		}
	}
	/**
	 * 退回硬币时执行此方法
	 */
	public void ejectQuarter(){
		if(state == HAS_QUARTER){
			state = NO_QUARTER;
			System.out.println("硬币退回中...");
		} else if(state == NO_QUARTER){
			System.out.println("你还未投入硬币");
		} else if(state == SOLD_OUT){
			System.out.println("你还未投入硬币,但是已经没有糖果了");
		} else if(state == SOLD){
			System.out.println("不能退回硬币,正在出售糖果...");
		}
	}
	
	/**
	 * 出售糖果时执行此方法
	 */
	public void turnCrank(){
		if(state == HAS_QUARTER){
			state = SOLD;
			dispense();
			System.out.println("请稍等正在出售糖果");
		} else if(state == NO_QUARTER){
			System.out.println("你还未投入硬币");
		} else if(state == SOLD_OUT){
			System.out.println("已经没有糖果了");
		} else if(state == SOLD){
			System.out.println("已经正在出售糖果...");
		}
	}
	
	/**
	 * 点机购买按钮时执行这里
	 */
	public void dispense(){
		if(state == HAS_QUARTER){
			System.out.println("你还未转到点击购买按钮");
		} else if(state == NO_QUARTER){
			System.out.println("你还未投入硬币");
		} else if(state == SOLD_OUT){
			System.out.println("你还未投入硬币,但是已经没有糖果了");
		} else if(state == SOLD){
			System.out.println("一颗糖果出来了");
			count = count - 1;
			if(count == 0){
				System.out.println("糖果卖完了");
				state = SOLD_OUT;
			} else {
				state = NO_QUARTER;//
			}
		}
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
}
