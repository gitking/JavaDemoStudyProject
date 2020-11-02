package com.yale.test.design.graphic.factory.af.good;

import java.io.IOException;
import java.nio.file.Path;

import com.yale.test.design.graphic.factory.af.WordDocument;

public class GoodWordDocument implements WordDocument {

	private String md;
	
	public GoodWordDocument(String md) {
		this.md = md;
	}
	
	@Override
	public void save(Path path) throws IOException {
		
	}
}
