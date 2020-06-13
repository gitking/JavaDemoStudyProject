package com.yale.test.math.array;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * Stack栈是一种先进后出的数据结构，
 * Stack是Vector的子类
 * Queue是一个接口队列,队列是先进先出的数据结构,Queue继承了Collection接口
 * Queue的子类有LinkedList,SynchronousQueue
 * @author dell
 */
public class StackDemo {
	public static void main(String[] args) {
		/**
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
		
		Queue<String> queue = new LinkedList<String>();
		queue.add("A");//入栈
		queue.add("B");//入栈
		queue.add("C");//入栈
		queue.add("D");//入栈
		System.out.println("队列poll取数据:" + queue.poll());
		System.out.println(queue.poll());
		System.out.println(queue.poll());
		System.out.println(queue.poll());
		System.out.println("队列取不到值返回null:" + queue.poll());//取不到返回null
		
		//fun();//这样写会报错StackOverflowError栈溢出错误
	}
	
	public static void fun() {
		fun();
	}
}
