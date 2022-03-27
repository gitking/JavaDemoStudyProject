package com.yale.test.java.exception;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * https://www.heapdump.cn/article/2573808 太扯了！异常信息突然就没了。
 * https://www.heapdump.cn/article/2624569 获取异常信息里再出异常就找不到日志了，我人傻了
 * https://www.heapdump.cn/article/2615303 卷向字节码-Java异常到底是怎么被处理的？
 * https://mp.weixin.qq.com/s/2A_jhF4a31t8_us8v1zbsQ  神了！异常信息突然就没了？
 * https://mp.weixin.qq.com/s/aOlScg5sfQ4JvHzcRFqeyQ  关于多线程中抛异常的这个面试题我再说最后一次！
 * 我们需要铭记一点，如果 finally 代码块中有 return 语句，那么程序会优先返回 finally 块中 return 的结果。
 * Throw early，Catch late关于异常处理，有一个非常著名的原则叫做：Throw early，Catch late。
 * 捕获具体的异常，而不是它的父类
 * https://mp.weixin.qq.com/s?__biz=MzU2NzAzMjQyOA==&mid=2247483988&idx=1&sn=5b34b96a5312f2687dd28bce39d542f4&chksm=fca22d57cbd5a441db81097d265556c18cdb386e1ea456e607e78f9a629a4ce9033947d19721&scene=21#wechat_redirect
 * Throw early，Catch late
 * https://howtodoinjava.com/best-practices/java-exception-handling-best-practices/
 * https://www.heapdump.cn/article/2573808 太扯了！异常信息突然就没了。
 * https://www.heapdump.cn/article/2624569 获取异常信息里再出异常就找不到日志了，我人傻了
 * https://www.heapdump.cn/article/2615303 卷向字节码-Java异常到底是怎么被处理的？
 * 5. 【强制】事务场景中，抛出异常被catch后，如果需要回滚，一定要注意手动回滚事务。
 * 6. 【强制】finally块必须对资源对象、流对象进行关闭，有异常也要做try-catch。 说明：如果JDK7及以上，可以使用try-with-resources方式。
 * 7. 【强制】不要在finally块中使用return。 说明：try块中的return语句执行成功后，并不马上返回，而是继续执行finally块中的语句，如果此处存在return语句，则在此直接返回，无情丢弃掉try块中的返回点。
 * 《阿里巴巴Java开发手册嵩山版2020.pdf》
 * @author issuser
 */
public class FinallyReturnDemo {
	
	public static int result;
	
	public int num;
	
	public static String testTryCatch() {
		String name = "";
		try {
			name = "try";
			int res = 1 / 0;
		} catch(Exception e){
			e.printStackTrace();
			name = "catch";
			System.out.println("trycatchfinally是怎么走的？01" + name);
			return name;
		} finally {
			name = "finally";
			System.out.println("trycatchfinally是怎么走的？02" + name);
			/*
			 * 在 finally 块中写 return 语句是一种非常不好的实践，因为程序会将 try-catch 块里面的语句，或者是抛出的异常全部丢弃掉。
			 * 如上面的代码，int res = 1 / 0; 会抛出一个 ArithmeticException ，该异常被 catch 捕获处理，我们的本意是，在 catch 块中将异常处理并返回，但是由于示例一 finally 块中有 return 语句，导致 catch 块的返回值被丢弃。
			 * 我们需要铭记一点，如果 finally 代码块中有 return 语句，那么程序会优先返回 finally 块中 return 的结果。
			 * 为了避免这样的事情发生，我们应该避免在 finally 块中使用 return 语句。
			 */
			return name;
		}
		//在finally里面写了return,下面行代码就永远走不到了,就会报错
		//System.out.println("trycatchfinally是怎么走的？03" + name);
	}
	
	public static void testTryCatchFinally() {
		try {
			int res = 1 / 0;
		} finally {
			System.out.println("在发生异常的时候,会输出本语句,并且抛出异常");
		}
	}
	
	
	
	
	public static void testTryCatchFinallyReturn() {
		try {
			int res = 1 / 0;
		} finally {
			System.out.println("在finally里面写return回导致异常抛不出去,所以,傻逼不要再finally里面写return关键字");
			System.out.println("*****注意观察此方法有没有抛出异常*****");
			return;
		}
		//在finally里面写了return,下面行代码就永远走不到了,就会报错
		//System.out.println("trycatchfinally是怎么走的？03" + name);
	}
	
	@SuppressWarnings("finally")//压制finally里面的黄色警告信息
	public static void testTryCatchFinallyReturn01() {
		try {//https://www.heapdump.cn/article/2615303
			File file = new File("sd");
			InputStream in = new FileInputStream(file);//FileNotFoundException
			/*
			 * read()这个方法是会抛出IOException的,正常来说你是必须catch或者继续往外抛出这个异常的.
			 * 但是如果你finally里面写了return这个关键字,编译器直接就不提示你catch或者继续往外抛出这个异常了,
			 * 因为finally里面写了return之后异常就抛不出去了.
			 */
			in.read();
		} finally {
			System.out.println("在finally里面写return回导致异常抛不出去,所以,傻逼不要再finally里面写return关键字");
			System.out.println("*****注意观察此方法有没有抛出异常*****");
			return;
		}
		//在finally里面写了return,下面行代码就永远走不到了,就会报错
		//System.out.println("trycatchfinally是怎么走的？03" + name);
	}
	
	private static int x = 0;

	//反例：《阿里巴巴Java开发手册嵩山版2020.pdf》
	public static int checkReturn() {
		try {
			// x等于1，此处不返回
			return ++x;
		} finally {
			// 返回的结果是2
			return ++x;
		}
	}
	
	public static String testTryCatch02() {
		String name = "";
		try {
			name = "try";
			int res = 1 / 0;
		} catch(Exception e){
			e.printStackTrace();
			name = "catch";
			System.out.println("trycatchfinally是怎么走的？testTryCatch02--001" + name);
			return name;
		} finally {
			name = "finally";
			System.out.println("trycatchfinally是怎么走的？testTryCatch02--002" + name);
		}
		//下面这个代码也不会走了,因为才catch里面走完在return之前会把finally里面的代码执行掉,finally执行之后就直接return了,下面这行代码不会输出
		System.out.println("trycatchfinally是怎么走的？testTryCatch02--003" + name);
		return name;
	}
	
	
	public static String testTryCatch03() {
		String name = "";
		try {
			name = "try";
			int res = 1 / 0;
		} catch(Exception e){
			e.printStackTrace();
			name = "catch";
			System.out.println("trycatchfinally是怎么走的？testTryCatch03--001" + name);
		} finally {
			name = "finally";
			System.out.println("trycatchfinally是怎么走的？testTryCatch03--002" + name);
		}
		System.out.println("trycatchfinally是怎么走的？testTryCatch03--003" + name);
		return name;
	}
	
	
	public static void testTryCatch04() {
		try {
			FinallyReturnDemo.result = 0;
			int res = 1 / 0;
		} catch(Exception e){
			e.printStackTrace();
			FinallyReturnDemo.result = 1;
			System.out.println("trycatchfinally是怎么走的？testTryCatch04--001" + FinallyReturnDemo.result);
			return;
		} finally {
			FinallyReturnDemo.result = 2;
			System.out.println("trycatchfinally是怎么走的？testTryCatch04--002" + FinallyReturnDemo.result);
		}
		FinallyReturnDemo.result = 3;
		//下面这个代码也不会走了,因为才catch里面走完在return之前会把finally里面的代码执行掉,finally执行之后就直接return了,下面这行代码不会输出
		System.out.println("trycatchfinally是怎么走的？testTryCatch04--003" + FinallyReturnDemo.result);
		return;
	}
	
	
	public static FinallyReturnDemo testTryCatch05() {
		FinallyReturnDemo demo = new FinallyReturnDemo();
		try {
			demo.num = 0;
			int res = 1 / 0;
		} catch(Exception e){
			e.printStackTrace();
			demo.num = 1;
			System.out.println("trycatchfinally是怎么走的？testTryCatch05--001" + demo.num);
			return demo;
		} finally {
			demo.num = 2;
			System.out.println("trycatchfinally是怎么走的？testTryCatch05--002" + demo.num);
		}
		demo.num = 3;
		//下面这个代码也不会走了,因为才catch里面走完在return之前会把finally里面的代码执行掉,finally执行之后就直接return了,下面这行代码不会输出
		System.out.println("trycatchfinally是怎么走的？testTryCatch05--003" + demo.num);
		return demo;
	}
	
	public static FinallyReturnDemo testTryCatch06() {
		FinallyReturnDemo demo = new FinallyReturnDemo();
		try {
			demo.num = 0;
			int res = 1 / 0;
		} catch(Exception e){
			e.printStackTrace();
			demo.num = 1;
			System.out.println("trycatchfinally是怎么走的？testTryCatch06--001" + demo.num + "对象的hashcode:" + demo);
			return demo;
		} finally {
			System.out.println("trycatchfinally是怎么走的？testTryCatch06--002" + demo.num + "对象的hashcode:" + demo);
			demo = new FinallyReturnDemo();
			demo.num = 99;
			System.out.println("trycatchfinally是怎么走的？testTryCatch06--003" + demo.num + "对象的hashcode:" + demo);
		}
		demo.num = 3;
		//下面这个代码也不会走了,因为才catch里面走完在return之前会把finally里面的代码执行掉,finally执行之后就直接return了,下面这行代码不会输出
		System.out.println("trycatchfinally是怎么走的？testTryCatch06--004" + demo.num + "对象的hashcode:" + demo);
		return demo;
	}
	
	public static void main(String[] args) {
		System.out.println("trycatchfinally是怎么走的？result:----------->" + testTryCatch());
		System.out.println("trycatchfinally是怎么走的？result02:--------->" + testTryCatch02());
		System.out.println("trycatchfinally是怎么走的？result03:--------->" + testTryCatch03());
		testTryCatch04();
		System.out.println("结果是多少04--------------->" + FinallyReturnDemo.result);
		FinallyReturnDemo demo = testTryCatch05();
		System.out.println("结果是多少05--------------->" + demo.num);
		
		FinallyReturnDemo demo06 = testTryCatch06();
		System.out.println("结果是多少06--------------->" + demo06.num + "对象的hashcode:" + demo06);
		System.out.println("期望返回1，实际返回2,用错return了:《阿里巴巴Java开发手册嵩山版2020.pdf》" + checkReturn());

		testTryCatchFinallyReturn();
		testTryCatchFinallyReturn01();
		testTryCatchFinally();
		
	}
}
