package com.yale.test.java.demo;

//一个.java文件只能包含一个public类，但可以包含多个非public类。如果有public类，文件名必须和public类的名字相同。
public class InnerClass {
	public static void main(String[] args) {
		
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
}
