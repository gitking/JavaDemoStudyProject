package com.yale.test.java.demo;


/**
 * 内部类
 * 一个类可以嵌套在另一个类的内部。浙很简单,只要确定内部类的定义是包在外部类的括号中就可以。
 * 内部类可以使用外部所有的方法与变量,就是是私有的也一样.内部类把存取外部类的方法和变量当作是开自己冰箱。
 * 内部类的实例一定会绑在外部类的实例上
 * 还有一种非常特殊的异常情况,内部类是定义在静态方法中的。本书《Head First Java》不打算讨论这个,你也可能一辈子都不会遇到。
 * 内部类不能有静态方法
 * @author dell
 */
//一个.java文件只能包含一个public类，但可以包含多个非public类。如果有public类，文件名必须和public类的名字相同。
public class InnerClass {
	public static void main(String[] args) {
		InnerClass in = new InnerClass();
		
		//创建内部类的实例
		InnerClass.MyInner inner = in.new MyInner();
	}
	
	private static void test() {
		//定义在一个class内部的class称为嵌套类（nested class），Java支持好几种嵌套类。
		System.out.println("由于Java支持嵌套类，如果一个类内部还定义了嵌套类，那么，嵌套类拥有访问private的权限：");
	}
	
	static class Inner {
		public void hi() {
			InnerClass.test();//静态内部类,可以访问外部类的private属性和方法
		}
	}
	
	class MyInner{
		void go() {
			System.out.println("内部类");
		}
	}
}
