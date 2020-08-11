package com.yale.test.leetcode;

import static org.junit.Assert.assertEquals;

import java.util.List;

/**
 * 如果待测试的输入和输出是一组数据： 可以把测试数据组织起来 用不同的测试数据调用相同的测试方法
 * 参数化测试和普通测试稍微不同的地方在于，一个测试方法需要接收至少一个参数，然后，传入一组参数反复运行。
 * JUnit提供了一个@ParameterizedTest注解，用来进行参数化测试。
 * 假设我们想对Math.abs()进行测试，先用一组正数进行测试：
 * 注意到参数化测试的注解是@ParameterizedTest，而不是普通的@Test。
 * @author dell
 */
public class ParameterizedTest {
	//@ParameterizedTest
	//@ValueSource(ints = { 0, 1, 5, 100 })
	void testAbs(int x) {
	    assertEquals(x, Math.abs(x));
	}
	//再用一组负数进行测试：
	//@ParameterizedTest
	//@ValueSource(ints = { -1, -5, -100 })
	void testAbsNegative(int x) {
	    assertEquals(-x, Math.abs(x));
	}
	
	/*
	 * 实际的测试场景往往没有这么简单。假设我们自己编写了一个StringUtils.capitalize()方法，它会把字符串的第一个字母变为大写，后续字母变为小写：
	 * 要用参数化测试的方法来测试，我们不但要给出输入，还要给出预期输出。因此，测试方法至少需要接收两个参数：
	 * 现在问题来了：参数如何传入？
	 * 最简单的方法是通过@MethodSource注解，它允许我们编写一个同名的静态方法来提供测试参数：
	 */
	//@ParameterizedTest
	void testCapitalize(String input, String result) {
	   // assertEquals(result, StringUtils.capitalize(input));
	}
	
	
	//@ParameterizedTest
	//@MethodSource
	void testCapitalize1(String input, String result) {
	    //assertEquals(result, StringUtils.capitalize(input));
	}

	/*
	 * 下面的代码很容易理解：静态方法testCapitalize()返回了一组测试参数，每个参数都包含两个String，正好作为测试方法的两个参数传入。
	 *  如果静态方法和测试方法的名称不同，@MethodSource也允许指定方法名。但使用默认同名方法最方便。 
	 */
//	static List<Arguments> testCapitalize() {
//	    return List.of( // arguments:
//	            Arguments.arguments("abc", "Abc"), //
//	            Arguments.arguments("APPLE", "Apple"), //
//	            Arguments.arguments("gooD", "Good"));
//	}
	/*
	 * 另一种传入测试参数的方法是使用@CsvSource，它的每一个字符串表示一行，一行包含的若干参数用,分隔，因此，上述测试又可以改写如下：
	 * 如果有成百上千的测试输入，那么，直接写@CsvSource就很不方便。这个时候，我们可以把测试数据提到一个独立的CSV文件中，然后标注上@CsvFileSource：
	 */
	//@ParameterizedTest
	//@CsvSource({ "abc, Abc", "APPLE, Apple", "gooD, Good" })
	void testCapitalize2(String input, String result) {
	    //assertEquals(result, StringUtils.capitalize(input));
	}
	
	/*
	 * JUnit只在classpath中查找指定的CSV文件，因此，test-capitalize.csv这个文件要放到test目录下，内容如下：
	 * apple, Apple
		HELLO, Hello
		JUnit, Junit
		reSource, Resource
	 */
	//@ParameterizedTest
	//@CsvFileSource(resources = { "/test-capitalize.csv" })
	void testCapitalizeUsingCsvFile(String input, String result) {
	    //assertEquals(result, StringUtils.capitalize(input));
	}
}
