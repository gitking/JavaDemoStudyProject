package com.yale.test.design.structural.decorator;

public class ItalicDecorator extends NodeDecorator {
	
	public ItalicDecorator(TextNode target) {
		super(target);
	}
	
	@Override
	public String getText() {
		return "<li>" + target.getText() + "</li>";
	}
}
