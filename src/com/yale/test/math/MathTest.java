package com.yale.test.math;

import java.math.BigDecimal;
import java.util.Random;
/**
 * java.lang.Math(jdk1.0开始有的)类是整个JDK里面唯一一个与数学计算有关的程序类。这里面提供有一些基础的数学函数。
 * 【推荐】避免Random实例被多线程使用，虽然共享该实例是线程安全的，但会因竞争同一seed 导致的性能下降。
	说明：Random实例包括java.util.Random 的实例或者 Math.random()的方式。 正例：在JDK7之后，可以直接使用API ThreadLocalRandom，
	而在 JDK7之前，需要编码保证每个线程持有一个单独的Random实例。《阿里巴巴Java开发手册（泰山版）.pdf》
 * @author dell
 *
 */
public class MathTest {

	public static void main(String[] args) {
		
		System.out.println("自然对数的底数:" + Math.E);
		System.out.println("Math类中的常量圆周率π:" + Math.PI);
		
		System.out.println("Math.pow计算某个数的平方:" + Math.pow(10, 3));//10的3次方是1000
		
		System.out.println("max方法返回俩个数字最大的那一个:" + Math.max(18.44, 55));

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
		
		System.out.println("Random是随机数类");
		Random dom = new Random();
		for (int x=0;x<10; x++) {
			System.out.print(dom.nextInt(100) + ",");//100是上限,输出的随机最大值是99
		}
		
		/**
		 * Math.random()方法用的也是random生成随机数的,值范围是0-1,随机最大小数位0.9999999999
		 * 【强制】注意 Math.random() 这个方法返回是double类型，注意取值的范围 0≤x<1（能够取到零值，注意除零异常），
		 * 如果想获取整数类型的随机数，不要将x放大10的若干倍然后取整，直接使用Random对象的nextInt或者nextLong方法。
		 * 《阿里巴巴Java开发手册（泰山版）.pdf》
		 */
		System.out.print(Math.random() + ",");//100是上限,输出的随机最大值是99
	}
	
	/**
	 * round的四舍五入小数位精确的并不精准,所以我们自己写的四舍五入方法,javascript(js)就是用这种方法做四舍五入的,原理就是
	 * 10.568 * 1000  之后再除以 1000
	 * https://edu.aliyun.com/lesson_36_451#_451
	 * 阿里云  mldn  课时49 数字操作类（Math类）
	 * 但是真正开发中,坚决不要写,要使用BigDecimal进行四舍五入,这个是java官方的精确计算类
	 */
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
