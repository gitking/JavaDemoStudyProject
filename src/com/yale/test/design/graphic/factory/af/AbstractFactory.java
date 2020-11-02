package com.yale.test.design.graphic.factory.af;

import com.yale.test.design.graphic.factory.af.fast.FastFactory;
import com.yale.test.design.graphic.factory.af.good.GoodFactory;

/*
 * 抽象工厂
 * 提供一个创建一系列相关或相互依赖对象的接口，而无需指定它们具体的类。
 * 抽象工厂模式（Abstract Factory）是一个比较复杂的创建型模式。
 * 抽象工厂模式和工厂方法不太一样，它要解决的问题比较复杂，不但工厂是抽象的，产品是抽象的，而且有多个产品需要创建，因此，这个抽象工厂会对应到多个实际工厂，每个实际工厂负责创建多个实际产品：
 *                                  ┌────────┐
	                             ─ >│ProductA│
	┌────────┐    ┌─────────┐   │   └────────┘
	│ Client │─ ─>│ Factory │─ ─
	└────────┘    └─────────┘   │   ┌────────┐
	                   ▲         ─ >│ProductB│
	           ┌───────┴───────┐    └────────┘
	           │               │
	      ┌─────────┐     ┌─────────┐
	      │Factory1 │     │Factory2 │
	      └─────────┘     └─────────┘
	           │   ┌─────────┐ │   ┌─────────┐
	            ─ >│ProductA1│  ─ >│ProductA2│
	           │   └─────────┘ │   └─────────┘
	               ┌─────────┐     ┌─────────┐
	           └ ─>│ProductB1│ └ ─>│ProductB2│
	               └─────────┘     └─────────┘
 * 这种模式有点类似于多个供应商负责提供一系列类型的产品。我们举个例子：
 * 假设我们希望为用户提供一个Markdown文本转换为HTML和Word的服务，它的接口定义如：AbstractFactory
 */
public interface AbstractFactory {
	//创建Html文档:
	HtmlDocument createHtml(String md);
	
	//创建word文档:
	WordDocument createWord(String md);
	
	/*
	 * 注意到客户端代码除了通过new创建了FastFactory或GoodFactory外，其余代码只引用了产品接口，并未引用任何实际产品（例如，FastHtmlDocument），
	 * 如果把创建工厂的代码放到AbstractFactory中，就可以连实际工厂也屏蔽了：
	 */
	public static AbstractFactory createFactory(String name) {
		if (name.equalsIgnoreCase("fast")) {
			return new FastFactory();
		} else if (name.equalsIgnoreCase("good")) {
			return new GoodFactory();
		} else {
			throw new IllegalArgumentException("Invalid factory name");
		}
	}
}
