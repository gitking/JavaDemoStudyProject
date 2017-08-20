package com.yale.design.iterator.before;

import java.util.ArrayList;
import java.util.Iterator;

import com.yale.design.iterator.Menu;

public class PancakeHouseMenu implements Menu{
	ArrayList menuItems;
	
	public PancakeHouseMenu(){
		menuItems = new ArrayList();
		addItem("小炒肉","湘菜",true,10.0d);
		addItem("糖醋排骨","湘菜",true,10.0d);
		addItem("爆炒栗子","湘菜",true,10.0d);
	}
	
	public void addItem(String name,String description,boolean vegetarian,double price){
		MenuItem menuItem = new MenuItem(name,description,vegetarian,price);
		menuItems.add(menuItem);
	}
	
	public ArrayList getMenuItems(){
		return menuItems;
	}
	
	public Iterator createIterator(){
		return menuItems.iterator();
	}
}
