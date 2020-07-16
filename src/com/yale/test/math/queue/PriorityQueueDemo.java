package com.yale.test.math.queue;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

/*
 * 这个时候，我们发现，要实现“VIP插队”的业务，用Queue就不行了，因为Queue会严格按FIFO的原则取出队首元素。我们需要的是优先队列：PriorityQueue。
 * PriorityQueue和Queue的区别在于，它的出队顺序与元素的优先级有关，对PriorityQueue调用remove()或poll()方法，
 * 返回的总是优先级最高的元素。
 */
public class PriorityQueueDemo {
	public static void main(String[] args) {
		Queue<String> q = new PriorityQueue<>();
        // 添加3个元素到队列:
        q.offer("apple");
        q.offer("pear");
        q.offer("banana");
        System.out.println(q.poll()); // apple
        System.out.println(q.poll()); // banana
        System.out.println(q.poll()); // pear
        System.out.println(q.poll()); // null,因为队列为空
        
        System.out.println("我们放入的顺序是apple、pear、banana，但是取出的顺序却是apple、banana、pear，这是因为从字符串的排序看，apple排在最前面，pear排在最后面。");
        System.out.println("因此，放入PriorityQueue的元素，必须实现Comparable接口，PriorityQueue会根据元素的排序顺序决定出队的优先级。");
        
        System.out.println("PriorityQueue默认按元素比较的顺序排序（必须实现Comparable接口），也可以通过Comparator自定义排序算法（元素就不必实现Comparable接口）。");
        System.out.println("PriorityQueue实现了一个优先队列：从队首获取元素时，总是获取优先级最高的元素。");
        
        Queue<User> qu = new PriorityQueue<>(new UserComparator());
        // 添加3个元素到队列:
        qu.offer(new User("Bob", "A1"));
        qu.offer(new User("Alice", "A2"));
        qu.offer(new User("Boss", "V1"));
        System.out.println(qu.poll()); // Boss/V1
        System.out.println(qu.poll()); // Bob/A1
        System.out.println(qu.poll()); // Alice/A2
        System.out.println(qu.poll()); // null,因为队列为空
	}
}

class User {
	public final String name;
	public final String number;
	public User(String name, String number) {
		this.name = name;
		this.number = number;
	}
	@Override
	public String toString() {
		return "User [name=" + name + ", number=" + number + "]";
	}
}

class UserComparator implements Comparator<User> {
	@Override
	public int compare(User u1, User u2) {
		if (u1.number.charAt(0) == u2.number.charAt(0)) {
            // 如果两人的号都是A开头或者都是V开头,比较号的大小:
			return u1.number.compareTo(u2.number);
		}
		if (u1.number.charAt(0) == 'V') {
			return -1;// u1的号码是V开头,优先级高:
		} else {
			return 1;
		}
	}
}
