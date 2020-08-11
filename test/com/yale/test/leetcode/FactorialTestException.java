package com.yale.test.leetcode;

import static org.junit.Assert.assertEquals;

import org.apache.tomcat.jni.OS;
import org.junit.Test;

import com.yale.test.junit.Config;

/**
 * 在Java程序中，异常处理是非常重要的。
 * 我们自己编写的方法，也经常抛出各种异常。对于可能抛出的异常进行测试，本身就是测试的重要环节。
 * 因此，在编写JUnit测试的时候，除了正常的输入输出，我们还要特别针对可能导致异常的情况进行测试。
 * 我们仍然用Factorial举例：
 * @author dell
 */
public class FactorialTestException {
	@Test
	public void testNegative() {
		/**
		 * assertThrows 是JUnit5才有的功能
		 * JUnit提供assertThrows()来期望捕获一个指定的异常。第二个参数Executable封装了我们要执行的会产生异常的代码。当我们执行Factorial.fact(-1)时，
		 * 必定抛出IllegalArgumentException。assertThrows()在捕获到指定异常时表示通过测试，未捕获到异常，或者捕获到的异常类型不对，均表示测试失败。
		 */
//		assertThrows(IllegalArgumentException.class, new Executable(){
//			@Override
//			public void execute() throws Throwable {
//
//			}
//		});
	}
	
	@Test
	public void testNegative1() {
		/**
		 * JUnit提供assertThrows()来期望捕获一个指定的异常。第二个参数Executable封装了我们要执行的会产生异常的代码。当我们执行Factorial.fact(-1)时，
		 * 必定抛出IllegalArgumentException。assertThrows()在捕获到指定异常时表示通过测试，未捕获到异常，或者捕获到的异常类型不对，均表示测试失败。
		 * 有些童鞋会觉得编写一个Executable的匿名类太繁琐了。实际上，Java 8开始引入了函数式编程，所有单方法接口都可以简写如下：
		 */
//		assertThrows(IllegalArgumentException.class, () -> {
//	        Factorial.fact(-1);
//	    });
	}
	/*
	 * 在运行测试的时候，有些时候，我们需要排出某些@Test方法，不要让它运行，这时，我们就可以给它标记一个@Disabled：
	 * 为什么我们不直接注释掉@Test，而是要加一个@Disabled？这是因为注释掉@Test，JUnit就不知道这是个测试方法，
	 * 而加上@Disabled，JUnit仍然识别出这是个测试方法，只是暂时不运行。它会在测试结果中显示：
	 * Tests run: 68, Failures: 2, Errors: 0, Skipped: 5
	 * 类似@Disabled这种注解就称为条件测试，JUnit根据不同的条件注解，决定是否运行当前的@Test方法。
	 */
	//@Disabled Junit5才有
	@Test
	public void testBug101() {
		
	}
	
	/*
	 * 我们想要测试getConfigFile()这个方法，但是在Windows上跑，和在Linux上跑的代码路径不同，因此，针对两个系统的测试方法，其中一个只能在Windows上跑，另一个只能在Mac/Linux上跑：
	 * 因此，我们给上述两个测试方法分别加上条件如下：
	 * @EnableOnOs就是一个条件测试判断。
	 * 不在Windows平台执行的测试，可以加上@DisabledOnOs(OS.WINDOWS)：
	 * 只能在Java 9或更高版本执行的测试，可以加上@DisabledOnJre(JRE.JAVA_8)：
	 * 只能在64位操作系统上执行的测试，可以用@EnabledIfSystemProperty判断：
	 * 需要传入环境变量DEBUG=true才能执行的测试，可以用@EnabledIfEnvironmentVariable：
	 * 当我们在JUnit中运行所有测试的时候，JUnit会给出执行的结果。在IDE中，我们能很容易地看到没有执行的测试：
	 * 带有⊘标记的测试方法表示没有执行。
	 */
	@Test
	//@EnabledOnOs(OS.WINDOWS)Junit5才有
	//@DisabledOnOs(OS.WINDOWS)
	//@DisabledOnJre(JRE.JAVA_8)
	//@EnabledIfSystemProperty(named = "os.arch", matches = ".*64.*")
	//@EnabledIfEnvironmentVariable(named = "DEBUG", matches = "true")
	public void testWindows() {
	    assertEquals("C:\\test.ini", Config.getConfigFile("test.ini"));
	}
	
	@Test
	//@EnabledOnOs({ OS.LINUX, OS.MAC })Junit5才有
	void testLinuxAndMac() {
	    assertEquals("/usr/local/test.cfg", Config.getConfigFile("test.cfg"));
	}
}
