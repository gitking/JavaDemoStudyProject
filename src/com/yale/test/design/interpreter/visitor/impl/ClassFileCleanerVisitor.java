package com.yale.test.design.interpreter.visitor.impl;

import java.io.File;

import com.yale.test.design.interpreter.visitor.Visitor;

public class ClassFileCleanerVisitor implements Visitor{
	@Override
	public void visitDir(File dir) {
	}

	@Override
	public void visitFile(File file) {
		if (file.getName().endsWith(".class")) {
			System.out.println("Will clean class file: " + file);
		}
	}
}
