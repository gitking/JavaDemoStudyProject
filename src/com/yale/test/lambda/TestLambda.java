package com.yale.test.lambda;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class TestLambda {
	public static void main(String[] args) {
		
		/**
		 * 阿里云 魔乐科技 课时25 内建函数式接口
		 * Lambda实际上简化了方法引用,但是Lambda核心在于函数式接口,而函数式接口的核心在于只有一个方法,如果你细心去观察的话会发现,实际上
		 * 在函数式编程里面只需要有四类接口(java.util.function):
		 * 1、功能型函数式接口(方法有参数有返回值):public interface Function<T, R> {public R apply(T t)}
		 * 2、供给型函数式接口(方法无参有返回值): public interface Supplier<T> {public T get()}
		 * 3、消费型函数式接口(方法有参数没返回值):public interface Consumer<T>{public void accept(T t)}
		 * 4、断言型函数式接口(方法有参数有返回值):public inteface Predicate<T> {boolean test(T t)}
		 */
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
