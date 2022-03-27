package com.yale.test.java.demo;


/**
 * 内部类
 * 一个类可以嵌套在另一个类的内部。浙很简单,只要确定内部类的定义是包在外部类的括号中就可以。
 * 内部类可以使用外部所有的方法与变量,就是是私有的也一样.内部类把存取外部类的方法和变量当作是开自己冰箱。
 * 内部类的实例一定会绑在外部类的实例上
 * 还有一种非常特殊的异常情况,内部类是定义在静态方法中的。本书《Head First Java》不打算讨论这个,你也可能一辈子都不会遇到。
 * 内部类不能有静态方法
 * 一个Java源文件可以包含多个类的定义，但只能定义一个public类，且public类名必须与文件名一致。如果要定义多个public类，必须拆到多个Java源文件中。 
 * com.yale.test.java.swing.InnerButton
 * 
 * javap -private 'InnerClass$Inner'
 * 执行javap命令后，我们看到普通内部类 A 比静态内部类 A 多了一个特殊的字段：com.yale.test.java.demo.InnerClass this$0。
 * 普通内部类多出的这个字段是 JDK “偷偷”为我们添加的，它指向了外部类 Home。
 * 所以，我们也就搞清楚了，之所以普通内部类可以直接访问外部类的所有成员，是因为 JDK 为普通内部类偷偷添加了这么一个隐式的变量 this$0，指向外部类。
 * 那么，我们应该优先选择普通内部类还是静态内部类呢？
 * 《Effective java》 Item 24 的内容是：Favor static member classes over nonstatic，即：优先考虑使用静态内部类。
 * 因为非静态内部类会持有外部类的一个隐式引用（this$0）, 存储这个引用需要占用时间和空间。更严重的是有可能会导致宿主类在满足垃圾回收的条件时却仍然驻留在内存中，由此引发内存泄漏的问题。
 * 所以，在需要使用内部类的情况下，我们应该尽可能选择使用静态内部类。
 * @author dell
 */
//一个.java文件只能包含一个public类，但可以包含多个非public类。如果有public类，文件名必须和public类的名字相同。
public class InnerClass {
	public static void main(String[] args) {
		InnerClass in = new InnerClass();
		
		//创建内部类的实例
		InnerClass.MyInner inner = in.new MyInner();
		
		InnerClass.Inner.innerTest();//调用静态内部类的静态方法
		
		InnerClass.Inner staticInner = new InnerClass.Inner();//静态内部类,不需要外部类的实例
		staticInner.hi();
		//匿名内部类
		Thread th = new Thread(new Runnable(){
			@Override
			public void run() {
				System.out.println("匿名内部类");
			}
		});
	}
	
	private int privateNum;
	public int publicNum;
	private static int privateStaticNum;
	public static int publicStaticNum;
	
	private static void test() {
		//定义在一个class内部的class称为嵌套类（nested class），Java支持好几种嵌套类。
		System.out.println("由于Java支持嵌套类，如果一个类内部还定义了嵌套类，那么，嵌套类拥有访问private的权限：");
	}
	
	public static void testOuterPub() {
		Inner inn = new Inner();//在外部类的静态方法里面可以直接new静态内部类
		//MyInner mi = new MyInner();//但是不能直接new 普通内部类
		MyInner mi = new InnerClass().new MyInner();//但是不能直接new 普通内部类
		//定义在一个class内部的class称为嵌套类（nested class），Java支持好几种嵌套类。
		System.out.println("由于Java支持嵌套类，如果一个类内部还定义了嵌套类，那么，嵌套类拥有访问private的权限：");
	}
	
	private void outerTest() {
		MyInner mi = new MyInner();//在外部类的实例方法里面可以直接new内部类
		Inner inn = new Inner();//在外部类的实例方法里面可以直接new静态内部类
		//定义在一个class内部的class称为嵌套类（nested class），Java支持好几种嵌套类。
		System.out.println("由于Java支持嵌套类，如果一个类内部还定义了嵌套类，那么，嵌套类拥有访问private的权限：");
	}
	
	public void outerPubTest() {
		//定义在一个class内部的class称为嵌套类（nested class），Java支持好几种嵌套类。
		System.out.println("由于Java支持嵌套类，如果一个类内部还定义了嵌套类，那么，嵌套类拥有访问private的权限：");
	}
	
	/**
	 * 普通内部类与静态内部类有什么区别？
	 * 1、静态内部类：静态内部类只能访问外部类的静态属性及方法，无法访问外部类的普通成员（变量和方法）
	 * 2、静态内部类可以包含静态的属性和方法。而普通内部类中不能包含静态的属性和方法。
	 * @author issuser
	 */
	static class Inner {//静态内部类,不需要外部类的实例
		//静态内部类很像一般普通内部类,静态内部类并未与外层对象产生特殊关联.但因为还被认为是外层的一个成员,
		//所以能够存取任何外层的私有成员,然而只限于静态的私有成员,
		public void hi() {
			test();//test是外部类的静态私有方法,静态内部类,可以访问外部类的静态的private属性和方法
			testOuterPub();
			InnerClass.test();//静态内部类,可以访问外部类的静态的private属性和方法
			//outerPubTest();//静态内部类不可以直接调用外部类的实例方法
			//outerTest();//静态内部类不可以直接调用外部类的私有的实例方法
			
			InnerClass sdf = new InnerClass();
			sdf.outerPubTest();//静态内部类必须通过外部类的实例对象访问外部类的实例方法
		}
		private int numPri;
		public static int num = 10;
		public static void innerTest() {
			System.out.println("静态内部类的静态方法");
		}
		
		public void innerTestPri() {
			System.out.println("静态内部类的实例方法");
		}
		public Inner() {
			num ++;
		}
		
		
	}
	
	/*
	 * 要记住你需要外部类的实例才能取得内部类的实例,因为内部类是外部的一个成员.
	 * 普通内部类与静态内部类有什么区别？
	 * 1、普通内部类：可以访问外部类的所有属性和方法，普通内部类中不能包含静态的属性和方法
	 * 2、普通内部类依赖外部类的对象实例
	 * https://mp.weixin.qq.com/s?__biz=MzU2NzAzMjQyOA==&mid=2247483988&idx=1&sn=5b34b96a5312f2687dd28bce39d542f4&chksm=fca22d57cbd5a441db81097d265556c18cdb386e1ea456e607e78f9a629a4ce9033947d19721&scene=21#wechat_redirect
	 */
	class MyInner{
		
		void go() {
			System.out.println("内部类");
			
			test();//test是外部类的静态私有方法,静态内部类,可以访问外部类的静态的private属性和方法
			testOuterPub();
			
			InnerClass sdf = new InnerClass();
			sdf.outerPubTest();//静态内部类必须通过外部类的实例对象访问外部类的实例方法
			
			outerPubTest();//普通内部类可以直接调用外部类的实例方法
			outerTest();//普通内部类可以直接调用外部类的私有的实例方法
			
			privateNum++;//外部类私有属性可以直接访问
			publicNum++;//外部类公开属性可以直接访问
			privateStaticNum++;//静态私有属性也可以直接获取
			publicStaticNum++;//静态共有属性也可以直接获取
		}
		
		public int testNum = 10;//普通属性可以有
		
		//public static String testStr = "";普通内部类中不能包含静态的属性和方法
		
//		public static String testMethod() {普通内部类中不能包含静态的属性和方法
//			return "";
//		}
	}
}
