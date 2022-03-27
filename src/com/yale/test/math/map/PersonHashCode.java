package com.yale.test.math.map;

import java.util.Objects;

/*
 * 本类和com.yale.test.math.map.MapDemo.java一起看
 * 本类和com.yale.test.math.array.ListDemo.java一起看
 * 本类和com.yale.test.math.array.SetDemo.java一起看
 * https://www.liaoxuefeng.com/wiki/1252599548343744/1265117217944672
 * https://www.liaoxuefeng.com/wiki/1252599548343744/1265116446975264
 */
public class PersonHashCode {
	public String firstName;
	public String lastName;
	public int age;
	
	/**
	 * 编写equals方法,
	 * 对于引用字段比较，我们使用equals()，对于基本类型字段的比较，我们使用==。
	 * 如果this.name为null，那么equalsTest()方法会报错，因此，需要继续改写如下：
	 */
	public boolean equalsTest(Object o) {
		if (o instanceof PersonHashCode) {
			PersonHashCode ph = (PersonHashCode)o;
			return this.firstName.equals(ph.firstName) && this.age == ph.age;
		}
		return false;
	}
	
	//如果Person有好几个引用类型的字段，下面的写法就太复杂了。要简化引用类型的比较，我们使用Objects.equals()静态方法：
	public boolean equalsTest1(Object o) {
		if (o instanceof PersonHashCode) {
			PersonHashCode ph = (PersonHashCode)o;
			boolean nameEquals = false;
			if (this.firstName == null && ph.firstName == null) {
				nameEquals = true;
			}
			if (this.firstName != null) {
				nameEquals = this.firstName.equals(ph.firstName);
			}
			return nameEquals && this.age == ph.age;
		}
		return false;
	}
	
	/**
	 * 编写equals方法
	 * 因此，我们总结一下equals()方法的正确编写方法：
     * 先确定实例“相等”的逻辑，即哪些字段相等，就认为实例相等；
     * 用instanceof判断传入的待比较的Object是不是当前类型，如果是，继续比较，否则，返回false；
     * 对引用类型用Objects.equals()比较，对基本类型直接用==比较。
     * 使用Objects.equals()比较两个引用类型是否相等的目的是省去了判断null的麻烦。两个引用类型都是null时它们也是相等的。
     * 如果不调用List的contains()、indexOf()这些方法，那么放入的元素就不需要实现equals()方法。
     * https://www.liaoxuefeng.com/wiki/1252599548343744/1265117217944672
     * 《阿里巴巴Java开发手册嵩山版2020.pdf》
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof PersonHashCode) {
			PersonHashCode ph = (PersonHashCode)o;
			return Objects.equals(this.firstName, ph.firstName) && this.age == ph.age;
		}
		return false;
	}
	
	/*
	 * 在正确实现equals()的基础上，我们还需要正确实现hashCode()，即上述3个字段分别相同的实例，hashCode()返回的int必须相同：
	 * 注意到String类已经正确实现了hashCode()方法，我们在计算Person的hashCode()时，反复使用31*h，这样做的目的是为了尽量把不同的Person实例的hashCode()均匀分布到整个int范围。
	 * 和实现equals()方法遇到的问题类似，如果firstName或lastName为null，上述代码工作起来就会抛NullPointerException。为了解决这个问题，我们在计算hashCode()的时候，经常借助Objects.hash()来计算：
	 * 所以，编写equals()和hashCode()遵循的原则是：
	 * equals()用到的用于比较的每一个字段，都必须在hashCode()中用于计算；equals()中没有使用到的字段，绝不可放在hashCode()中计算。
	 * 另外注意，对于放入HashMap的value对象，没有任何要求。
	 * hashCode()方法编写得越好，HashMap工作的效率就越高。
	 * https://www.liaoxuefeng.com/wiki/1252599548343744/1265117217944672#0
	 */
	int hashCode1() {
		int h = 0;
		//firstName有空指针的危险
		h = 31 * h + firstName.hashCode();
		h = 31 * h + lastName.hashCode();
		h = 31 * h + age;
		return h;
	}
	
	/**
	 * 编写equals和hashCode
	 * 编写hashCode方法
	 * https://www.liaoxuefeng.com/wiki/1252599548343744/1265117217944672
	 */
	@Override
	public int hashCode() {
		return java.util.Objects.hash(firstName, lastName, age);
	}
}
