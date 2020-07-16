package com.yale.test.math.queue;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * Stack栈是一种先进后出的数据结构，
 * Stack是Vector的子类
 * Stack只有入栈和出栈的操作：
	    把元素压栈：push(E)；
	    把栈顶的元素“弹出”：pop(E)；
	    取栈顶元素但不弹出：peek(E)。
	在Java中，我们用Deque可以实现Stack的功能：
	    把元素压栈：push(E)/addFirst(E)；
	    把栈顶的元素“弹出”：pop(E)/removeFirst()；
	    取栈顶元素但不弹出：peek(E)/peekFirst()。
	为什么Java的集合类没有单独的Stack接口呢？因为有个遗留类名字就叫Stack，出于兼容性考虑，所以没办法创建Stack接口，只能用Deque接口来“模拟”一个Stack了。
 * 当我们把Deque作为Stack使用时，注意只调用push()/pop()/peek()方法，不要调用addFirst()/removeFirst()/peekFirst()方法，这样代码更加清晰。
 * @author dell
 */
public class StackDemo {
	public static void main(String[] args) {
		/**
		 * Stack在计算机中使用非常广泛，JVM在处理Java方法调用的时候就会通过栈这种数据结构维护方法调用的层次。例如：
		 * JVM会创建方法调用栈，每调用一个方法时，先将参数压栈，然后执行对应的方法；当方法返回时，返回值压栈，调用方法通过出栈操作获得方法返回值。
		 * 因为方法调用栈有容量限制，嵌套调用过多会造成栈溢出，即引发StackOverflowError：
		 * Stack继承了Vector类,而Vector又实现了List接口
		 * 所以Stack既可以当List使用,又可以当栈使用,
		 */
		Stack<String> stack = new Stack<String>();
		stack.push("A");//入栈
		stack.push("B");
		stack.push("C");
		stack.push("D");
		//stack.add();调用的是List接口的方法,不要这样用
		/**
		 * stack.peek();
		 * pop()内部调用的是peek()方法
		 */
		System.out.println("出栈" + stack.pop());
		System.out.println(stack.pop());
		System.out.println(stack.pop());
		
		//fun();//这样写会报错StackOverflowError栈溢出错误
		
		/*
		 * 我们再来看一个Stack的用途：对整数进行进制的转换就可以利用栈。
		 * 例如，我们要把一个int整数12500转换为十六进制表示的字符串，如何实现这个功能？
		 * 首先我们准备一个空栈：
		 * 然后计算12500÷16=781…4，余数是4，把余数4压栈：
		 * 然后计算781÷16=48…13，余数是13，13的十六进制用字母D表示，把余数D压栈：
		 * 然后计算48÷16=3…0，余数是0，把余数0压栈：
		 * 最后计算3÷16=0…3，余数是3，把余数3压栈：
		 * 当商是0的时候，计算结束，我们把栈的所有元素依次弹出，组成字符串30D4，这就是十进制整数12500的十六进制表示的字符串。
		 * 练习题：
		 * https://www.liaoxuefeng.com/wiki/1252599548343744/1265121668997888
		 */
	}
	
	public static void fun() {
		fun();
	}
}
