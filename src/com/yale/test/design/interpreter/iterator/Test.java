package com.yale.test.design.interpreter.iterator;

/*
 * 迭代器
 * 提供一种方法顺序访问一个聚合对象中的各个元素，而又不需要暴露该对象的内部表示。
 * 迭代器模式（Iterator）实际上在Java的集合类中已经广泛使用了。我们以List为例，要遍历ArrayList，即使我们知道它的内部存储了一个Object[]数组，
 * 也不应该直接使用数组索引去遍历，因为这样需要了解集合内部的存储结构。如果使用Iterator遍历，那么，ArrayList和LinkedList都可以以一种统一的接口来遍历：
 *  List<String> list = ...
	for (Iterator<String> it = list.iterator(); it.hasNext(); ) {
	    String s = it.next();
	}
 * 实际上，因为Iterator模式十分有用，因此，Java允许我们直接把任何支持Iterator的集合对象用foreach循环写出来：
 * List<String> list = ...
	for (String s : list) {
	
	}
 * 然后由Java编译器完成Iterator模式的所有循环代码。
 * 虽然我们对如何使用Iterator有了一定了解，但如何实现一个Iterator模式呢？我们以一个自定义的集合为例，通过Iterator模式实现倒序遍历：
 * 实现Iterator模式的关键是返回一个Iterator对象，该对象知道集合的内部结构，因为它可以实现倒序遍历。我们使用Java的内部类实现这个Iterator：
 * 使用内部类的好处是内部类隐含地持有一个它所在对象的this引用，可以通过ReverseArrayCollection.this引用到它所在的集合。
 * 上述代码实现的逻辑非常简单，但是实际应用时，如果考虑到多线程访问，当一个线程正在迭代某个集合，而另一个线程修改了集合的内容时，
 * 是否能继续安全地迭代，还是抛出ConcurrentModificationException，就需要更仔细地设计。
 * 小结
 * Iterator模式常用于遍历集合，它允许集合提供一个统一的Iterator接口来遍历元素，同时保证调用者对集合内部的数据结构一无所知，从而使得调用者总是以相同的接口遍历各种不同类型的集合。
 */
public class Test {
	public static void main(String[] args) {
		ReverseArrayCollection<String> rac = new ReverseArrayCollection<String>("apple", "pear", "orange", "banana");
		System.out.println(rac);
		for (String str : rac) {
			System.out.println(str);
		}
	}
}
