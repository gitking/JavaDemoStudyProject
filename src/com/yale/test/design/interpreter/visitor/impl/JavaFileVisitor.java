package com.yale.test.design.interpreter.visitor.impl;

import java.io.File;

import com.yale.test.design.interpreter.visitor.Visitor;

public class JavaFileVisitor implements Visitor{

	@Override
	public void visitDir(File dir) {
		System.out.println("Visitor dir:" + dir);
	}

	@Override
	public void visitFile(File file) {
		if (file.getName().endsWith(".java")) {
			System.out.println("Found java file: " + file);
		}
	}
}
