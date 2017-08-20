package com.yale.test.design.cmoposite;

public class MenuTestDrive {

	public static void main(String[] args) {
		MenuComponent pancakeHouseMenu = new Menu("粤菜","早餐");
		MenuComponent dinerMenu = new Menu("湘菜","午餐");
		MenuComponent cafeMenu = new Menu("豫菜","晚餐");
		MenuComponent desserMenu = new Menu("夜宵","侯灿");
		
		MenuComponent allMenus = new Menu("所有菜单","所有食物");
		
		allMenus.add(pancakeHouseMenu);
		allMenus.add(dinerMenu);
		allMenus.add(cafeMenu);
		allMenus.add(desserMenu);
		
		dinerMenu.add(new MenuItem("鱼香肉丝","甜",true,19.9));
		dinerMenu.add(desserMenu);
		
		desserMenu.add(new MenuItem("番茄炒蛋","辣",true,20));
		
		Waitress waitress = new Waitress(allMenus);
		waitress.printMenu();
		
		System.out.println("---素食菜单---");
		
		MenuComponent pancakeHouseMenuSec = new Menu("粤菜","早餐");
		MenuComponent dinerMenuSec = new Menu("湘菜","午餐");
		MenuComponent cafeMenuSec = new Menu("豫菜","晚餐");
		MenuComponent desserMenuSec = new Menu("夜宵","侯灿");
		
		MenuComponent allMenusSec = new Menu("所有菜单","所有食物");
		
		allMenusSec.add(pancakeHouseMenuSec);
		allMenusSec.add(dinerMenuSec);
		allMenusSec.add(cafeMenuSec);
		allMenusSec.add(desserMenuSec);
		
		dinerMenuSec.add(new MenuItem("鱼香肉丝","甜",true,19.9));
		dinerMenuSec.add(desserMenuSec);
		
		desserMenuSec.add(new MenuItem("番茄炒蛋","辣",true,20));
		
		WaitressSec waitressSec = new WaitressSec(allMenusSec);
		waitressSec.printVegetarianMenu();
	}
}
