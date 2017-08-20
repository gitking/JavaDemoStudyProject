package com.yale.test.design.cmoposite;

import java.util.Iterator;
import java.util.Stack;

public class CompositeIterator implements Iterator {
	Stack stack = new Stack();//栈先进后出
	
	public CompositeIterator(Iterator iterator){
		stack.push(iterator);
	}
	
	/**
	 * 将元素放在栈顶,通过hasNext()递归调用
	 * 将该元素的子元素循环完毕
	 */
	public Object next(){
		if(hasNext()){
			Iterator iterator = (Iterator)stack.peek();//将栈顶上元素拿出来,而不将该元素从栈中移除
			MenuComponent component = (MenuComponent)iterator.next();
			if(component instanceof Menu){
				stack.push(component.createIterator());
			}
			return component;
		} else {
			return null;
		}
	}
	
	/**
	 * 递归调用hasNext()方法判断
	 * Stack栈里面的每个元素是否还有子元素,如果没有子元素就将其从栈中移除掉
	 */
	public boolean hasNext(){
		if(stack.empty()){
			return false;
		} else {
			Iterator iterator = (Iterator) stack.peek();//将栈顶上元素拿出来,而不将该元素从栈中移除
			if(iterator.hasNext()){
				return true;
			} else {
				stack.pop();//将栈顶上的元素拿出来，并将从栈中移除这个元素
				return hasNext();
			}
		}
	}
	
	public void remove(){
		throw new UnsupportedOperationException("不支持删除");
	}
}
