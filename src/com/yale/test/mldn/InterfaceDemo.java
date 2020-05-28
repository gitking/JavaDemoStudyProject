package com.yale.test.mldn;

interface IMessage {
	
	public void print();
	
	public default void fun() {
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
