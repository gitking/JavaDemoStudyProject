package com.yale.test.java.demo;
/**
 * protected
 * 继承有个特点，就是子类无法访问父类的private字段或者private方法。例如，Student类就无法访问Person类的name和age字段：
 * 用protected修饰的字段可以被子类访问：
 * protected关键字可以把字段和方法的访问权限控制在继承树内部，一个protected字段和方法可以被其子类，以及子类的子类所访问，后面我们还会详细讲解。
 * protected作用于继承关系。定义为protected的字段和方法可以被子类访问，以及子类的子类：
 * 因此，继承是is关系，组合是has关系。
 * https://www.liaoxuefeng.com/wiki/1252599548343744/1260454548196032
 * @author dell
 */
public class SubPersonDemo extends PersonDemo {
    protected int score;
    protected static int staticFile;

    
    /*
     * 在Java中，任何class的构造方法，第一行语句必须是调用父类的构造方法。如果没有明确地调用父类的构造方法，
     * 编译器会帮我们自动加一句super();，所以，Student类的构造方法实际上是这样：
     * 如果父类没有默认的构造方法，子类就必须显式调用super()并给出参数以便让编译器定位到父类的一个合适的构造方法。
	 * 这里还顺带引出了另一个问题：即子类不会继承任何父类的构造方法。子类默认的构造方法是编译器自动生成的，不是继承的。
     */
    public SubPersonDemo(String name, int age, int score){
    	/*
    	 * this()只能用在构造函数中,且它必须是第一行语句。
    	 * 每个构造函数可以选择调用super()或this(),但不能同时调用,super()与this()不能兼得。
    	 */
    	super(name, age);//如果父类没有无参构造方法,需要自己手工调用父类的构造方法
    	this.score = score;
    }
    
    
    /*
     * 在继承关系中，子类如果定义了一个与父类方法签名完全相同的方法，被称为覆写（Override）。
     * Override覆写和Overload方法重载不同的是，如果方法签名如果不同，就是Overload，Overload方法是一个新方法；
     * 如果方法签名相同，并且返回值也相同，就是Override。
     */
}
