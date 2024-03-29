package com.yale.test.java.demo;

public class PersonDemo {
	private String name;
    private int age;

    public PersonDemo(String name, int age) {
        this.name = name;
        this.age = age;
    }

    /*
     * 一个构造方法可以调用其他构造方法，这样做的目的是便于代码复用。调用其他构造方法的语法是this(…)：
	 * 记住子类对象可能需要动用到从父类继承下来的东西,所以那些东西必须要先完成。
	 * 父类的构造函数必须在子类的构造函数之前结束。
     */
    public PersonDemo(String name) {
    	/*
    	 * this()和super()只能用在构造函数中,且它必须是第一行语句。
    	 * 每个构造函数可以选择调用super()或this(),但不能同时调用,super()与this()不能兼得。
    	 */
        this(name, 12);//这行代码必须放在第一行
       // this.name = name;
        //this.age = 12;
    }

//    public PersonDemo() {
//    }
    
    /*
     * 方法重载
     * 如果有一系列方法，它们的功能都是类似的，只有参数有所不同，那么，可以把这一组方法名做成同名方法。例如，在PersonDemo类中，定义多个hello()方法： 
     * 这种方法名相同，但各自的参数不同，称为方法重载（Overload）。
     * 注意：方法重载的返回值类型通常都是相同的。
     * 注意：方法名相同，方法参数相同，但方法返回值不同，也是不同的方法。在Java程序中，出现这种情况，编译器会报错。 
     * 如果方法签名相同，并且返回值也相同，就是Override。
     * 如果方法签名如果不同，就是Overload，Overload方法是一个新方法；
     */
    public void hello() {
        System.out.println("Hello, world!");
    }

    public void hello(String name) {
        System.out.println("Hello, " + name + "!");
    }
    
    protected void hello(double name) {
        System.out.println("Hello, " + name + "!");
    }
    
//    public String hello(String name) {//方法重载：方法名和返回值必须相同,参数可以不同,方法修饰符不同
//        System.out.println("Hello, " + name + "!");
//        return "";
//    }

    public void hello(String name, int age) {
        if (age < 18) {
            System.out.println("Hi, " + name + "!");
        } else {
            System.out.println("Hello, " + name + "!");
        }
    }
}
