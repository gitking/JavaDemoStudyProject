package com.yale.test.math.queue;

import java.util.LinkedList;
import java.util.Queue;

/*
 * Queue是一个接口队列,队列是先进先出的数据结构,Queue继承了Collection接口
 * 队列（Queue）是一种经常使用的集合。Queue实际上是实现了一个先进先出（FIFO：First In First Out）的有序表。
 * 它和List的区别在于，List可以在任意位置添加和删除元素，而Queue只有两个操作：
	    把元素添加到队列末尾；
	    从队列头部取出元素。
     在Java的标准库中，队列接口Queue定义了以下几个方法：
    int size()：获取队列长度；
    boolean add(E)/boolean offer(E)：添加元素到队尾；
    E remove()/E poll()：获取队首元素并从队列中删除；
    E element()/E peek()：获取队首元素但并不从队列中删除。
 * Queue的子类有LinkedList,SynchronousQueue
 */
public class QueueDemo {
	public static void main(String[] args) {
		//LinkedList即实现了List接口，又实现了Queue接口，但是，在使用的时候，如果我们把它当作List，就获取List的引用，如果我们把它当作Queue，就获取Queue的引用：
		Queue<String> queue = new LinkedList<String>();
		queue.add("A");//入栈
		queue.add("B");//入栈
		queue.add("C");//入栈
		queue.add("D");//入栈
		System.out.println("队列poll取数据:" + queue.poll());
		System.out.println(queue.poll());
		System.out.println(queue.poll());
		System.out.println(queue.poll());
		//注意：不要把null添加到队列中，否则poll()方法返回null时，很难确定是取到了null元素还是队列为空。
		System.out.println("队列取不到值返回null:" + queue.poll());//取不到返回null
		
		/*
		 * 接下来我们以poll()和peek()为例来说说“获取并删除”与“获取但不删除”的区别。对于Queue来说，每次调用poll()，都会获取队首元素，并且获取到的元素已经从队列中被删除了：
		 * 如果用peek()，因为获取队首元素时，并不会从队列中删除这个元素，所以可以反复获取：
		 */
		 Queue<String> q = new LinkedList<>();
        // 添加3个元素到队列:
        q.offer("apple");
        q.offer("pear");
        q.offer("banana");
        // 从队列取出元素:
        System.out.println(q.poll()); // apple
        System.out.println(q.poll()); // pear
        System.out.println(q.poll()); // banana
        System.out.println(q.poll()); // null,因为队列是空的
        
        Queue<String> qk = new LinkedList<>();
        // 添加3个元素到队列:
        qk.offer("apple");
        qk.offer("pear");
        qk.offer("banana");
        // 队首永远都是apple，因为peek()不会删除它:
        System.out.println(qk.peek()); // apple
        System.out.println(qk.peek()); // apple
        System.out.println(qk.peek()); // apple
	}
}
