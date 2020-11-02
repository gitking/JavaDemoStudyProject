package com.yale.test.design.structural.composite.impl;

import java.util.Collections;
import java.util.List;

import com.yale.test.design.structural.composite.Node;

public class CommentNode implements Node {
	private String text;
	
	public CommentNode(String text) {
		this.text = text;
	}
	
	public Node add(Node node) {
		throw new UnsupportedOperationException();
	}
	
	public List<Node> children() {
		return Collections.EMPTY_LIST;
	}
	
	public String toXml() {
		return "<!--" + text + "-->";
	}
}
