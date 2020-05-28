package com.yale.test.lambda;

/**
 * Lambda是从JDK1.8推出的重要新特性,函数式编程。
 * @author dell
 */
public class Test {
	public static void main(String[] args) {
		IMessage msgName = new IMessage(){
			@Override
			public void print() {
				System.out.println("这个是一个接口的匿名实现类,要写这么多行代码,就为了打印这一句话。Java面向对象的缺点就是结构太完整了,必须写这么整齐");
			}
		};
		msgName.print();
		
		//这行代码相当于实现了一个匿名接口,不过比匿名接口少写了很多代码。函数式编程虽然少写代码,但是一般人不容易看懂
		IMessage msg = () -> System.out.println("Lambda表达式,是一种函数式编程,使用Lambda表达式时,接口IMessage只能有一个方法");
		msg.print();
		
		IMessage msgSec = () -> {
				System.out.println("Lambda表达式,是一种函数式编程,当方法有多行代码时,这样写");
				System.out.println("Lambda表达式,是一种函数式编程,当方法有多行代码时,这样写");
				System.out.println("Lambda表达式,是一种函数式编程,当方法有多行代码时,这样写");
			};
		msgSec.print();
		
		IMath math = (m1,m2) -> m1 + m2;//只有一行代码,可以不写return
		System.out.println("Lambda表达式,当方法只有返回值时,可以这么写,(这里面实际上参数)->" + math.add(10, 20));
		
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
