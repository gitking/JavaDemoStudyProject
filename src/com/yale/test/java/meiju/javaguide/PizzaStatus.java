package com.yale.test.java.meiju.javaguide;

/*
 * 用好Java中的枚举真的没有那么简单
 * https://github.com/Snailclimb/JavaGuide/blob/master/docs/java/basis/%E7%94%A8%E5%A5%BDJava%E4%B8%AD%E7%9A%84%E6%9E%9A%E4%B8%BE%E7%9C%9F%E7%9A%84%E6%B2%A1%E6%9C%89%E9%82%A3%E4%B9%88%E7%AE%80%E5%8D%95.md
 * 最近重看 Java 枚举，看到这篇觉得还不错的文章，于是简单翻译和完善了一些内容，分享给大家，希望你们也能有所收获。另外，不要忘了文末还有补充哦！
 * ps: 这里发一篇枚举的文章，也是因为后面要发一篇非常实用的关于 SpringBoot 全局异常处理的比较好的实践，里面就用到了枚举。
 * 这篇文章由 JavaGuide 翻译，公众号: JavaGuide,原文地址：https://www.baeldung.com/a-guide-to-java-enums 。
 * 转载请注明上面这段文字。
 * 这篇文章翻译的不行,词不达意,有点看不懂我。2020年10月24日16:46:57
 * 1.概览
 * 在本文中，我们将看到什么是 Java 枚举，它们解决了哪些问题以及如何在实践中使用 Java 枚举实现一些设计模式。
 * enum关键字在 java5 中引入，表示一种特殊类型的类，其总是继承java.lang.Enum类，更多内容可以自行查看其官方文档(https://docs.oracle.com/javase/6/docs/api/java/lang/Enum.html)。
 * 枚举在很多时候会和常量拿来对比，可能因为本身我们大量实际使用枚举的地方就是为了替代常量。那么这种方式由什么优势呢？
 * 以这种方式定义的常量使代码更具可读性，允许进行编译时检查，预先记录可接受值的列表，并避免由于传入无效值而引起的意外行为。
 * 下面示例定义一个简单的枚举类型 pizza 订单的状态，共有三种 ORDERED, READY, DELIVERED状态:
 * 简单来说，我们通过下面的代码避免了定义常量，我们将所有和 pizza 订单的状态的常量都统一放到了一个枚举类型里面。
 * 2.自定义枚举方法
 * 现在我们对枚举是什么以及如何使用它们有了基本的了解，让我们通过在枚举上定义一些额外的API方法，将上一个示例提升到一个新的水平：Pizza
 * 3.使用 == 比较枚举类型
 */
public enum PizzaStatus {
	ORDERED,
	READY,
	DELIVERED;
	public static void main(String[] args) {
		System.out.println(PizzaStatus.ORDERED.name());//ORDERED
		System.out.println(PizzaStatus.ORDERED);//ORDERED
		System.out.println(PizzaStatus.ORDERED.name().getClass());//class java.lang.String
		System.out.println(PizzaStatus.ORDERED.getClass());//class com.yale.test.java.meiju.javaguide.PizzaStatus
	}
}
