package com.yale.test.design.graphic.factory.af;

import java.io.IOException;
import java.nio.file.Paths;

import com.yale.test.design.graphic.factory.af.fast.FastFactory;
import com.yale.test.design.graphic.factory.af.good.GoodFactory;

/*
 * 小结
 * 抽象工厂模式是为了让创建工厂和一组产品与使用相分离，并可以随时切换到另一个工厂以及另一组产品；
 * 抽象工厂模式实现的关键点是定义工厂接口和产品接口，但如何实现工厂与产品本身需要留给具体的子类实现，客户端只和抽象工厂与抽象产品打交道。
 * https://www.liaoxuefeng.com/wiki/1252599548343744/1281319134822433
 */
public class Test {
	public static void main(String[] args) throws IOException {
		AbstractFactory fastFactory = new FastFactory();
		
		HtmlDocument fastHtml =  fastFactory.createHtml("#Hello\nHello, world!");
		System.out.println(fastHtml.toHtml());
		fastHtml.save(Paths.get(".", "fast.html"));
		
		WordDocument fastWord = fastFactory.createWord("#Hello\nHello, world!");
		fastWord.save(Paths.get(".", "fast.doc"));
		
		/*
		 * 如果我们要同时使用GoodDoc Soft的服务怎么办？因为用了抽象工厂模式，
		 * GoodDoc Soft只需要根据我们定义的抽象工厂和抽象产品接口，实现自己的实际工厂和实际产品即可：
		 * 客户端要使用GoodDoc Soft的服务，只需要把原来的new FastFactory()切换为new GoodFactory()即可。
		 * 注意到客户端代码除了通过new创建了FastFactory或GoodFactory外，其余代码只引用了产品接口，并未引用任何实际产品（例如，FastHtmlDocument），如果把创建工厂的代码放到AbstractFactory中，就可以连实际工厂也屏蔽了：
		 */
		AbstractFactory goodFactory = new GoodFactory();
		HtmlDocument goodHtml = goodFactory.createHtml("");
		System.out.println(goodHtml.toHtml());
		goodHtml.save(Paths.get(".", "good.html"));
		WordDocument goodWord = goodFactory.createWord("");
		goodWord.save(Paths.get(".", "good.doc"));
	}
}
