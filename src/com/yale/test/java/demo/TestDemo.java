package com.yale.test.java.demo;

public class TestDemo {

	public static void main(String[] args) {
		System.out.println("可变参数" + add(1,2,2));
		//可变参数可以接收数组
		System.out.println("可变参数可以接收数组" + add(new int[] {6,7,8,8,9}));
		//可变参数也可以不传参数
		System.out.println("可变参数可以不传参数" + add());
	}
	
	/**
	 * 可变参数
	 * 可变参数必须放在方法参数的最后面,一个方法里面只能有一个参数
	 * @param data
	 * @return
	 */
	public static int add (int ... data) {
		int sum = 0;
		for (int x=0; x < data.length; x++) {
			sum += data[x];
		}
		return sum;
	}
	
	
}
