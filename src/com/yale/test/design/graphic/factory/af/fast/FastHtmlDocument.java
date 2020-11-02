package com.yale.test.design.graphic.factory.af.fast;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.yale.test.design.graphic.factory.af.HtmlDocument;

public class FastHtmlDocument implements HtmlDocument {
	
	private String md;
	
	public FastHtmlDocument(String md) {
		this.md = md;
	}
	
	@Override
	public String toHtml() {
//		return md.lines().map(s -> {
//			if (s.startsWith("#")) {
//				return "<h1>" + s.substring(1) + "</h1>";
//			}
//			return "<p>" + s + "</p>";
//		}).reduce("", (acc, s) -> acc + s + "\n");
		return "<h1>" + md.substring(1) + "</h1>";
	}
	
	@Override
	public void save(Path path) throws IOException {
		Files.write(path, toHtml().getBytes("UTF-8"));
	}
}
