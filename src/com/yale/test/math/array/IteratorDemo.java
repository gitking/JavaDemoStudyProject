package com.yale.test.math.array;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;

/*
 * 使用迭代器的好处在于，调用方总是以统一的方式遍历各种集合类型，而不必关系它们内部的存储结构。
 * 这里的关键在于，集合类通过调用iterator()方法，返回一个Iterator对象，这个对象必须自己知道如何遍历该集合。
 */
public class IteratorDemo {
	public static void main(String[] args) {
		List<String> list = new ArrayList<String>();
		list.add("Hello");
		list.add("1");
		list.add("Hello");
		list.add("Word");
		Iterator<String> it = list.iterator();//集合输出就用Iterator,死记就行,方便
		int i =0;
		while(it.hasNext()) {//while循环实际上的意思是:不知道循环次数,但知道循环结束条件,就用while循环
			i ++;
			if (i==1) {
				/**
				 * list在循环中如果增加数据会报错
				 * java.util.ConcurrentModificationException
				 * 而且Iterator没有增加方法,ListIterator才有增加方法
				 */
				//list.add("ListIterator可以在迭代过程中增加数据");
			}
			System.out.println(it.next());
		}
		
		ListIterator<String> listIt = list.listIterator();
		/**
		 * 如果要想实现由后向前的输出,那么应该首先由前向后输出,否则无法实现双向,
		 * 因为你向前向后靠的是指针,指针不指到后面,无法实现
		 */
		System.out.println("由后向前输入");
		while (listIt.hasPrevious()) {
			System.out.print(listIt.previous() + ",");
		}
		
		/**
		 * ListIterator 只有list接口才有,ListIterator是双向迭代接口
		 */
		
		System.out.println("由前向后输入");
		i =0;
		while (listIt.hasNext()) {
			i ++;
			if (i==1) {
				listIt.add("ListIterator可以在迭代过程中增加数据");
			}
			System.out.print(listIt.next() + ",");
		}
		System.out.println();
		
		/**
		 * 如果要想实现由后向前的输出,那么应该首先由前向后输出,否则无法实现双向,
		 * 因为你向前向后靠的是指针,指针不指到后面,无法实现
		 */
		System.out.println("由后向前输入");
		while (listIt.hasPrevious()) {
			System.out.print(listIt.previous() + ",");
		}
		
		System.out.println();
		/**
		 * Enumeration枚举输出,只有Vector支持枚举输出
		 */
		Vector<String> vector = new Vector<String>();
		vector.add("Hello");
		vector.add("1");
		vector.add("Hello");
		vector.add("Word");
		Enumeration<String> enumeration = vector.elements();
		while (enumeration.hasMoreElements()) {
			System.out.println("Enumeration枚举输出：" + enumeration.nextElement());
		}
		
		for (Enumeration<String> e = vector.elements(); e.hasMoreElements();) {
			System.out.println("使用for循环进行枚举输出:" + e.nextElement());
		}
		
		/*
		 * 实际上，Java编译器并不知道如何遍历List。上述代码能够编译通过，只是因为编译器把for each循环通过Iterator改写为了普通的for循环：
		 * for each就是用Iterator循环的
		 */
		ReverseList<String> rlist = new ReverseList<>();
        rlist.add("Apple");
        rlist.add("Orange");
        rlist.add("Pear");
        for (String s : rlist) {
            System.out.println("自定义的list可以使用Iterator" + s);
        }
	}
}

/*
 * 如果我们自己编写了一个集合类，想要使用for each循环，只需满足以下条件：
	    集合类实现Iterable接口，该接口要求返回一个Iterator对象；
	    用Iterator对象迭代集合内部数据。
	这里的关键在于，集合类通过调用iterator()方法，返回一个Iterator对象，这个对象必须自己知道如何遍历该集合。
	一个简单的Iterator示例如下，它总是以倒序遍历集合：
 */
class ReverseList<T> implements Iterable<T> {
	private List<T> list = new ArrayList<>();
	public void add(T t){
		list.add(t);
	}
	
	@Override
	public Iterator<T> iterator() {
		return new ReverseIterator(list.size());
	}
	
	class ReverseIterator implements Iterator<T> {
		int index;
		ReverseIterator(int index) {
			this.index = index;
		}
		
		@Override
		public boolean hasNext() {
			return index > 0;
		}
		
		@Override
		public T next() {
			index--;
			return ReverseList.this.list.get(index);
		}
	}
}
