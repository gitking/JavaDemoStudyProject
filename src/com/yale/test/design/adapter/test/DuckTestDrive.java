package com.yale.test.design.adapter.test;

import com.yale.test.design.adapter.Duck;
import com.yale.test.design.adapter.TurkeyAdapter;
import com.yale.test.design.adapter.impl.MallardDuck;
import com.yale.test.design.adapter.impl.WildTurkey;

public class DuckTestDrive {
	public static void main(String[] args) {
		MallardDuck duck = new MallardDuck();
		
		WildTurkey turkey = new WildTurkey();
		
		Duck turkeyAdapter = new TurkeyAdapter(turkey);
		
		System.out.println("The Turkey says.....");
		turkey.gobble();
		turkey.fly();
		
		System.out.println("The Duck says......");
		duck.guack();
		duck.fly();
		
		System.out.println("适配器says.....");
		turkeyAdapter.guack();
		turkeyAdapter.fly();
		
	}
}
