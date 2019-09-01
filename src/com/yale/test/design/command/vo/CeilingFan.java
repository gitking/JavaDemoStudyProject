package com.yale.test.design.command.vo;

public class CeilingFan {
	public static final int HIGH = 3;
	public static final int MEDIUM = 2;
	public static final int LOW = 1;
	public static final int OFF = 0;
	
	String desc;
	int speed;
	
	public CeilingFan(String desc){
		this.desc = desc;
		this.speed = OFF;
	}
	
	public void high(){
		speed = HIGH;
		System.out.println(this.desc + ",高速运转" );
	}
	
	public void medium(){
		speed = MEDIUM;
		System.out.println(this.desc + ",中速运转" );
	}
	
	public void low(){
		speed = LOW;
		System.out.println(this.desc + ",低速运转" );
	}
	
	public void off(){
		speed = OFF;
		System.out.println(this.desc + ",高速运转" );
	}
	
	public int getSpeed(){
		return speed;
	}
}
