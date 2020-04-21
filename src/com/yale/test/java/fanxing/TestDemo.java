package com.yale.test.java.fanxing;

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
 	}
	
	//泛型方法可以单独定义在一个非泛型类里面
	 public static <T> T[] fun(T...args){
		 return args;
	 }
}
