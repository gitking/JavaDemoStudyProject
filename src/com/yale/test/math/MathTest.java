package com.yale.test.math;

import java.math.BigDecimal;

public class MathTest {

	public static void main(String[] args) {
		System.out.println(Math.round(18.44));
		System.out.println(Math.round(18.49));
		System.out.println(Math.round(18.51));
		System.out.println(Math.round(18.5));
		System.out.println(Math.round(-18.5));//round针对于负数,只要小数位不大于0.5都不做进位处理,这行代码的结果为-18
		System.out.println(Math.round(-18.51));
		
		System.out.println(MathTest.myRound(-18.555, 2));//这个方法负数依然不能正确得到四舍五入的值,这行代码的结果为-18.55
		
		//注意myRoundSec跟myRoundThi的方法实现,同样的数字计算出来的结果是不一样的
		System.out.println(MathTest.myRoundSec(-18.555, 2));//这个方法负数依然不能正确得到四舍五入的值,这行代码的结果为-18.55
		System.out.println(MathTest.myRoundThi(-18.555, 2));//这个方法负数依然不能正确得到四舍五入的值,这行代码的结果为-18.55

	}
	
	public static double myRound(double num, int scale) {
		return Math.round(num * Math.pow(10, scale)) / Math.pow(10, scale);
	}
	
	public static double myRoundSec(double num, int scale) {
		BigDecimal big = new BigDecimal(Double.toString(num));
		BigDecimal other = new BigDecimal(Double.toString(1));
		System.out.println("myRoundSec:" + big.doubleValue());
		System.out.println("myRoundSec:" + other.doubleValue());
		return big.divide(other, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	public static double myRoundThi(double num, int scale) {
		BigDecimal big = new BigDecimal(num);
		BigDecimal other = new BigDecimal(1);
		System.out.println("myRoundThi:" + big.doubleValue());
		System.out.println("myRoundThi:" + other.doubleValue());
		return big.divide(other, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
}
