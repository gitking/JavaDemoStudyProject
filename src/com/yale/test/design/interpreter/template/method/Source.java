package com.yale.test.design.interpreter.template.method;

//像桥接那一章创建引擎类那样创建Source类
public interface Source {
	public String read(String key);
}
