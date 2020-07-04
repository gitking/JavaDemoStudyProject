package com.yale.test.java.fanshe.perfma.demo;

/*
 * 这个类模仿的是java中rt.jar包中的sun.reflect.DelegatingMethodAccessorImpl
 * @author dell
 */
public class DelegatingClass implements Person {
	
	public DelegatingClass(Person person) {
		setDelegatingPerson(person);
	}

	private Person delegatingPerson;//接收委派的真实类
	
	@Override
	public void change() {
		this.delegatingPerson.change();//委派类调用目标类的方法
	}
	
	public void setDelegatingPerson(Person delegatingPerson) {
		this.delegatingPerson = delegatingPerson;
	}
}
