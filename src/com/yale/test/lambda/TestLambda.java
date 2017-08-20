package com.yale.test.lambda;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class TestLambda {
	public static void main(String[] args) {
		
		Function<Integer, String> fun = String ::valueOf;//功能性函数
		System.out.println("这是功能性函数接口,JAVA提供的Lambda函数式编程接口,输入参数为Integer,返回参数为String,有输入参数也有输出参数." + fun.apply(1000));
		
		IntFunction<String> intFun = String :: valueOf;
		System.out.println("这是功能性函数接口,JAVA提供的Lambda函数式编程接口,IntFunction省略了输入参数,有输入参数也有输出参数." + intFun.apply(1000));
		
		Consumer<String> con = System.out :: println;
	    con.accept("这是一个消费型接口,只有输入参数,没有返回参数");
		
		Supplier<String> sup = "hello word" :: toUpperCase;
		System.out.println("这是一个供给型接口,只负责帮忙调用方法，只有输出参数,没有输入参数" + sup.get());
		
		Predicate<String> pre = "##Word" :: startsWith;
		System.out.println("这是一个断言型函数接口,有输入参数，也有输出参数,并返回值是true或者false." + pre.test("##"));
	}
}
