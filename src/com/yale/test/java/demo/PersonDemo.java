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
     */
    public PersonDemo(String name) {
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
