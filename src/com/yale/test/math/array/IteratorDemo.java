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
 * 用Iterator模式实现遍历集合
 * Iterator模式是用于遍历集合类的标准访问方法。它可以把访问逻辑从不同类型的集合类中抽象出来，从而避免向客户端暴露集合的内部结构。
 * 例如，如果没有使用Iterator，遍历一个数组的方法是使用索引：
 * for (int i=0; i<array.size(); i++) {
 * }
 * 而访问一个链表（LinkedList）又必须使用while循环：
 * while ((e=e.next())!=null) {
 * }
 * 以上两种方法客户端都必须事先知道集合的内部结构，访问代码和集合本身是紧耦合，无法将访问逻辑从集合类和客户端代码中分离出来，每一种集合对应一种遍历方法，客户端代码无法复用。
 * 更恐怖的是，如果以后需要把ArrayList更换为LinkedList，则原来的客户端代码必须全部重写。
 * 为解决以上问题，Iterator模式总是用同一种逻辑来遍历集合：
 * 奥秘在于客户端自身不维护遍历集合的"指针"，所有的内部状态（如当前元素位置，是否有下一个元素）都由Iterator来维护，而这个Iterator由集合类通过工厂方法生成，因此，它知道如何遍历整个集合。
 * 客户端从不直接和集合类打交道，它总是控制Iterator，向它发送"向前"，"向后"，"取当前元素"的命令，就可以间接遍历整个集合。
 * 首先看看java.util.Iterator接口的定义：
 * public interface Iterator {
	    boolean hasNext();
	    Object next();
	    void remove();
	}
 * 依赖前两个方法就能完成遍历，典型的代码如下：
 * for (Iterator it = c.iterator(); it.hasNext(); ) {
	    Object o = it.next();
	}
 * 每一种集合类返回的Iterator具体类型可能不同，Array可能返回ArrayIterator，Set可能返回SetIterator，Tree可能返回TreeIterator，
 * 但是它们都实现了Iterator接口，因此，客户端不关心到底是哪种Iterator，它只需要获得这个Iterator接口即可，这就是面向对象的威力。
 * Iterator源码剖析
 * 让我们来看看AbstracyList如何创建Iterator。首先AbstractList定义了一个内部类（inner class）：
 * private class Itr implements Iterator<E> {}
 * 而iterator()方法的定义是：
 * public Iterator iterator() {
    return new Itr();
 * }
 * 因此客户端不知道它通过Iterator it = a.iterator();所获得的Iterator的真正类型。
 * Itr类依靠3个int变量（还有一个隐含的AbstractList的引用）来实现遍历，cursor是下一次next()调用时元素的位置，第一次调用next()将返回索引为0的元素。lastRet记录上一次游标所在位置，因此它总是比cursor少1。
 * 变量cursor和集合的元素个数决定hasNext()：
 * public boolean hasNext() {
	    return cursor != size();
	}
 * 方法next()返回的是索引为cursor的元素，然后修改cursor和lastRet的值：
 * expectedModCount表示期待的modCount值，用来判断在遍历过程中集合是否被修改过。AbstractList包含一个modCount变量，它的初始值是0，当集合每被修改一次时（调用add，remove等方法），modCount加1。因此，modCount如果不变，表示集合内容未被修改。
 * Itr初始化时用expectedModCount记录集合的modCount变量，此后在必要的地方它会检测modCount的值：
 * 如果modCount与一开始记录在expectedModeCount中的值不等，说明集合内容被修改过，此时会抛出ConcurrentModificationException。
 * 这个ConcurrentModificationException是RuntimeException，不要在客户端捕获它。如果发生此异常，说明程序代码的编写有问题，应该仔细检查代码而不是在catch中忽略它。
 * 但是调用Iterator自身的remove()方法删除当前元素是完全没有问题的，因为在这个方法中会自动同步expectedModCount和modCount的值：
 * 要确保遍历过程顺利完成，必须保证遍历过程中不更改集合的内容（Iterator的remove()方法除外），因此，确保遍历可靠的原则是只在一个线程中使用这个集合，或者在多线程中对遍历代码进行同步。
 * 最后给个完整的示例：
 * Collection c = new ArrayList();
	c.add("abc");
	c.add("xyz");
	for (Iterator it = c.iterator(); it.hasNext(); ) {
	    String s = (String)it.next();
	    System.out.println(s);
	}
 * 如果你把第一行代码的ArrayList换成LinkedList或Vector，剩下的代码不用改动一行就能编译，而且功能不变，这就是针对抽象编程的原则：对具体类的依赖性最小。
 * https://www.liaoxuefeng.com/article/895885644922112
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
