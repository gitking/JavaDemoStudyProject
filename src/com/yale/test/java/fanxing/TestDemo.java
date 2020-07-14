package com.yale.test.java.fanxing;

import java.util.ArrayList;
import java.util.List;

public class TestDemo {

	/**
	 * 在之前定义的类活接口上发现都可以在里面的方法中继续使用泛型,这种方法就称为泛型方法。
	 * 但是泛型方法不一定非定义在泛型类或接口里面,也可以单独定义
	 * @param args
	 */
	public static void main(String[] args) {
		Integer data[] = fun(1,2,2);
		for (int temp : data) {
			System.out.println(temp);
		}
		/*
		 * 在Java标准库中的ArrayList<T>实现了List<T>接口，它可以向上转型为List<T>：
		 * 即类型ArrayList<T>可以向上转型为List<T>。
		 * 注意泛型的继承关系：可以把ArrayList<Integer>向上转型为List<Integer>（T不能变！），
		 * 但不能把ArrayList<Integer>向上转型为ArrayList<Number>（T不能变成父类） 
		 * 要特别注意：不能把ArrayList<Integer>向上转型为ArrayList<Number>或List<Number>。
		 * ArrayList<Integer>和ArrayList<Number>两者完全没有继承关系。
		 */
 	}
	
	//泛型方法可以单独定义在一个非泛型类里面
	 public static <T> T[] fun(T...args){
		 return args;
	 }
	 
	 /*
	  * 编写泛型类时，要特别注意，泛型类型<T>不能用于静态方法。例如：
	  * 下面代码会导致编译错误，我们无法在静态方法create()的方法参数和返回类型上使用泛型类型T。
	  */
//	 public static List<T> create(T first, T last){
//		 return new ArrayList<T>();
//	 }
	 
	 /*
	  * 有些同学在网上搜索发现，可以在static修饰符后面加一个<T>，编译就能通过：
	  * 但实际上，这个<T>和List<T>类型的<T>已经没有任何关系了。
	  * 对于静态方法，我们可以单独改写为“泛型”方法，只需要使用另一个类型即可。对于上面的create()静态方法，我们应该把它改为另一种泛型类型，例如，<K>
	  * 这样才能清楚地将静态方法的泛型类型和实例类型的泛型类型区分开。
	  */
	 public static <T> List<T>  create(T first, T last){
		 return new ArrayList<T>();
	 }
	 
	 /*
	  * 对于静态方法，我们可以单独改写为“泛型”方法，只需要使用另一个类型即可。对于上面的create()静态方法，我们应该把它改为另一种泛型类型，例如，<K>
	  * 这样才能清楚地将静态方法的泛型类型和实例类型的泛型类型区分开。
	  * 这个静态方法不要跟类的的泛型一样,比如类上面用的泛型是T,你静态方法就用K就行
	  * 静态方法不能引用泛型类型<T>，必须定义其他类型（例如<K>）来实现静态泛型方法；
	  */
	 public static <K> List<K> createK(K first, K last){
		 return new ArrayList<K>();
	 }
}
