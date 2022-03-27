package com.yale.test.java.demo;

import com.yale.test.java.demo.InnerClass.Inner;
import com.yale.test.java.demo.InnerClass.MyInner;

/**
 * 内部类
 * 一个类可以嵌套在另一个类的内部。浙很简单,只要确定内部类的定义是包在外部类的括号中就可以。
 * 内部类可以使用外部所有的方法与变量,就是是私有的也一样.内部类把存取外部类的方法和变量当作是开自己冰箱。
 * 内部类的实例一定会绑在外部类的实例上
 * 还有一种非常特殊的异常情况,内部类是定义在静态方法中的。本书《Head First Java》不打算讨论这个,你也可能一辈子都不会遇到。
 * 内部类不能有静态方法
 * 一个Java源文件可以包含多个类的定义，但只能定义一个public类，且public类名必须与文件名一致。如果要定义多个public类，必须拆到多个Java源文件中。 
 * com.yale.test.java.swing.InnerButton
 * @author dell
 */
//一个.java文件只能包含一个public类，但可以包含多个非public类。如果有public类，文件名必须和public类的名字相同。
public class InnerClass2 {
	public static void main(String[] args) {
		InnerClass in = new InnerClass();
		MyInner min = in.new MyInner();//普通内部类
		Inner inner = new InnerClass.Inner();//静态内部类
		
		InnerClass.Inner.innerTest();
	}
}
