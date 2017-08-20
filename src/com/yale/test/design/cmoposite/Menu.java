package com.yale.test.design.cmoposite;

import java.util.ArrayList;
import java.util.Iterator;

public class Menu extends MenuComponent {
	ArrayList menuComponents = new ArrayList();
	String name;
	String description;
	
	public Menu(String name,String description){
		this.name = name;
		this.description = description;
	}
	
	public void add(MenuComponent menuComponent){
		menuComponents.add(menuComponent);
	}
	
	public void remove(MenuComponent menuComponent){
		menuComponents.remove(menuComponent);
	}
	
	public MenuComponent getChild(int i){
		return (MenuComponent)menuComponents.get(i);
	}
	
	public String getName(){
		return name;
	}
	
	public String getDescription(){
		return description;
	}
	
	public Iterator createIterator(){
		return new CompositeIterator(menuComponents.iterator());
	}
	
	public void print(){
		System.out.println("\n" + getName());
		System.out.println("," + getDescription());
		System.out.println("-----------");
		
		/**
		 * 看吧！我们用了迭代器,用它遍历所有菜单组件....遍历过程中,可能遇到其他菜单,或者是遇到菜单项。
		 * 由于菜单和菜单项都实现了print(),那我们只需要调用print()即可。
		 */
		Iterator iterator = menuComponents.iterator();
		while(iterator.hasNext()){
			MenuComponent menuComponent = (MenuComponent)iterator.next();
			menuComponent.print();//递归
		}
	}
}
