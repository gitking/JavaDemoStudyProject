package com.yale.test.java.demo;

/*
 * 如果一个类不希望任何其他类继承自它，那么可以把这个类本身标记为final。用final修饰的类不能被继承
 */
public  class SuperDemo extends Student{
	public final String strss;//这里不初始化赋值,变量会报错,解决这个报错的办法是在构造方法里面给strss初始化赋值
	public final String name = "用final修饰的字段在初始化后不能被修改";
	
	//用final static修饰的变量,必须直接初始化,否则编译报错
	public final static String strStatic = "";//这里不初始化赋值,变量会报错,解决这个报错的办法是在构造方法里面给strss初始化赋值
	
	public SuperDemo(){
		strss = "";//这里不给strss初始化赋值,编译会报错
	}
	public SuperDemo(String name) {
		strss = "";//这里不给strss初始化赋值,编译一样会报错
	}
	
	public static void main(String[] args) {
		SuperDemo.testStatic();//静态方法也可以被继承
		SuperDemo sd = new SuperDemo();
		sd.superDemo();
		//sd.name="121";这里会报错,因为name,是用final修饰的,并且JVM会针对final修饰的字段做优化,详情参考PerfmaDemo类.
		//并且用final修饰的属性,也不能提供set方法
		System.out.println();
	}
	
	public void superDemo() {
		System.out.println("SuperDemo类:我是子类,我继承了Student类,Student类继承了Person类。");
		//在子类的覆写方法中，如果要调用父类的被覆写的方法，可以通过super来调用。例如：
		super.superDemo();//调父类Student的superDemo方法
	}
	
	public final void method() {
		System.out.println("继承可以允许子类覆写父类的方法。如果一个父类不允许子类对它的某个方法进行覆写，"
				+ "可以把该方法标记为final。用final修饰的方法不能被Override：");
	}

	public String getName() {
		return name;
	}
	
//	public void setName(String name){
//		this.name = name;
//	}
}
