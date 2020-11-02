package com.yale.test.design.graphic.factory.af.good;

import com.yale.test.design.graphic.factory.af.AbstractFactory;
import com.yale.test.design.graphic.factory.af.HtmlDocument;
import com.yale.test.design.graphic.factory.af.WordDocument;

public class GoodFactory implements AbstractFactory{

	@Override
	public HtmlDocument createHtml(String md) {
		return new GoodHtmlDocument(md);
	}

	@Override
	public WordDocument createWord(String md) {
		return new GoodWordDocument(md);
	}

}
