package com.yale.test.java.fanxing;

/**
 * 泛型自从 JDK1.5 引进之后，真的非常提高生产力。一个简单的泛型 T，寥寥几行代码， 就可以让我们在使用过程中动态替换成任何想要的类型，
 * 再也不用实现繁琐的类型转换方法。
 * Java 采用类型擦除（Type erasure generics）的方式实现泛型。用大白话讲就是这个泛型只存在源码(.java文件中)中，编译器将源码编译成字节码之时，就会把泛型『擦除』，所以字节码中并不存在泛型。
 * 编译之后，我们使用 javap -s class 查看字节码。观察 部分的字节码，从 descriptor 可以看到，泛型 T 已被擦除，最终替换成了 Object。
 * 并不是每一个泛型参数被擦除类型后都会变成 Object 类，如果泛型类型为  T extends String 这种方式，最终泛型擦除之后将会变成 String。
 * https://mp.weixin.qq.com/s/1cK5sWFyugGAtg3Be285Qg
 * @author dell
 */
public class Test {
	public static void main(String[] args) {
		Point<String> per = new Point<String>();
		per.setX("北纬13度");
		per.setY("东经12度");
		
		Point<Integer> intPer = new Point<Integer>();
		intPer.setX(10);
		intPer.setY(20);
		
		testMethod(per);
		testMethod2(intPer);
		testMethod3(intPer);
		//testMethod3(per);
		testMethod4(per);
		//testMethod4(intPer);
		
		//当使用泛型类不指定尖括号里面的类型是,默认就是Object类型,这是为了向后兼容泛型没出来之前的那些类不能报错,只会给你警告
		Point objPer = new Point();
		objPer.setX(10.1);
		objPer.setY(20.2);
		testMethod2(objPer);
		testMethod3(objPer);
	}
	
	public static void testMethod(Point<String> point) {
		point.setX("12");
		System.out.println(point);
	}
	
	public static void testMethod2(Point<?> point) {
		//point.setX("12");当参数里面的泛型使用?通配符时,不能再方法内部调用泛型的set方法,因为虚拟机并不知道具体的类型,所以不能确定该怎么设置值
		System.out.println(point);
	}
	
	/**
	 * ? extends 这个可以用在方法类型上,也可以用在参数上,设置参数上限,参数比较是Number或者Number子类
	 * @param numPoint
	 */
	public static void testMethod3(Point<? extends Number> numPoint){
		System.out.println(numPoint);
	}
	
	/**
	 * ? super 这个只能用在参数上
	 * @param strPoint
	 */
	public static void testMethod4(Point<? super String> strPoint) {
		System.out.println(strPoint);
	}
}
