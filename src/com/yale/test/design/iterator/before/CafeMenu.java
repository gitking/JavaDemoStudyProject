package com.yale.test.design.iterator.before;

import java.util.Hashtable;
import java.util.Iterator;

import com.yale.test.design.iterator.DinerMenuIterator;
import com.yale.test.design.iterator.Menu;

public class CafeMenu implements Menu{
	Hashtable menuItems = new Hashtable();
	
	public CafeMenu(){
		addItem("小炒肉","湘菜",true,10.0d);
		addItem("糖醋排骨","湘菜",true,10.0d);
		addItem("爆炒栗子","湘菜",true,10.0d);
		
		addItem("小炒肉","湘菜",true,10.0d);
		addItem("糖醋排骨","湘菜",true,10.0d);
		addItem("爆炒栗子","湘菜",true,10.0d);
	}
	
	public void addItem(String name,String description,boolean vegetarian,double price){
		MenuItem menuItem = new MenuItem(name,description,vegetarian,price);
		menuItems.put(menuItem.getName(),menuItem);
	}
	
	/**
	 * 实现迭代器模式后,这个方法需要删掉掉
	 * 避免把菜单的维护细节暴露给调用方
	 * @return
	 */
	public Hashtable getMenuItems(){
		return menuItems;
	}
	
	/**
	 * 返回迭代器接口,客户不知道菜单是如何维护菜单项的,也不需要知道迭代器是如何实现的。
	 * 客户只需直接使用这个迭代器遍历菜单项即可。
	 * @return
	 */
	public Iterator createIterator(){
		return menuItems.values().iterator();
	}
}
