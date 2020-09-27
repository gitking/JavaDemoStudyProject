package com.yale.test.java.demo.jicheng;

public class Father {
	public String name = "父类的name属性";
	public Father() {
        super(); // 构造方法的第一行代码总是调用super(),如果你自己没加,JVM编译时会自动帮你加上
        //接下来初始化成员变量,上面name的值实际上是在这里被初始化的.
	}
	public void setName(String val) {
		this.name = this.name + val;
	}
}
