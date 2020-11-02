package com.yale.test.design.graphic.builder;

public class QuoteBuilder {
	public String buildQuote(String line) {
		//return "<blockquote>" + line.substring(1).strip() + "</blockquote>";
		return "<blockquote>" + line.substring(1) + "</blockquote>";
	}
}
