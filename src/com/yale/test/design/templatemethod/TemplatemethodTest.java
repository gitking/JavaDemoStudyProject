package com.yale.design.templatemethod;

public class TemplatemethodTest {
	public static void main(String[] args) {
		Tea tea = new Tea();
		tea.prepareRecipe();
		Coffee coffee = new Coffee();
		coffee.prepareRecipe();
	}
}
