package com.yale.test.lambda;

public class Test {
	public static void main(String[] args) {
		IMessage msg = () -> System.out.println("Lambda表达式,是一种函数式编程,使用Lambda表达式时,接口IMessage只能有一个方法");
		msg.print();
		
		IMessage msgSec = () -> {
				System.out.println("Lambda表达式,是一种函数式编程,当方法有多行代码时,这样写");
				System.out.println("Lambda表达式,是一种函数式编程,当方法有多行代码时,这样写");
				System.out.println("Lambda表达式,是一种函数式编程,当方法有多行代码时,这样写");
			};
		msgSec.print();
		
		IMath math = (m1,m2) -> m1 + m2;
		System.out.println("Lambda表达式,当方法只有返回值时,可以这么写" + math.add(10, 20));
		
		//方法引用
		IUtil<Integer,String> util = String :: valueOf;//这行代码的意思是,接口IUtil的方法changeType引用String类里面的valueOf方法
		String result = util.changeType(100);
		System.out.println("方法引用:" + result.length());
		
		IUtilSec utilSec = String :: valueOf;
		String resultSec = utilSec.changeType(100);
		System.out.println("方法引用:" + resultSec.length());
		
		IUtilThrid utilThrid = "hello" :: toUpperCase;
		System.out.println(utilThrid.changeWord());

		IUtilFour<Person,String,Integer> utilFour = Person :: new;
		System.out.println("构造方法引用:" +  utilFour.createPer("王亚乐", 12));
		
		IUtilSix<Integer, String,String> utilSix = String :: compareTo;
		System.out.println(utilSix.compare("H", "h"));
		
	}
}
