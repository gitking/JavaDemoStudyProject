package com.yale.test.lambda;

import java.util.Arrays;

/*
 * 函数式编程
 * 本章我们介绍Java的函数式编程。
 * 我们先看看什么是函数。函数是一种最基本的任务，一个大型程序就是一个顶层函数调用若干底层函数，这些被调用的函数又可以调用其他函数，即大任务被一层层拆解并执行。所以函数就是面向过程的程序设计的基本单元。
 * Java不支持单独定义函数，但可以把静态方法视为独立的函数，把实例方法视为自带this参数的函数。
 * 而函数式编程（请注意多了一个“式”字）——Functional Programming，虽然也可以归结到面向过程的程序设计，但其思想更接近数学计算。
 * 我们首先要搞明白计算机（Computer）和计算（Compute）的概念。
 * 在计算机的层次上，CPU执行的是加减乘除的指令代码，以及各种条件判断和跳转指令，所以，汇编语言是最贴近计算机的语言。
 * 而计算则指数学意义上的计算，越是抽象的计算，离计算机硬件越远。
 * 对应到编程语言，就是越低级的语言，越贴近计算机，抽象程度低，执行效率高，比如C语言；越高级的语言，越贴近计算，抽象程度高，执行效率低，比如Lisp语言。
 * 函数式编程就是一种抽象程度很高的编程范式，纯粹的函数式编程语言编写的函数没有变量，因此，任意一个函数，只要输入是确定的，输出就是确定的，这种纯函数我们称之为没有副作用。而允许使用变量的程序设计语言，由于函数内部的变量状态不确定，同样的输入，可能得到不同的输出，因此，这种函数是有副作用的。
 * 函数式编程的一个特点就是，允许把函数本身作为参数传入另一个函数，还允许返回一个函数！
 * 函数式编程最早是数学家阿隆佐·邱奇研究的一套函数变换逻辑，又称Lambda Calculus（λ-Calculus），所以也经常把函数式编程称为Lambda计算。
 * Java平台从Java 8开始，支持函数式编程。
 * Lambda基础
 * 在了解Lambda之前，我们先回顾一下Java的方法。
 * Java的方法分为实例方法，例如Integer定义的equals()方法：
 * 以及静态方法，例如Integer定义的parseInt()方法：
 * 无论是实例方法，还是静态方法，本质上都相当于过程式语言的函数。例如C函数：
 * 只不过Java的实例方法隐含地传入了一个this变量，即实例方法总是有一个隐含参数this。
 * 函数式编程（Functional Programming）是把函数作为基本运算单元，函数可以作为变量，可以接收函数，还可以返回函数。历史上研究函数式编程的理论是Lambda演算，所以我们经常把支持函数式编程的编码风格称为Lambda表达式。
 * Lambda表达式
 * 在Java程序中，我们经常遇到一大堆单方法接口，即一个接口只定义了一个方法：
 * Comparator
 * Runnable
 * Callable
 * 以Comparator为例，我们想要调用Arrays.sort()时，可以传入一个Comparator实例，以匿名类方式编写如下：
 * String[] array = ...
 * Arrays.sort(array, new Comparator<String>() {
    public int compare(String s1, String s2) {
        return s1.compareTo(s2);
    }
});
上述写法非常繁琐。从Java 8开始，我们可以用Lambda表达式替换单方法接口。改写上述代码如下：
 */
public class FunctionalProgramming {
	public static void main(String[] args) {
		String[] array = new String[]{"Apple", "Orange", "Banana", "Lemon"};
		Arrays.sort(array, (s1, s2)->{
			return s1.compareTo(s2);
		});
		System.out.println(String.join(",", array));
		/*
		 * 观察Lambda表达式的写法，它只需要写出方法定义：
		 * (s1, s2) -> {
			    return s1.compareTo(s2);
			}
		 * 其中，参数是(s1, s2)，参数类型可以省略，因为编译器可以自动推断出String类型。-> { ... }表示方法体，所有代码写在内部即可。
		 * Lambda表达式没有class定义，因此写法非常简洁。如果只有一行return xxx的代码，完全可以用更简单的写法：
		 * Arrays.sort(array, (s1, s2) -> s1.compareTo(s2));
		 * 返回值的类型也是由编译器自动推断的，这里推断出的返回值是int，因此，只要返回int，编译器就不会报错。
		 */
		String[] array1 = new String[]{"Apple", "Orange", "Banana", "Lemon"};
		Arrays.sort(array1, (s1, s2) -> s1.compareTo(s2));
		System.out.println(String.join(",", array1));
		/*
		 * FunctionalInterface
		 * 我们把只定义了单方法的接口称之为FunctionalInterface，用注解@FunctionalInterface标记。例如，Callable接口：
		 * @FunctionalInterface
			public interface Callable<V> {
			    V call() throws Exception;
			}
		 * 再来看Comparator接口：
		 * @FunctionalInterface
			public interface Comparator<T> {
			
			    int compare(T o1, T o2);
			
			    boolean equals(Object obj);
			
			    default Comparator<T> reversed() {
			        return Collections.reverseOrder(this);
			    }
			
			    default Comparator<T> thenComparing(Comparator<? super T> other) {
			        ...
			    }
			    ...
			}
		 * 虽然Comparator接口有很多方法，但只有一个抽象方法int compare(T o1, T o2)，其他的方法都是default方法或static方法。另外注意到boolean equals(Object obj)是Object定义的方法，不算在接口方法内。因此，Comparator也是一个FunctionalInterface。
		 * 小结
		 * 单方法接口被称为FunctionalInterface。
		 * 接收FunctionalInterface作为参数的时候，可以把实例化的匿名类改写为Lambda表达式，能大大简化代码。
		 * Lambda表达式的参数和返回值均可由编译器自动推断。
		 */
		
		/**
		 * 方法引用
		 * 使用Lambda表达式，我们就可以不必编写FunctionalInterface接口的实现类，从而简化代码：
		 * 实际上，除了Lambda表达式，我们还可以直接传入方法引用。例如：
		 */
	}
}
