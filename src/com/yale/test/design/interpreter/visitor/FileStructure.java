package com.yale.test.design.interpreter.visitor;

import java.io.File;

public class FileStructure {
	private File path;//持有数据
	
	public FileStructure(File path) {
		this.path = path;
	}
	
	public void handle(Visitor visitor) {//Visitor接口处理数据
		scan(path, visitor);
	}
	
	private void scan(File file, Visitor visitor) {
		if (file.isDirectory()) {
			visitor.visitDir(file);
			for (File sub: file.listFiles()) {
				scan(sub, visitor);
			}
		} else if (file.isFile()) {
			visitor.visitFile(file);
		}
	}
}
