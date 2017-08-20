package com.yale.design.iterator.before;

import java.util.ArrayList;

public class Test {
	public static void main(String[] args) {
		/**
		 * 要想打印不同的菜单需要俩个循环来遍历这些菜单,如果后续又增加一个菜单,可能又多一种循环方式
		 */
		PancakeHouseMenu pancakeHouseMenu = new PancakeHouseMenu();
		ArrayList menuList = pancakeHouseMenu.getMenuItems();
		
		for(int i=0; i <menuList.size(); i++){
			MenuItem menuItem = (MenuItem)menuList.get(i);
			System.out.println("菜名:" + menuItem.getName() + ",菜系:" + menuItem.getDescription() + ",价钱:" + menuItem.getPrice());
		}
		DinerMenu dinerMenu = new DinerMenu();
		MenuItem [] menuArr = dinerMenu.getMenuItems();
		
		for(int i=0; i < menuArr.length; i++){
			MenuItem menuItem = menuArr[i];
			System.out.println("菜名:" + menuItem.getName() + ",菜系:" + menuItem.getDescription() + ",价钱:" + menuItem.getPrice());
		}
	}
}
