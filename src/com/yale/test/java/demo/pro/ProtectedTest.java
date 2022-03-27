package com.yale.test.java.demo.pro;

import com.yale.test.java.demo.SubPersonDemo;
import com.yale.test.java.demo.DClass2;
//import com.yale.test.java.demo.DClass;
/*
 * 存取权限和存取修饰符(谁可以看到什么)
 * java有4种存取权限等级与3种存取修饰符,只有3种修饰符的原因是因为有一个缺省的(不加任何的修饰符)的权限等级。
 * 存取权限(从限制最少的列出)
 * public 代表任何程序代码都可以存取的公开事物(类,变量,方法,构造函数)
 * protected 受保护的部分运行起来很像是default,但也能允许不在相同的子类继承受保护的部分
 * default 只有在同一包中的默认事物能够存取
 * private 只有同一类中的程序代码才能存取,它是对类而不是对象设限,所以Dog可以看到别的Dog的私有部分,但Cat就能看到Dog的私有部分
 * 存取修饰符
 * public 用public来设定打算要开放给其他程序代码的类,常数(static final变量),方法,以及大部分的构造函数
 * protected
 * protected与default的等级几乎一样,只有一处不同:它允许不同包中的子类继承它的成员.这就是protected所带来的功能--不在同一个包的子类.
 * 大部分的开发者很少有机会用到protected,也许有一天你会遇到适用的设计和时机.protected只能应用在继承上,如果不同包的子类有对父类实例的引用,
 * 子类无法透过此引用存取父类的protected方法,唯一的办法只能通过继承取得此方法.
 * default
 * protected与default权限等级都是作用在包上,默认的等级是最简单的,它代表只有同一包也是默认等级的程序可以存取,例如没有被明确声明为public的类只能被同一包中的类存取。
 * 存取又是什么意思?不能存取某类的程序代码是不能想到该类的.这个想字代表使用,例如说你不能初始化或声明此受限制类型的变量,参数,返回值。反正就是不能在程序中提到这个类.
 * 不然编译器会报错,想象一下这样隐含的意义--在默认的类中public方法意味着此方法并全然是public的.如果你无法存取该类就无法使用此方法.
 * 为什么要限制同一包的存取?通常包是一群在运作上有关联的类,所以互相存取程序代码是很合理的,而只有少部分的类与方法会开放给外部.
 * 这就是default,它是如此的单纯--只有同一包可以存取.
 * private 设定给大部分的实例变量,以及不想开发给外部程序调用的方法.
 */
public class ProtectedTest{
	
	private float value;
	public static void main(String[] args) {
		SubPersonDemo spd = new SubPersonDemo("test", 10, 99);
		//int sc = spd.score;score是对象属性,只能在子类的对象方法里面使用
		//spd.staticFile = 10;//staticFile使用static修饰的,所以可以在子类中使用
		SubClass sc = new SubClass("test", 10, 99);
		//sc.score = 10;//因为在不同包中,这里会报错,只能在子类中使用,
		//sc.staticFile = 20;//因为在不同包中,这里会报错,只能在子类中使用
		//DClass这个类是用default修饰的所以在这个类里面不能访问
		//DClass dc = new DClass();
		System.out.println("DClass这个类只能在同一个包中的类中才能访问,在不同的包下面会报错,编译直接不通过:");
		DClass2 dc2 = new DClass2();
		//dc2.defaultField;//在当前这个包里面访问不到DClass2类的属性defaultField.
	}
	
	public void testPrivate(ProtectedTest pt){
		System.out.println("这里可以直接调用private私有属性" + pt.value);
	}
}
