package com.yale.test.design.cmoposite;

import java.util.Iterator;

public class MenuItem extends MenuComponent {
	String name;
	String description;
	boolean vegetarian;
	double price;
	
	public MenuItem(String name,String description,boolean vegetarian,double price){
		this.name = name;
		this.description = description;
		this.vegetarian = vegetarian;
		this.price = price;
	}
	
	public String getName(){
		return name;
	}
	
	public String getDescription(){
		return description;
	}
	
	public double getPrice(){
		return price;
	}
	
	public boolean isVegetarian(){
		return vegetarian;
	}
	
	public Iterator createIterator(){
		return new NullIterator();
	}
	
	public void print(){
		System.out.println(" " + getName());
		if(isVegetarian()){
			System.out.print("素食");
		}
		 System.out.println("," + getPrice());
		 System.out.println(",--" + getDescription());
	}
}
