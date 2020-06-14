package com.yale.test.math.array;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;

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
	}
}
