package com.yale.test.java.fanxing;

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
		
		//当使用泛型类不指定尖括号里面的类型是,默认就是Object类型
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
