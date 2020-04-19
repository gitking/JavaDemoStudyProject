package com.yale.test.lambda;
//@FunctionalInterface标注的接口只能有一个方法,FunctionalInterface代表是函数式接口,
//那意味着你可以使用Lambda表达式,同时也意味着这个接口里面只能有一个方法
@FunctionalInterface
public interface IMath {
	int add(int x, int y);
}
