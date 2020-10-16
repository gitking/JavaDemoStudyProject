package com.yale.test.lambda;

public class Human {
	String name;
	public Human(String name){
		this.name = name;
	}
	
	@Override
	public String toString() {
		return "Human:" + this.name;
	}
}
