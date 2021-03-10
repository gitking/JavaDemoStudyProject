package com.yale.test.demo.javatezhongbin;

/*
 * 变量a,b交换有几种方式
 */
public class ChangeTwoNum {
	public static void main(String[] args) {
		//第一种,定义一个中间变量C来实现
		int a = 10;
		int b =11;
		System.out.println("交换前:a的值为:" + a + ",b的值为:" + b);
		int c = a;
		a = b;
		b = c;
		System.out.println("交换后:a的值为:" + a + ",b的值为:" + b);
		System.out.println("*********************************************");
		//第二种方式,数据叠加后再减回来的方法,注意这个方法可能会发生值越界的情况,比如a等于int的最大值,然后再加1就越界了
		a = 10;
		b =11;
		System.out.println("交换前:a的值为:" + a + ",b的值为:" + b);
		a = a + b;//a=21
		b = a - b;//b=10
		a = a - b;//a=11,注意这个时候b已经变成10了,不是11了
		System.out.println("交换后:a的值为:" + a + ",b的值为:" + b);
		System.out.println("#############################################");
		//第三种办法,异域运算,可以解决值越界问题
		a = 10;
		b =11;
		System.out.println("交换前:a的值为:" + a + ",b的值为:" + b);
		/*
		 * 异域运算是按照二进制位进行"异域",对于a,b俩个数字,如果某个二进制位上的a,b都是0或1,则返回0,
		 * 如果一个是0,一个是1则返回1,这样可以得到一个新的数字,这有什么用呢？当这个数字再与A发生"异域"的时候,就能得到B(这个大家可以自行反推)
		 * 这样的技巧其实就是二进制位上的一个加法但不进位的做法(由于只有0,1,所以1+1=2后还是为0),但这个运算是最低级的CPU位运算,
		 * 所以它的效率是极高的,并且不会越界.
		 * 异域运算在计算机系统中大量存在,它的一个很大的优势就是可以按照反向的规则还原数据本身.
		 */
		a = a ^ b;//
		b = a ^ b;//
		a = a ^ b;//
		System.out.println("交换后:a的值为:" + a + ",b的值为:" + b);
		System.out.println("*********************************************");
		
		//http://www.ruanyifeng.com/blog/2021/01/_xor.html
		//
		int t1 = 999;
		int t2 = 999;
		int zero = t1 ^ t2;
		System.out.println("结果应该是0:" + zero);
		
		int t3 = -999;
		int t4 = -999;
		int zero1 = t3 ^ t4;
		System.out.println("结果应该是0:" + zero1);
		
		long numL = 98989898989L;
		long num = 0;
		long res = numL ^ num;
		System.out.println("x ^ 0 = x结果应该是numL:[" + res + "]");
		
		int nl = 0;
		long resI = numL ^ nl;
		System.out.println("一个值与 0 的运算，总是等于其本身。x ^ 0 = x结果应该是numL:[" + resI + "]");
	}
}
