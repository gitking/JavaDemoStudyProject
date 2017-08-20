package com.yale.design.iterator;

import java.util.ArrayList;
import java.util.Iterator;

import com.yale.design.iterator.before.CafeMenu;
import com.yale.design.iterator.before.DinerMenu;
import com.yale.design.iterator.before.MenuItem;
import com.yale.design.iterator.before.PancakeHouseMenu;

public class TestSec {
	
	ArrayList menuList;
	
	public TestSec(ArrayList menuList){
		this.menuList = menuList;
	}

	public static void main(String[] args) {
		PancakeHouseMenu pancakeHouseMenu = new PancakeHouseMenu();
		DinerMenu dinerMenu = new DinerMenu();
		CafeMenu cafeMenu = new CafeMenu();
		ArrayList menuSecList = new ArrayList();
		menuSecList.add(pancakeHouseMenu);
		menuSecList.add(dinerMenu);
		menuSecList.add(cafeMenu);
		TestSec test = new TestSec(menuSecList);
		test.printMenu();
	}

	public void printMenu(){
		Iterator menuIterator = menuList.iterator();
		while(menuIterator.hasNext()){
			Menu menu = (Menu)menuIterator.next();
			Iterator menuSecIterator = menu.createIterator();
			printMenu(menuSecIterator);
		}
	}
	
	private void printMenu(Iterator iterator){
		while(iterator.hasNext()){
			MenuItem menuItem = (MenuItem)iterator.next();
			System.out.println("菜名:" + menuItem.getName() + ",菜系:" + menuItem.getDescription() + ",价钱:" + menuItem.getPrice());
		}
	}
}
