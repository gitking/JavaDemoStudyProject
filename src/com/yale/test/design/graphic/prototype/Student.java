package com.yale.test.design.graphic.prototype;

/*
 * 对于普通类，我们如何实现原型拷贝？Java的Object提供了一个clone()方法，它的意图就是复制一个新的对象出来，
 * 我们需要实现一个Cloneable接口来标识一个对象是“可复制”的：
 * 使用的时候，因为clone()的方法签名是定义在Object中，返回类型也是Object，所以要强制转型，比较麻烦：
 * 实际上，使用原型模式更好的方式是定义一个copy()方法，返回明确的类型：
 * 原型模式应用不是很广泛，因为很多实例会持有类似文件、Socket这样的资源，而这些资源是无法复制给另一个对象共享的，只有存储简单类型的“值”对象可以复制。
 * 小结
 * 原型模式是根据一个现有对象实例复制出一个新的实例，复制出的类型和属性与原实例相同。
 */
public class Student implements Cloneable{
	private int id;
	private String name;
	private int score;
	
	@Override
	public Object clone() {
		Student std = new Student();
		std.id = this.id;
		std.name = this.name;
		std.score = this.score;
		return std;
	}
	
	public Student copy() {
		Student std = new Student();
		std.id = this.id;
		std.name = this.name;
		std.score = this.score;
		return std;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	
	@Override
	public String toString() {
		return String.format("{Student: id=%s, name=%s, score=%s}@%s", this.id, this.name, this.score, Integer.toHexString(hashCode()));
	}
}
