package com.yale.test.design.graphic.factory.af.fast;

import com.yale.test.design.graphic.factory.af.AbstractFactory;
import com.yale.test.design.graphic.factory.af.HtmlDocument;
import com.yale.test.design.graphic.factory.af.WordDocument;

public class FastFactory implements AbstractFactory {
	@Override
	public HtmlDocument createHtml(String md) {
		return new FastHtmlDocument(md);
	}
	
	@Override
	public WordDocument createWord(String md) {
		return new FastWordDocument(md);
	}
}
