package com.yale.test.math;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * 大数类BigInteger
 * 可以存储理论无限大[计算机无理限制]的整数
 * 数值存储是二进制补码,大端模式排序
 * 可以使用不同进制,进行基本运算,进行位运算
 * volatile解决多线程内存不可见问题。对于一写多读，是可以解决变量同步问题，但是如果多写，同样无法解决线程安全问题。
	说明：如果是count++操作，使用如下类实现：AtomicInteger count = new AtomicInteger(); count.addAndGet(1); 
	如果是JDK8，推荐使用LongAdder对象，比AtomicLong性能更好（减少乐观锁的重试次数）。《阿里巴巴Java开发手册（泰山版）.
	在Java中，由CPU原生提供的整型最大范围是64位long型整数。使用long型整数可以直接通过CPU指令进行计算，速度非常快。
	和long型整数运算比，BigInteger不会有范围限制，但缺点是速度比较慢。
 * 大端小端可以参考阮一峰写的文章[字符编码笔记：ASCII，Unicode 和 UTF-8]:http://www.ruanyifeng.com/blog/2007/10/ascii_unicode_and_utf-8.html
 * 七、Little endian 和 Big endian
 * 上一节已经提到，UCS-2 格式可以存储 Unicode 码（码点不超过0xFFFF）。以汉字严为例，Unicode 码是4E25，需要用两个字节存储，一个字节是4E，另一个字节是25。存储的时候，4E在前，25在后，这就是 Big endian 方式；25在前，4E在后，这是 Little endian 方式。
 * 这两个古怪的名称来自英国作家斯威夫特的《格列佛游记》。在该书中，小人国里爆发了内战，战争起因是人们争论，吃鸡蛋时究竟是从大头(Big-endian)敲开还是从小头(Little-endian)敲开。为了这件事情，前后爆发了六次战争，一个皇帝送了命，另一个皇帝丢了王位。
 * 第一个字节在前，就是"大头方式"（Big endian），第二个字节在前就是"小头方式"（Little endian）。
 * 那么很自然的，就会出现一个问题：计算机怎么知道某一个文件到底采用哪一种方式编码？
 * Unicode 规范定义，每一个文件的最前面分别加入一个表示编码顺序的字符，这个字符的名字叫做"零宽度非换行空格"（zero width no-break space），用FEFF表示。这正好是两个字节，而且FF比FE大1。
 * 如果一个文本文件的头两个字节是FE FF，就表示该文件采用大头方式；如果头两个字节是FF FE，就表示该文件采用小头方式。
 * @author dell
 */
public class BigIntegerTest {

	public static void main(String[] args) {
		BigInteger b1 = new BigInteger("29");
		//将二进制字符串数换为10进制的BigInteger
		BigInteger b2 = new BigInteger("1010", 2);
		BigInteger b3 = BigInteger.valueOf(33L);
		
		System.out.println(b1.toString());
		System.out.println(b2.toString());
		System.out.println("将b3以二进制的形式输出:" + b3.toString(2));
		
		BigInteger add = b1.add(b2);
		BigInteger sub = b1.subtract(b2);
		BigInteger mul = b1.multiply(b2);
		BigInteger div = b1.divide(b2);//除法,整数相除余数被舍弃掉
		System.out.println(add);
		System.out.println(sub);
		System.out.println(mul);
		System.out.println(div);
		
		BigInteger remainder = b1.remainder(b2);//取余
		System.out.println("余数:" + remainder);
		
		BigInteger[] divideAndRemainder = b1.divideAndRemainder(b2);//同时获取商和余数
		for (int i=0; i<divideAndRemainder.length;i++) {
			if (i == 0) {
				System.out.print("20/19 商:" + divideAndRemainder[i]);
			} else {
				System.out.println(",余" + divideAndRemainder[i]);
			}
		}
		
		int res = b1.compareTo(b2);
		System.out.println("b1和b2比较,返回-1(小于) 0(等于) 1(大于):" + res);
		
		BigInteger bi = new BigInteger("1234567890");
		System.out.println("pow:" + bi.pow(5)); // 2867971860299718107233761438093672048294900000
		System.out.println("也可以把BigInteger转换成long型：" + bi.longValue());
		//使用longValueExact()方法时，如果超出了long型的范围，会抛出ArithmeticException。
		System.out.println("也可以把BigInteger转换成long型：" + bi.longValueExact());
		/*
		 * BigInteger和Integer、Long一样，也是不可变类，并且也继承自Number类。因为Number定义了转换为基本类型的几个方法：
			    转换为byte：byteValue()
			    转换为short：shortValue()
			    转换为int：intValue()
			    转换为long：longValue()
			    转换为float：floatValue()
			    转换为double：doubleValue()
			    因此，通过上述方法，可以把BigInteger转换成基本类型。如果BigInteger表示的范围超过了基本类型的范围，转换时将丢失高位信息，即结果不一定是准确的。
			    如果需要准确地转换成基本类型，可以使用intValueExact()、longValueExact()等方法，在转换时如果超出范围，将直接抛出ArithmeticException异常。
		 */
		
		BigInteger n = new BigInteger("999999").pow(99);
        float f = n.floatValue();
        System.out.println("如果BigInteger的值甚至超过了float的最大范围（3.4x1038），那么返回的float是什么呢？" + f);
        System.out.println("如果BigInteger的值甚至超过了float的最大范围（3.4x1038），那么返回的float是什么呢？" + Float.isInfinite(f));

		
		BigInteger big01 = new BigInteger("100");
		System.out.println("BigInteger的&按位与运算:" + big01.and(new BigInteger("1")));
		System.out.println("BigInteger的|按位或运算:" + big01.or(new BigInteger("1")));
		System.out.println("BigInteger的^按位异域运算:" + big01.xor(new BigInteger("1")));
		System.out.println("BigInteger的~按位取反运算:" + big01.not());
		System.out.println("BigInteger的andNot方法不知道啥意思:" + big01.andNot(new BigInteger("1")));

		System.out.println("BigInteger的<<左移运算:" + big01.shiftLeft(1));
		System.out.println("BigInteger的>>右移运算:" + big01.shiftRight(1));
		//要特别注意，Integer有个getInteger(String)方法，它不是将字符串转换为int，而是把该字符串对应的系统变量转换为Integer：
		System.out.println("获取java系统版本号:" + Integer.getInteger("java.version")); // 版本号，11
		
		
		try {
			//构造一个随机生成的正 BigInteger，它可能是质数，具有指定的 bitLength。 数(质数)是指在大于1的自然数中，除了1和它本身以外不再有其他因数的自然数。
			//所以这个构造方法BigInteger的第一个参数bitLength必须大于等于2,否则会报错java.lang.ArithmeticException: bitLength < 2
			BigInteger pn = new BigInteger(99, 2, SecureRandom.getInstanceStrong());
			System.out.println("生成一个随机素数(质数):" + pn.intValue());
			
			//返回一个可能是质数的正 BigInteger，具有指定的 bitLength。 此方法返回的 BigInteger 为复合的概率不超过 2<sup>-100</sup>。 
			//probablePrime这个方法跟BigInteger(99, 2, SecureRandom.getInstanceStrong());这个构造方法差不多是一个意思
			//这个知识点来自《Effective Java中文版》第2版,第4页。静态工厂方法与构造器不同的第一大优势在于,他们有名称。
			BigInteger primeNumber = BigInteger.probablePrime(99, SecureRandom.getInstanceStrong());
			System.out.println("生成一个随机素数(质数):" + primeNumber);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
	}
}
