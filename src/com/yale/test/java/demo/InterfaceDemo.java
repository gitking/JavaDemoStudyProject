package com.yale.test.java.demo;

/*
 * 所谓interface，就是比抽象类还要抽象的纯抽象接口，因为它连字段都不能有。
 * 因为接口定义的所有方法默认都是public abstract的，所以这两个修饰符不需要写出来（写不写效果都一样）。
 * interface是可以有静态字段的，并且静态字段必须为final类型：实际上，因为interface的字段只能是public static final类型，所以我们可以把这些修饰符都去掉
 * 合理设计interface和abstract class的继承关系，可以充分复用代码。一般来说，公共逻辑适合放在abstract class中，具体逻辑放到各个子类，
 * 而接口层次代表抽象程度。可以参考Java的集合类定义的一组接口、抽象类以及具体子类的继承关系：
┌───────────────┐
│   Iterable    │
└───────────────┘
        ▲                ┌───────────────────┐
        │                │      Object       │
┌───────────────┐        └───────────────────┘
│  Collection   │                  ▲
└───────────────┘                  │
        ▲     ▲          ┌───────────────────┐
        │     └──────────│AbstractCollection │
┌───────────────┐        └───────────────────┘
│     List      │                  ▲
└───────────────┘                  │
              ▲          ┌───────────────────┐
              └──────────│   AbstractList    │
                         └───────────────────┘
                                ▲     ▲
                                │     │
                                │     │
                     ┌────────────┐ ┌────────────┐
                     │ ArrayList  │ │ LinkedList │
                     └────────────┘ └────────────┘
 * @author dell
 */
interface IMessage {
	
	public void print();
	
	public default void fun() {
		//default方法和抽象类的普通方法是有所不同的。因为interface没有字段，default方法无法访问字段，而抽象类的普通方法可以访问实例字段。
		//实现类可以不必覆写default方法。default方法的目的是，当我们需要给接口新增一个方法时，会涉及到修改全部子类。如果新增的是default方法，
		//那么子类就不必全部修改，只需要在需要覆写的地方去覆写新增方法。
		System.out.println("注意从JDK1.8之后接口里面可以定义普通方法了");
		System.out.println("注意从JDK1.8之后在接口里面可以用default关键字定义普通方法了,用default定义的普通方法需要通过对象调用");
		System.out.println("注意从JDK1.8之后在接口里面可以用static关键字定义静态方法了,用static定义的普通方法通过接口名就可以调用");
	}
	
	public static IMessage getInstance() {
		return new MessageImpl();
	}
	
}

class MessageImpl implements IMessage {
	@Override
	public void print() {
		System.out.println("实现了接口的方法");
	}
}
public class InterfaceDemo {

	public static void main(String[] args) {
		MessageImpl mi = (MessageImpl)IMessage.getInstance();
		mi.fun();//这个是用default定义在接口里面的方法
		mi.print();
	}
}
