package com.yale.test.java.meiju;

/*
 * 使用enum定义的枚举类是一种引用类型。前面我们讲到，引用类型比较，要使用equals()方法，如果使用==比较，
 * 它比较的是两个引用类型的变量是否是同一个对象。因此，引用类型比较，要始终使用equals()方法，但enum类型可以例外。
 * 这是因为enum类型的每个常量在JVM中只有一个唯一实例，所以可以直接用==比较：
 * 通过enum定义的枚举类，和其他的class有什么区别？
	答案是没有任何区别。enum定义的类型就是class，只不过它有以下几个特点：
	    定义的enum类型总是继承自java.lang.Enum，且无法被继承；
	    只能定义出enum的实例，而无法通过new操作符创建enum的实例；
	    定义的每个实例都是引用类型的唯一实例；
	    可以将enum类型用于switch语句。
 */
enum ColorDemo {
	RED, GREEN, BLUE//这里实际上调用的是ColorDemo的无参构造方法,如果没有无参构造方法,这里必须传值,见Sex.java类
}
/**
 * 枚举让多例模式的代码变得简单多了
 * 所谓的枚举就是一种高级的多例设计模式
 * 使用enum关键字定义的枚举类本质上就相当于一个class定义的类继承了java.lang.Enum父类
 * 虽然枚举等同于多例设计模式,但是多例设计是在一个类中产生的,所以该类中可以定义更多的属性或者是方法。只依靠以上的概念只能够说产生了若干个对象,但是并没有方法去定义更多的机构
 * 所以在枚举设计的时候考虑到了这些因素,提出了更强大的枚举设计方案:可以在枚举里面定义属性,方法,或者实现接口
 * 枚举最大的特点是只有指定的几个对象可以使用
 * 另外需要注意的是,枚举本身还支持switch判断,也就是说switch按照时间进度来讲,最初只支持int和char,到了JDK1.5的时候支持了枚举,到了
 * jdk1.7的时候支持了String
 * switch这种开关语句有个重要的特点:如果你在编写case的时候没有加上break;则会在满足的case语句之后一直执行,直到遇见break;或全部结束
 * @author dell
 */
public class ColorEnum {
	public static void main(String[] args) {
		System.out.println("ColorDemo枚举类跟多例是一样的:" + ColorDemo.RED);
		
		/**
		 * 注意观察java.lang.Enum类的构造方法
		 * protected Enum(String name, int ordinal)当定义enum类中的对象的时候自动设置序号和名字
		 * ordinal取的枚举的序号,name取的枚举的数据
		 * values()可以取的所有的枚举数据,返回的是一个枚举的对象数组
		 */
		System.out.println(ColorDemo.RED.ordinal() + " = " + ColorDemo.RED.name());
		
		for (ColorDemo temp: ColorDemo.values()) {
			System.out.println(temp.ordinal() + " = " + temp.name());
		}
		switch (Sex.MAN) {//枚举本身还支持switch判断
			case MAN:
				System.out.println("是男人");
				break;
			case WOMAN:
				System.out.println("是女人");
				break;
		}
	}
}
