package com.yale.test.java.demo.jicheng;

public class Father {
	public String name = "父类的name属性";
	public Father() {
		/**
		 * 记住子类对象可能需要动用到从父类继承下来的东西,所以那些东西必须要先完成。
		 * 父类的构造函数必须在子类的构造函数之前结束。
		 */
        super(); // 构造方法的第一行代码总是调用super(),如果你自己没加,JVM编译时会自动帮你加上
        //接下来初始化成员变量,上面name的值实际上是在这里被初始化的.
        System.out.println("父类只有无参构造方法,子类没有无参构造方法,子类的有参构造方法一样会自动调用父类的构造方法");
	}
	public void setName(String val) {
		this.name = this.name + val;
	}
}
