package com.yale.test.design.iterator.before;

import java.util.Iterator;

import com.yale.test.design.iterator.DinerMenuIterator;
import com.yale.test.design.iterator.Menu;

public class DinerMenu implements Menu{
	static final int MAX_ITEMS = 6;
	int numberOfItems = 0;
	MenuItem [] menuItems;
	
	public DinerMenu(){
		menuItems = new MenuItem[MAX_ITEMS];
		addItem("小炒肉","湘菜",true,10.0d);
		addItem("糖醋排骨","湘菜",true,10.0d);
		addItem("爆炒栗子","湘菜",true,10.0d);
		
		addItem("小炒肉","湘菜",true,10.0d);
		addItem("糖醋排骨","湘菜",true,10.0d);
		addItem("爆炒栗子","湘菜",true,10.0d);
	}
	
	public void addItem(String name,String description,boolean vegetarian,double price){
		MenuItem menuItem = new MenuItem(name,description,vegetarian,price);
		if(numberOfItems >= MAX_ITEMS){
			System.err.println("抱歉,不能再多添加菜单了。");
		} else {
			menuItems[numberOfItems] = menuItem;
			numberOfItems = numberOfItems + 1;
		}
	}
	
	/**
	 * 实现迭代器模式后,这个方法需要删掉掉
	 * 避免把菜单的维护细节暴露给调用方
	 * @return
	 */
	public MenuItem [] getMenuItems(){
		return menuItems;
	}
	
	/**
	 * 返回迭代器接口,客户不知道菜单是如何维护菜单项的,也不需要知道迭代器是如何实现的。
	 * 客户只需直接使用这个迭代器遍历菜单项即可。
	 * @return
	 */
	public Iterator createIterator(){
		return new DinerMenuIterator(menuItems);
	}
}
