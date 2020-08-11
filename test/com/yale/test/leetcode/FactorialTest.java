package com.yale.test.leetcode;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * TDD是测试驱动开发（Test-Driven Development）的英文简称，
 * 编写接口
     │
     ▼
    编写测试
     │
     ▼
┌─> 编写实现
│    │
│ N  ▼
└── 运行测试
     │ Y
     ▼
    任务完成
 * 这就是传说中的TDD
 * 什么是单元测试呢？单元测试就是针对最小的功能单元编写测试代码。Java程序最小的功能单元是方法，因此，对Java程序进行单元测试就是针对单个Java方法的测试。
 * 单元测试有什么好处呢？在学习单元测试前，我们可以先了解一下测试驱动开发。
 * 所谓测试驱动开发，是指先编写接口，紧接着编写测试。编写完测试后，我们才开始真正编写实现代码。在编写实现代码的过程中，一边写，一边测，什么时候测试全部通过了，那就表示编写的实现完成了：
 * JUnit
 * JUnit是一个开源的Java语言的单元测试框架，专门针对Java设计，使用最广泛。JUnit是事实上的单元测试的标准框架，任何Java开发者都应当学习并使用JUnit编写单元测试。
 * 使用JUnit编写单元测试的好处在于，我们可以非常简单地组织测试代码，并随时运行它们，JUnit就会给出成功的测试和失败的测试，还可以生成测试报告，不仅包含测试的成功率，还可以统计测试的代码覆盖率，即被测试的代码本身有多少经过了测试。对于高质量的代码来说，测试覆盖率应该在80%以上。
 * 此外，几乎所有的IDE工具都集成了JUnit，这样我们就可以直接在IDE中编写并运行JUnit测试。JUnit目前最新版本是5。
 * 以Eclipse为例，当我们已经编写了一个Factorial.java文件后，我们想对其进行测试，需要编写一个对应的FactorialTest.java文件，以Test为后缀是一个惯例，并分别将其放入src和test目录中。最后，在Project - Properties - Java Build Path - Libraries中添加JUnit 5的库：
 * 单元测试的好处
	单元测试可以确保单个方法按照正确预期运行，如果修改了某个方法的代码，只需确保其对应的单元测试通过，即可认为改动正确。此外，测试代码本身就可以作为示例代码，用来演示如何调用该方法。
	使用JUnit进行单元测试，我们可以使用断言（Assertion）来测试期望结果，可以方便地组织和运行测试，并方便地查看测试结果。此外，JUnit既可以直接在IDE中运行，也可以方便地集成到Maven这些自动化工具中运行。
	在编写单元测试的时候，我们要遵循一定的规范：
		一是单元测试代码本身必须非常简单，能一下看明白，决不能再为测试代码编写测试；
		二是每个单元测试应当互相独立，不依赖运行的顺序；
		三是测试时不但要覆盖常用测试用例，还要特别注意测试边界条件，例如输入为0，null，空字符串""等情况。
 * @author dell
 */
public class FactorialTest {
	@Test
	public void testFact() {
		/*
		 * 一个JUnit测试包含若干@Test方法，并使用Assertions进行断言，注意浮点数assertEquals()要指定delta。
		 * assertEquals(expected, actual)是最常用的测试方法，它在Assertion类中定义
		 * Assertion还定义了其他断言方法，例如：
	     * assertTrue(): 期待结果为true
	     * assertFalse(): 期待结果为false
	     * assertNotNull(): 期待结果为非null
	     * assertArrayEquals(): 期待结果为数组并与期望数组每个元素的值均相等
		 */
		assertEquals(1, Factorial.factorial(1));
		assertEquals(2, Factorial.factorial(2));
		assertEquals(6, Factorial.factorial(3));
		assertEquals(3628800, Factorial.factorial(10));
		assertEquals(2432902008176640000L, Factorial.factorial(20));
		//使用浮点数时，由于浮点数无法精确地进行比较，因此，我们需要调用assertEquals(double expected, double actual, double delta)这个重载方法，指定一个误差值：
		assertEquals(0.1, Math.abs(1 - 9 / 10.0), 0.0000001);
	}
}
