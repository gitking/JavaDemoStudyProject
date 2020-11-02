package com.yale.test.design.structural.facade;

import com.yale.test.design.structural.facade.organization.AdminOfIndustry;
import com.yale.test.design.structural.facade.organization.Bank;
import com.yale.test.design.structural.facade.organization.Company;
import com.yale.test.design.structural.facade.organization.Taxation;

/*
 * 外观
 * 为子系统中的一组接口提供一个一致的界面。Facade模式定义了一个高层接口，这个接口使得这一子系统更加容易使用。
 * 外观模式，即Facade，是一个比较简单的模式。它的基本思想如下：
 * 如果客户端要跟许多子系统打交道，那么客户端需要了解各个子系统的接口，比较麻烦。如果有一个统一的“中介”，让客户端只跟中介打交道，中介再去跟各个子系统打交道，对客户端来说就比较简单。所以Facade就相当于搞了一个中介。
 * 我们以注册公司为例，假设注册公司需要三步：
 * 1.向工商局申请公司营业执照；2.在银行开设账户 3.在税务局开设纳税号。
 * 如果子系统比较复杂，并且客户对流程也不熟悉，那就把这些流程全部委托给中介：
 */
public class Facade {
	private AdminOfIndustry admin = new AdminOfIndustry();
	private Bank bank = new Bank();
	private Taxation taxation = new Taxation();
	
	public Company openCompany(String name) {
		Company c = this.admin.register(name);
		String bankAccount = this.bank.openAccount(c.getId());
		c.setBankAccount(bankAccount);
		
		String taxCode = this.taxation.applyTaxCode(c.getId());
		c.setTaxCode(taxCode);
		return c;
	}
}
