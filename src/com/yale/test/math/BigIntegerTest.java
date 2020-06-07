package com.yale.test.math;

import java.math.BigInteger;

/**
 * 大数类BigInteger
 * 可以存储理论无限大[计算机无理限制]的整数
 * 数值存储是二进制补码,大端模式排序
 * 可以使用不同进制,进行基本运算,进行位运算
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
		
		BigInteger big01 = new BigInteger("100");
		System.out.println("BigInteger的&按位与运算:" + big01.and(new BigInteger("1")));
		System.out.println("BigInteger的|按位或运算:" + big01.or(new BigInteger("1")));
		System.out.println("BigInteger的^按位异域运算:" + big01.xor(new BigInteger("1")));
		System.out.println("BigInteger的~按位取反运算:" + big01.not());
		System.out.println("BigInteger的andNot方法不知道啥意思:" + big01.andNot(new BigInteger("1")));

		System.out.println("BigInteger的<<左移运算:" + big01.shiftLeft(1));
		System.out.println("BigInteger的>>右移运算:" + big01.shiftRight(1));
		
	}
}
