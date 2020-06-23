package com.yale.test.java.demo;

import java.util.Arrays;

public class TestDemo {
	public String[] nameFiled;
	private String strVal;
	
	public static void main(String[] args) {
		System.out.println("可变参数" + add(1,2,2));
		//可变参数可以接收数组
		System.out.println("可变参数可以接收数组" + add(new int[] {6,7,8,8,9}));
		//可变参数也可以不传参数
		System.out.println("可变参数可以不传参数" + add());
		
		
		String[] names = new String[]{"test", "demo"};
		TestDemo td = new TestDemo();
		td.stringArr(names);
		names[0] = "change";
		System.out.println("引用类型参数的传递，调用方的变量，和接收方的参数变量，指向的是同一个对象。双方任意一方对这个对象的修改，"
				+ "都会影响对方（因为指向同一个对象嘛）。" + Arrays.toString(td.nameFiled));
		
		String str = "String也是引用对象";
		td.setStrVal(str);
		System.out.println("现在的值:" + td.getStrVal());
		str = "为什么没有改变";
		System.out.println("现在的值:" + td.getStrVal());
		
		System.out.println("可变参数可以传null吗?" + add(null));

	}
	
	/**
	 * 可变参数
	 * 可变参数必须放在方法参数的最后面,一个方法里面只能有一个可变参数
	 * 而可变参数可以保证无法传入null，因为传入0个参数时，接收到的实际值是一个空数组而不是null(这个应该是jdk12的特性)。
	 * 基本类型参数的传递，是调用方值的复制。双方各自的后续修改，互不影响。
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
	
	
	public void stringArr (String[] names) {
		this.nameFiled = names;
	}

	public String getStrVal() {
		return strVal;
	}

	public void setStrVal(String strVal) {
		this.strVal = strVal;
	}
}
