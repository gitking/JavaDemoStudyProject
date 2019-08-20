package com.yale.test.design.iterator;

import java.util.Iterator;

import com.yale.test.design.iterator.before.CafeMenu;
import com.yale.test.design.iterator.before.DinerMenu;
import com.yale.test.design.iterator.before.MenuItem;
import com.yale.test.design.iterator.before.PancakeHouseMenu;

public class Test {
	
	Menu pancakeHouseMenu;
	Menu dinerMenu;
	Menu cafeMenu;
	
	public Test(PancakeHouseMenu pancakeHouseMenu,DinerMenu dinerMenu,CafeMenu cafeMenu){
		this.pancakeHouseMenu = pancakeHouseMenu;
		this.dinerMenu = dinerMenu;
		this.cafeMenu = cafeMenu;
	}

	public static void main(String[] args) {
		PancakeHouseMenu pancakeHouseMenu = new PancakeHouseMenu();
		DinerMenu dinerMenu = new DinerMenu();
		CafeMenu cafeMenu = new CafeMenu();
		
		Test test = new Test(pancakeHouseMenu,dinerMenu,cafeMenu);
		test.printMenu();
	}

	public void printMenu(){
		Iterator pancakeHouseMenu = this.pancakeHouseMenu.createIterator();
		Iterator dinerIterator = this.dinerMenu.createIterator();
		Iterator cafeIterator = this.cafeMenu.createIterator();
		System.out.println("菜单----湘菜\n");
		printMenu(pancakeHouseMenu);
		System.out.println("菜单----湘菜\n");
		printMenu(dinerIterator);
		System.out.println("菜单----湘菜\n");
		printMenu(cafeIterator);
	}
	
	private void printMenu(Iterator iterator){
		while(iterator.hasNext()){
			MenuItem menuItem = (MenuItem)iterator.next();
			System.out.println("菜名:" + menuItem.getName() + ",菜系:" + menuItem.getDescription() + ",价钱:" + menuItem.getPrice());
		}
	}
}
