package com.yale.test.math;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * https://www.imooc.com/video/21177
 * 慕课网的 舞马 老师 二进制与Java中的基本数据类型 课程
 * 【【计算机科学速成课】[40集全/精校] - Crash Course Computer Science-哔哩哔哩】https://b23.tv/ZlioyU
 * 什么是浮点数:浮点数指的是小数点的位置可以浮动的数,比如220.5=22.05x10^1又可以=2.205x10^2。计算机时基于二进制的,所以所有的小数都是使用二进制存储的。
 * 根据IEEE754指定的浮点数表示标准,对于任何一个小数来说呢,计算机都会将其规格化表达为1.xxx乘2的N次方这样的表达形式。对于任何一个小数来说，它的二进制表示呢第一位一定是1,剩下的XXX表示尾数。
 * 浮点数在JAVA中有俩种,double是双精度占64位,float单精度占32位。以float为例,32bit分为三段,第一段占一个bit代表符号位,第二段为指数位占8bit,第三段为有效数字占23bit。
 * double符号占1bit,指数位占11bit,有效数字占52bit.那精度是什么东西呢？以float为例,float的有效数字占23bit,23bit可以表示最大得十进制数字为2^23=8388608（七位数）。但是要注意
 * 计算机存储浮点数的第一位一定是1,所以计算机在存储浮点数的时候并不存储第一位,所以float的最大精度是2^24=16777216(八位数,严格来说是7到8位,这块有知识点还没搞明白)。所以float的精度实际上能表示八个十进制位的数字。
 * 那double的精度就是2^53=9,007,199,254,740,992(16位，严格来说是16到17位,这块有知识点还没搞明白),所以System.out.println(0.3-0.1);的结果是“0.19999999999999998”总共17位。
 * 有一个问题不存储第一位,那0.0怎么存？IEEE754规定:最高位有效数字M,计算机默认存储数值1,但这是指数位E不全为1或者全为0的情况,如果指数位全为0，则还原的就是0.XXXXX,
 * 这时候,如果有效数字也全为0的话,那就是0.0了。
 * 【Java那些事.02.求你了，别用Double和Float进行小数计算/精度缺失引发的血案-哔哩哔哩】https://b23.tv/BhojxL
 * 小数在计算机中的存储方式有俩点：1、定点数2、浮点数(因为小数点可以在数字间浮动，有好几种方法表示浮点数，最常见的是IEEE 754标准,它用类似科学计数法的方法,来存十进制值，例如:625.9可以写成0.6259x10^3。
 * 这里有俩个重要数字:.6259叫有效位数,3是指数.在32位浮点数中,第1位表示数字的正负,接下来8位存指数(指数位),剩下23位存有效位数.)
 * 定点数:约定所有数值数据的小数点隐含在某一个固定位置上.
 * 		定点数优点:首先我们确定了小数点的位置,他的整数部分和小数部分都可以完全的表示出来。
 * 		定点数缺点:它表示的范围和存储的空间有着非常密切的联系.如果固定使用几个字节表示,那它表示的范围就很有限了.
 * 浮点数:数值数据的小数点位置不固定,也就是说小数点的位置并不固定,小数点是浮动的.但是浮动的小数点我们该怎么表示呢?
 * 其实浮点数使用了指数计数法来表示
 * 		指数规则:任意实数,都可以由一个定点数x基数的整数次幂得到.就像10进制的科学计数法.
 * 			       比如:3.14159=0.314159 x 10^1 或者  3.14159 = 31.14159 x 10^-1 
 * 		浮点数就是在二进制中使用指数计数法的规则来存储数据。
 * 			       比如:(2.75)10 = (10.11)2 = 1.011 x 2^1 或者 0.1011 x 2^2  或者 101.1 x 2^-1次方等等各种形式
 * 				   上面这个例子的意思是先将十进制的数字2.75转换成二进制10.11,然后它用指数计数法就可以写作1.011x2^1或101.1 x 2^-1次方等等各种形式.
 * 			     二进制的基数恒定是2,所以在存储的时候我们只需要一个定点数再加上指数就可以存储小数了.
 * 在浮点数规则当中定点数的部分叫做:尾数,指数部分叫做:阶码。小数同样也有正负之分,所以我们在存储时只需要掌握符号位,阶码,尾数这个三个部分就可以用
 * 二进制浮点数存储小数了.
 * 		浮点数优点:
 * 	            浮点数缺点:1,在存入一个数据时它的尾数和阶码的位数并不固定,在我们进行数据交换的时候需要先对它进行固定.
 * 				2,尾数的定点形式也不固定,我们可以将小数点隐含在任意一个位置上来表示这个浮点数.
 * 		针对以上俩个缺点直到1985年美国制定了    二进制浮点算数标准:IEEE754标准,世界上几乎所有的计算机和编程语言都支持该标准
 * java的俩种浮点类型使用的存储规则
 * 		单精度float(4字节)和双精度double(8字节)遵循的就是IEEE754标准
 * 		IEEE754规定,float:1位符号位(S) + 8位阶码(E) + 23位尾数(M)
 * 				   double: 1位符号位(S) + 11位阶码(E) + 52位尾数(M)
 * 		IEEE754规定, 尾数M:纯小数形式,小数点在尾数最前端,通常为规约形式.
 * 					规约形式:在科学表示法下,小数最高有效位是1(整数部分).例子:(12.5625)10 = (1100.1001)2 = 1.1001001 x 2^3,
 * (0.5625)10 = (0.1001)2 = 1.001 x 2^-1,但是存储时由于规约形式的最高位恒定是1,所以这个1在存储时就被省略了,尾数就只存入纯小数
 * 		尾数M的表示范围:(0 <= M <= 1),规约形式实值=1+M,使用规约形式实际上可以使尾数的表示范围比实际存储再多一位,像float定义的尾数是23位,
 * 当用规约形式存储加上隐含的一位就是24位,double就从52位变成了实际的53位,这样设计就可以使用更少的空间表示了更大范围的数。
 * 阶码:浮点数的阶码通常用移码来表示,移码又叫增码,它是将二进制表示的机器数正向偏移固定的值所得到的结果.
 * 移码:将数值正向偏移(2e-1),等于符号位取反的补码.这里的小写e表示的位数的限制,在8位长度下偏移值就是2的7次方也就是128.
 * 正向增加128由于位数的限制移码实际值就等于补码的符号位取反的结果.例子: 1=(0000 0001)补 = (1000 0001)移  -1=(1111 1111)补 = (0111 1111)移
 * 之所以用移码来记录阶码呢,目的就是为了方便编码比大小,如果直接使用补码来记录的话,符号位参与运算,直接对比的负数结果恒大于正数的,因为负数的首位符号位是1
 * 用移码来表示呢就解决了这个问题,因为它将补码的符号位取反了,正数的首位变成了1,在计算时我一般将移码记录数值看作是无符号数也就是纯粹的二进制数
 * 然后再减去偏移值得到的结果就是它所记录的实际值,就是移码表示的-1在计算时我们将它看作是纯正数(0111 1111)移 = 127 然后减去偏移值128 = -1
 * IEEE754阶码规则:在规定中阶码使用的是标准移码减1来记录数值,实际上就是将它的偏移值在2的e-1次方的基础上再减去1,在8位的阶码中他的偏移值
 * 就是127,如果在double11位的阶码中偏移值就是1023,在计算时要注意这一点.
 * 阶码:用移码(标准移码-1)记录指数,实际偏移值为(2e-1 - 1).
 * 
 * 使用BigDecimal进行浮点精确计算，BigDecimal用于表示精确的小数，常用于财务计算；
 * 和BigInteger类似，BigDecimal可以表示一个任意大小且精度完全准确的浮点数。
 * 如果查看BigDecimal的源码，可以发现，实际上一个BigDecimal是通过一个BigInteger和一个scale来表示的，即BigInteger表示一个完整的整数，而scale表示小数位数：
 * BigDecimal这个类的原理：在处理十进制小数扩大N倍变成整数，然后再保留精度信息。
 * @author dell
 */
public class BigDecimalTest {

	public static void main(String[] args) {
		float num1 = 0.1f;
		float num2 = 0.8f;
		System.out.println("注意结果并不是0.9:->" + (num1 + num2));
		
		/*
		 * 禁止使用构造方法BigDecimal(double)的方式把double值转化为BigDecimal对象。 说明：BigDecimal(double)存在精度损失风险，
		 * 在精确计算或值比较的场景中可能会导致业务逻辑异常。
		 * 如：BigDecimal g = new BigDecimal(0.1f); 实际的存储值为：0.10000000149 正例：优先推荐入参为String的构造方法，
		 * 或使用BigDecimal的valueOf方法，此方法内部其实执行了Double的toString，而Double的toString按double的实际能表达的精度对尾数进行了截断。
		 *  BigDecimal recommend1 = new BigDecimal("0.1"); 
		 *  BigDecimal recommend2 = BigDecimal.valueOf(0.1);
		 */
		BigDecimal b1 = new BigDecimal("0.1");//推荐使用这种方式
		System.out.println("推荐使用这种方式:" + b1.toString());
		BigDecimal b2 = new BigDecimal(0.1);//严重不推荐使用这种方式
		System.out.println("严重不推荐使用这种方式,因为double类型在存储类型时实际上存储的是一个近似值,而BigDecimal存储的是精确值:" + b2.toString());
		System.out.println("解决办法就是先将double转换成String,再用BigDecimal就可以了");
		BigDecimal b3 = new BigDecimal(0.1 + "");//解决办法
		System.out.println("解决办法就是先将double转换成String,再用BigDecimal就可以了" + b3.toString());
		System.out.println("还有另外的解决方法就是直接使用BigDecimal的valueOf方法");
		BigDecimal b4 = BigDecimal.valueOf(0.1);//第二种解决办法
		System.out.println("还有另外的解决方法就是直接使用BigDecimal的valueOf方法" + b4.toString());
		
		
		System.out.println("BigDecimal的加减乘方法没有什么特别的,但是除法要特别注意,使用除法时要注意,当一个数值发生除不尽的时候"
				+ "就会发生ArithmeticException异常,因为BigDecimal是精确小数类,我们不能让BigDecimal一直无限循环运算下去");
		BigDecimal bb, bb2;
		bb = BigDecimal.valueOf(0.3);
		bb2 = BigDecimal.valueOf(0.1);
		BigDecimal div1 = bb.divide(bb2);
		System.out.println(div1);
		try {
			BigDecimal div2 = bb2.divide(bb);
			System.out.println(div2);
		} catch (ArithmeticException e) {
			e.printStackTrace();
		}
		System.out.println("解决办法");
		
		//MathContext的第一个参数是保留几位小数,第二个参数是四舍五入的方法,HALF_UP是四舍五入
		BigDecimal div3 = bb2.divide(bb, new MathContext(5, RoundingMode.HALF_UP));
		System.out.println("保留5位小数,并用四舍五入的方式保留小数" + div3);
		
		System.out.println("有些人可能会有疑问既然BigDecimal可以精确表示小数,那为啥要用Double这种不精确的类型呢？实际上现实生活中很多数值本来就不是很精确的");
		System.out.println("就像一部电影的时长你可以精确到分秒,但是你没必要精确到毫秒或者纳秒了,大部分情况下我们只需要有限范围内的精确就可以了,");
		System.out.println("而且基本类型的存储和计算效率会高很多,使用他是一个取舍的结果");
		
		BigDecimal d1 = new BigDecimal("123.45");
		System.out.println("BigDecimal用scale()表示小数位数，例如：" + d1.scale());
		//通过BigDecimal的stripTrailingZeros()方法，可以将一个BigDecimal格式化为一个相等的，但去掉了末尾0的BigDecimal：
		BigDecimal d11 = new BigDecimal("123.4500");
		BigDecimal d2 = d1.stripTrailingZeros();
		System.out.println(d11.scale()); // 4
		System.out.println(d2.scale()); // 2,因为去掉了00
		
		
		BigDecimal d3 = new BigDecimal("1234500");
		BigDecimal d4 = d3.stripTrailingZeros();
		System.out.println(d3.scale()); // 0
		System.out.println(d4.scale()); // -2
		System.out.println("如果一个BigDecimal的scale()返回负数，例如，-2，表示这个数是个整数，并且末尾有2个0。");
		
		//调用divideAndRemainder()方法时，返回的数组包含两个BigDecimal，分别是商和余数，其中商总是整数，余数不会大于除数。我们可以利用这个方法判断两个BigDecimal是否是整数倍数：
		BigDecimal n = new BigDecimal("12.75");
		BigDecimal m = new BigDecimal("0.15");
		BigDecimal[] dr = n.divideAndRemainder(m);
		if (dr[1].signum() == 0) {
		    // n是m的整数倍
			System.out.println("n是m的整数倍");
		} else {
			System.out.println("n不是m的整数倍");
		}
		
		/**
		 * 10. 【强制】如上所示BigDecimal的等值比较应使用compareTo()方法，而不是equals()方法。 
		 * 说明：equals()方法会比较值和精度（1.0与1.00返回结果为false），而compareTo()则会忽略精度。
		 * 《阿里巴巴Java开发手册嵩山版2020.pdf》
		 * 12. 【强制】禁止使用构造方法BigDecimal(double)的方式把double值转化为BigDecimal对象。 
		 * 说明：BigDecimal(double)存在精度损失风险，在精确计算或值比较的场景中可能会导致业务逻辑异常。
		 * 如：BigDecimal g = new BigDecimal(0.1F); 实际的存储值为：0.10000000149 
		 * 正例：优先推荐入参为String的构造方法，或使用BigDecimal的valueOf方法，此方法内部其实执行了Double的toString，而Double的toString按double的实际能表达的精度对尾数进行了截断。 
		 * BigDecimal recommend1 = new BigDecimal("0.1"); 
		 * BigDecimal recommend2 = BigDecimal.valueOf(0.1);
		 */
		//在比较两个BigDecimal的值是否相等时，要特别注意，使用equals()方法不但要求两个BigDecimal的值相等，还要求它们的scale()相等：
		BigDecimal d111 = new BigDecimal("123.456");
		BigDecimal d22 = new BigDecimal("123.45600");
		System.out.println(d111.equals(d22)); // false,因为scale不同
		System.out.println(d111.equals(d22.stripTrailingZeros())); // true,因为d2去除尾部0后scale变为2
		System.out.println("推荐 总是使用compareTo()比较两个BigDecimal的值，不要使用equals()！ " + d111.compareTo(d22)); // 0
		
		BigDecimal nVatVar = new BigDecimal("123.45600");
		System.out.println("nVatVar的值为:" + nVatVar);
		System.out.println("nVatVar的值取反为:negate就是取反的意思" + nVatVar.negate());
		
		BigDecimal recommend1 = new BigDecimal(0.1F);
		System.out.println("实际的存储值为：" + recommend1.toString());
		System.out.println("实际的存储值为：" + recommend1.toEngineeringString());
		System.out.println("实际的存储值为：" + recommend1.toPlainString());
		
		BigDecimal recommendTest = new BigDecimal("0.1");
		System.out.println("实际的存储值为：" + recommendTest.toString());
		System.out.println("实际的存储值为：" + recommendTest.toEngineeringString());
		System.out.println("实际的存储值为：" + recommendTest.toPlainString());
		
		BigDecimal recommendTest1 = BigDecimal.valueOf(0.1F);
		System.out.println("实际的存储值为：" + recommendTest1.toString());
		System.out.println("实际的存储值为：" + recommendTest1.toEngineeringString());
		System.out.println("实际的存储值为：" + recommendTest1.toPlainString());
		
		BigDecimal recommendTest2 = BigDecimal.valueOf(0.1);
		System.out.println("实际的存储值为：" + recommendTest2.toString());
		System.out.println("实际的存储值为：" + recommendTest2.toEngineeringString());
		System.out.println("实际的存储值为：" + recommendTest2.toPlainString());
	}
}
