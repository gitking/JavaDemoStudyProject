package com.yale.test.design.cmoposite;

import java.util.Iterator;

/**
 * Waitress 负责打印菜单
 */
public class WaitressSec {
	MenuComponent allMenus;
	public WaitressSec(MenuComponent allMenus){
		this.allMenus = allMenus;
	}
	public void printMenu(){
		allMenus.print();
	}
	
	public void printVegetarianMenu(){
		Iterator iterator = allMenus.createIterator();
		System.out.println("----素食菜单-----");
		
		while(iterator.hasNext()){
			MenuComponent menuComponent = (MenuComponent)iterator.next();
			try {
				if(menuComponent.isVegetarian()){
					menuComponent.print();
				}
			} catch (Exception e) {
			}
		}
	}
}
