package com.yale.test.java.fanshe.perfma.demo;

/*
 * 这个类模仿的是java中rt.jar包中的sun.reflect.NativeMethodAccessorImpl
 * @author dell
 */
public class Father implements Person{
	public int num;
	private DelegatingClass parent;
	
	@Override
	public void change() {
		if (++this.num > 15) {//超过15岁(就是16岁,15岁的时候还养,16岁就不养了),就让孩子来养我
			this.parent.setDelegatingPerson(new Son());
		}
		System.out.println("儿子我养你到" + this.num + "岁,我在模仿java中rt.jar包中的sun.reflect.NativeMethodAccessorImpl方法");
	}

	public void setParent(DelegatingClass parent) {
		this.parent = parent;
	}
}
