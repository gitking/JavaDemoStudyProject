package com.yale.test.design.structural.facade;

import com.yale.test.design.structural.facade.organization.Company;

public class Test {
	public static void main(String[] args) {
		Facade facade = new Facade();
		Company c = facade.openCompany("Facade Software Ltd.");
		System.out.println(c);
	}
}
