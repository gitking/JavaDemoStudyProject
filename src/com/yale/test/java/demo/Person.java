package com.yale.test.java.demo;

public class Person {
	public static void main(String[] args) {
		Person ps = new Person();
		Student stu = new Student();
		
		ps.testParam(stu);//传子类
		
		Person tempP = stu;//先将子类对象赋值给父类对象
	
		stu.testParam01((Student)tempP);
		
		Student stuTemp = (Student)tempP;//这行代码不会报错,因为tempP实际上就是一个Student对象,所以这里将父类强制转换为子类不会报错
		stuTemp.test();
		
		Student temp = (Student)ps;//这行代码会报错,因为ps这个对象实际上一个父类对象,所以不能将一个实际的父类对象强制转换为子类对象
		ps.testParam01(temp);//这里不强制会报错,但是强制调用testParam01方法有风险,如果testParam01方法内部调用了,子类特有的方法,代码运行就会报错
	}
	
	
	public void testParam(Person per) {
		System.out.println("参数如果是父类,则实际传参时即可以传父类也可以传子类");
	}
	
	public void testParam01(Student stu) {
		System.out.println("参数如果是子类,则实际传参时不能传父类对象过来,编译会报错");
		System.out.println("但是可以把父类强制转为子类后,再传进来.");
		//stu.test();
	}
}
