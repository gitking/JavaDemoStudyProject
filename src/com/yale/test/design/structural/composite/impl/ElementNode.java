package com.yale.test.design.structural.composite.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import com.yale.test.design.structural.composite.Node;

public class ElementNode implements Node {
	private String name;
	private List<Node> list = new ArrayList<Node>();
	
	public ElementNode(String name) {
		this.name = name;
	}
	
	public Node add(Node node) {
		list.add(node);
		return this;
	}
	
	public List<Node> children() {
		return list;
	}
	
	public String toXml() {
		String start = "<" + name + ">";
		String end = "</" + name + ">\n";
		StringJoiner sj = new StringJoiner("", start, end);
		list.forEach(node -> {
			sj.add(node.toXml() + "\n");
		});
		return sj.toString();
	}
}
