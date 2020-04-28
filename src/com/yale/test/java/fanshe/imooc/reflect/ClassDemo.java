package com.yale.test.java.fanshe.imooc.reflect;
class Foo {
	static {
		System.out.println("如果类已经被JVM加载过了,Class.forName就不会重新加载这个类了");
	}
	void print() {
		System.out.println("通过反射调用");
	}
}
public class ClassDemo {

	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		/**
		 * new创建对象,是静态加载类,在编译时刻就需要加载所有的可能使用到的类
		 * Class.forName是动态加载类
		 */
		Foo foo1 = new Foo();
		//Foo.class,其实说明了Java中每个类都有一个隐藏的静态变量
		Class c1 = Foo.class;
		
		Class c2 = foo1.getClass();
		System.out.println("类对象都一样不一样呢?" + (c1 == c2));
		/**
		 * Class.forName还能动态加载类
		 */
		Class c3 = Class.forName("com.yale.test.java.fanshe.imooc.reflect.Foo");
		System.out.println("类对象都一样不一样呢?" + (c1 == c3));
		Foo foo2 = (Foo)c3.newInstance();//Foo类必须有无参构造方法
		foo2.print();

	}
}
