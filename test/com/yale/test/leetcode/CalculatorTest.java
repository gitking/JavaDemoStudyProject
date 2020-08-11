package com.yale.test.leetcode;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.yale.test.junit.Calculator;

/*
 * 在一个单元测试中，我们经常编写多个@Test方法，来分组、分类对目标代码进行测试。
 * 在测试的时候，我们经常遇到一个对象需要初始化，测试完可能还需要清理的情况。如果每个@Test方法都写一遍这样的重复代码，显然比较麻烦。
 * JUnit提供了编写测试前准备、测试后清理的固定代码，我们称之为Fixture。
 * 我们来看一个具体的Calculator的例子：
 * 这个类的功能很简单，但是测试的时候，我们要先初始化对象，我们不必在每个测试方法中都写上初始化代码，而是通过@BeforeEach来初始化，通过@AfterEach来清理资源：
 * 因此，我们总结出编写Fixture的套路如下：
 * 对于实例变量，在@BeforeEach中初始化，在@AfterEach中清理，它们在各个@Test方法中互不影响，因为是不同的实例；
 * 对于静态变量，在@BeforeAll中初始化，在@AfterAll中清理，它们在各个@Test方法中均是唯一实例，会影响各个@Test方法。
 * 大多数情况下，使用@BeforeEach和@AfterEach就足够了。只有某些测试资源初始化耗费时间太长，以至于我们不得不尽量“复用”时才会用到@BeforeAll和@AfterAll。
 * 最后，注意到每次运行一个@Test方法前，JUnit首先创建一个XxxTest实例，因此，每个@Test方法内部的成员变量都是独立的，不能也无法把成员变量的状态从一个@Test方法带到另一个@Test方法。
 */
public class CalculatorTest {
	Calculator calclatory;
	
	/*
	 * @Before会在每个Test方法运行之前自动运行
	 * 还有一些资源初始化和清理可能更加繁琐，而且会耗费较长的时间，例如初始化数据库。
	 * JUnit还提供了@BeforeAll和@AfterAll，它们在运行所有@Test前后运行，顺序如下：
	 * 因为@BeforeAll和@AfterAll在所有@Test方法运行前后仅运行一次，因此，它们只能初始化静态变量，例如：
	 * 事实上，@BeforeAll和@AfterAll也只能标注在静态方法上。
	 */
	@Before
	public void setUp() {
		this.calclatory = new Calculator();
	}
	
	/*
	 * @After会在每个Test方法运行之后自动运行
	 * 还有一些资源初始化和清理可能更加繁琐，而且会耗费较长的时间，例如初始化数据库。
	 * JUnit还提供了@BeforeAll和@AfterAll，它们在运行所有@Test前后运行，顺序如下：
	 */
	@After
	public void tearDown() {
		this.calclatory = null;
	}
	
	@Test
	public void testAdd() {
		assertEquals(100, this.calclatory.add(100));
		assertEquals(150, this.calclatory.add(50));
		assertEquals(130, this.calclatory.add(-20));
	}
	
	@Test
	public void testSub() {
		assertEquals(-100, this.calclatory.sub(100));
		assertEquals(-150, this.calclatory.sub(50));
		assertEquals(-130, this.calclatory.sub(-20));
	}
}
