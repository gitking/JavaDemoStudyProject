package com.yale.test.design.cmoposite;

/**
 * Waitress 负责打印菜单
 */
public class Waitress {
	MenuComponent allMenus;
	public Waitress(MenuComponent allMenus){
		this.allMenus = allMenus;
	}
	public void printMenu(){
		allMenus.print();
	}
}
