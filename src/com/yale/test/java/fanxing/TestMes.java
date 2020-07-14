package com.yale.test.java.fanxing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestMes {
	public static void main(String[] args) {
		
		IMessage<String> mes = new Message<String>();
		mes.print("泛型实现类依然为泛型");
		
		IMessage<String> mesStr = new Message<String>();
		mes.print("泛型实现类在实现的时候,明确指定了实现的类型");
		
		IMessage<Integer> mesInt = new MessageNumberImpl<Integer>();
		mesInt.print(10);
		
		//注意MesNumber设置了类型上限,这里只能使用Number和number的子类
		MesNumber<Integer> mesInt1 = new MesNumber<Integer>();
		mesInt1.setNode(99);
		fun1(mesInt1);
		
		try {
			MesNumber<Integer> pair = new MesNumber<>(Integer.class);
			pair.getNode();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		
		Message<Integer> msg = new Message<Integer>();
		msg.setNode(12);
		fun(msg);//注意方法里面打印的String类型,不是msg的Integer
		
		
		MesSuper<String> mesInt2 = new MesSuper<String>();
		mesInt2.setNode("asdfasdfas");
		fun2(mesInt2);//fun2是用super修饰的
		
	}
	
	/**
	 * 使用?通配符描述的是它可以接收任意类型,但是由于不确定类型,所以无法修改
	 * 在?号基础上又产生了俩个子通配符
	 *  ? extends 类(这个可以用在类型声明上):设置泛型上限,? extends Number 表示只能够设置Number或其子类
	 *  换句话说，这是一个对参数Message<?> temp 进行只读的方法（恶意调用set(null)除外）。
	 *  ? super 类(这个只能用在参数上):设置泛型下限，？ super String 表示只能设置String或其父类
	 *  换句话说，使用<? super Integer>通配符作为方法参数，表示方法内部代码对于参数只能写，不能读。
	 * @param temp
	 */
	public static void fun(Message<?> temp) {
		//上面用了?号之后,这个方法里面就不能调用set方法了,但是可以get取数据
		//temp.setNode("asdf天天很帅气");//注意上面的参数没有给泛型指定类型,这个时候setNode的类型是Object类型的
		//这里唯一的例外是可以给方法参数传入null：
		System.out.println(temp.getNode());
	}
	
	public static void fun1(MesNumber<? extends Number> temp) {
		//上面用了?号之后,这个方法里面就不能调用set方法了,但是可以get取数据
		//temp.setNode("asdf天天很帅气");//注意上面的参数没有给泛型指定类型,这个时候setNode的类型是Object类型的
		System.out.println(temp.getNode());
	}
	
	
	public static void fun2(MesSuper<? super String> temp) {
		//上面用了?号之后,这个方法里面就不能调用set方法了,但是可以get取数据
		//temp.setNode("asdf天天很帅气");//注意上面的参数没有给泛型指定类型,这个时候setNode的类型是Object类型的
		//String str = temp.getNode();这里需要强制换行
		System.out.println(temp.getNode());
	}
	
	//fun(Message<String> temp)和fun(Message<Integer> temp)
	//不能同时定义,因为在编译之后会把泛型擦除掉,编译成class的时候是没有泛型类型的,这个俩个方法就一样了
	//泛型是java的一个语法糖
//	public static void fun(Message<String> temp) {
//		temp.setNote("天天很帅气");
//		System.out.println(temp.getNote());
//	}
//	
//	public static void fun(Message<Integer> temp) {
//		temp.setNote(11);
//		System.out.println(temp.getNote());
//	}
}
