package com.yale.test.java.demo.jicheng.demo01;

/**
 * JAVA中的继承,方法和属性都可以继承，但是注意：覆盖，方法可以覆盖,但是属性不会覆盖。
 * @author issuser
 */
public class Test {
	public static void main(String[] args) {
		SuperClassDemo scd = new SubClassDemo();
		System.out.println(scd instanceof SubClassDemo);
		System.out.println(scd instanceof SuperClassDemo);
		System.out.println(scd instanceof Object);
		System.out.println(scd.getClass());
		System.out.println("--------------------------------");
		//注意test方法输出的属性是父类的,说明test这个方法虽然被子类SubClassDemo继承了,但是name这个属性没有被覆盖。JVM执行的时候还是从父类那里去执行的。
		scd.test();
		System.out.println("--------------------------------");
		SubClassDemo scd2 = new SubClassDemo();
		//注意test方法输出的属性是父类的,说明test这个方法虽然
		scd2.test();
	}
}
