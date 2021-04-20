package com.yale.test.math;

import java.math.BigDecimal;
import java.util.Formatter;
import java.util.Locale;
import java.util.Scanner;

public class FloatDemo {

	public static void main(String[] args) {
		/*
		 * 浮点数运算和整数运算相比，只能进行加减乘除这些数值计算，不能做位运算和移位运算。
		         在计算机中，浮点数虽然表示的范围大，但是，浮点数有个非常重要的特点，就是浮点数常常无法精确表示。
		         举个栗子：
		          浮点数0.1在计算机中就无法精确表示，因为十进制的0.1换算成二进制是一个无限循环小数，很显然，无论使用float还是double，都只能存储一个0.1的近似值。但是，0.5这个浮点数又可以精确地表示。
		         因为浮点数常常无法精确表示，因此，浮点数运算会产生误差：
		         浮点数在内存的表示方法和整数比更加复杂。Java的浮点数完全遵循IEEE-754标准，这也是绝大多数计算机平台都支持的浮点数标准表示方法。
		 */
		double x = 1.0 / 10;//0.1
        double y = 1 - 9.0 / 10;//1-0.1
        // 观察x和y是否相等:
        System.out.println(x);
        System.out.println(y);
        float fn = 0.1f;
        System.out.println("浮点数0.1->" + fn);
        float res = (float)(1-0.9);
        System.out.println("float浮点数0.1->" + res);
        
        double resd = (double)(1-0.9);
        System.out.println("double浮点数0.1->" + resd);
        //由于浮点数存在运算误差，所以比较两个浮点数是否相等常常会出现错误的结果。正确的比较方法是判断两个浮点数之差的绝对值是否小于一个很小的数：
        //比较x和y是否相等，先计算其差的绝对值:
        double r = Math.abs(x - y);
        // 再判断绝对值是否足够小:
        if (r < 0.00001) {//前面讲过了浮点数在计算机中常常无法精确表示，并且计算可能出现误差，因此，判断浮点数相等用==判断不靠谱：
        	//https://www.liaoxuefeng.com/wiki/1252599548343744/1259539352677728
            // 可以认为相等
        	System.out.println("相等");
        } else {
            // 不相等
        	System.out.println("不相等");
        }
        
        /*
         * 浮点数之间的等值判断，注意是等值判断,基本数据类型不能用==来比较，包装数据类型不能用equals来判断。 
         * 说明：浮点数采用“尾数+阶码”的编码方式，类似于科学计数法的“有效数字+指数”的表示方式。二进制无法精确表示大部分的十进制小数，具体原理参考《码出高效》。
         * 《阿里巴巴Java开发手册（泰山版）.pd》
         */
        float af = 1.0f - 0.9f;
        float bf = 0.9f - 0.8f;
        if (af > bf)  {
        	System.out.println("af和bf,等值判断不能用==,但是可以比大小无所谓");
        }
        if(af == bf){
        	// 预期进入此代码快，执行其它业务逻辑
        	// 但事实上af==bf的结果为false
        	System.out.println("af和bf,等值判断不能用==,但是可以比大小无所谓");
        }
        System.out.println("俩者的精度不一样,af：" + af);
        System.out.println("俩者的精度不一样,bf:" + bf);
        if (af == bf) {
        	System.out.println("af和bf相等");
        } else {
        	System.out.println("af和bf不相等,本来我预期的是相等的,结果是不相等,但是这个误差可以忽略掉,我就认为他是相等的");
        }
        Float xf = new Float(af);
        Float yf = new Float(bf);
        if (xf.equals(yf)) {//这里走的是不相等:包装数据类型不能用equals来判断:不相等0.100000024,0.099999964
        	// 预期进入此代码快，执行其它业务逻辑,但事实上equals的结果为false
        	System.out.println("包装数据类型不能用equals来判断:相等" + xf.toString() + "," + yf.toString());
        } else {
        	System.out.println("包装数据类型不能用equals来判断:不相等"  + xf.toString() + "," + yf.toString());
        }
        
        float constF = 0.1f;
        float constF1 = 0.1f;
        if (constF == constF1)  {
        	System.out.println("constF和constF1,是相等的,因为值直接赋值的");
        }
        //解决办法：指定一个误差范围，两个浮点数的差值在此范围之内，则认为是相等的。
        float a = 1.0f - 0.9f;
        float b = 0.9f - 0.8f;
        float diff = 1e-6f;
        if (Math.abs(a - b) < diff) {
        	System.out.println("a 和 b 相等:true");
        }
        //解决办法:使用BigDecimal来定义值，再进行浮点数的运算操作。来自《阿里巴巴Java开发手册（泰山版）.pdf》
        //推荐 总是使用compareTo()比较两个BigDecimal的值，不要使用equals()！BigDecimalTest
        BigDecimal aBig = new BigDecimal("1.0");
        BigDecimal bBig = new BigDecimal("0.9");
        BigDecimal cBig = new BigDecimal("0.8");
        BigDecimal xBig = aBig.subtract(bBig);
        BigDecimal yBig = bBig.subtract(cBig);
        
        if (xBig.equals(yBig)) {
        	System.out.println("yBig和yBig是相等的:true");
        }
        
        //如果参与运算的两个数其中一个是整型，那么整型可以自动提升到浮点型：
        int n = 5;
        double d = 1.2 + 24.0 / n; // 6.0
        System.out.println(d);
        
        //需要特别注意，在一个复杂的四则运算中，两个整数的运算不会出现自动提升的情况。例如：
        double dd = 1.2 + 24 / 5; // 5.2
        //计算结果为5.2，原因是编译器计算24 / 5这个子表达式时，按两个整数进行运算，结果仍为整数4。
        
        /*
         * 溢出
			整数运算在除数为0时会报错，而浮点数运算在除数为0时，不会报错，但会返回几个特殊值：
			NaN表示Not a Number
			Infinity表示无穷大
			-Infinity表示负无穷大
			冷知识
         */
        double d1 = 0.0 / 0; // NaN
        System.out.println(d1);
        System.out.println("Not a Number:" + Double.isNaN(d1));
        System.out.println("isInfinite:" + Double.isInfinite(d1));
        double d2 = 1.0 / 0; // Infinity
        System.out.println(d2);
        System.out.println("Not a Number:" + Double.isNaN(d2));
        System.out.println("isInfinite:" + Double.isInfinite(d2));
        double d3 = -1.0 / 0; // -Infinity
        System.out.println(d3);
        System.out.println("Not a Number:" + Double.isNaN(d3));
        System.out.println("isInfinite:" + Double.isInfinite(d3));
        /*
         * 强制转型
		 * 可以将浮点数强制转型为整数。在转型时，浮点数的小数部分会被丢掉。如果转型后超过了整型能表示的最大范围，将返回整型的最大值。例如：
         */
        int n1 = (int) 12.3; // 12
        int n2 = (int) 12.7; // 12
        int n22 = (int) -12.7; // -12
        int n3 = (int) (12.7 + 0.5); // 13
        int n4 = (int) 1.2e20; //2147483647,科学计数法
        
        //如果要进行四舍五入，可以对浮点数加上0.5再强制转型：
        double ddd = 2.6;
        int ndd = (int) (ddd + 0.5);
        System.out.println("如果要进行四舍五入，可以对浮点数加上0.5再强制转型：" + ndd);
        /*
         * 廖雪峰的练习题
         * 计算前N个自然数的和可以根据公示：
			(1+N)×N2\frac{(1+N)\times N}22(1+N)×N​
			请根据公式计算前N个自然数的和：
			https://www.liaoxuefeng.com/wiki/1252599548343744/1255888634635520
         * 根据一元二次方程ax2+bx+c=0ax^2+bx+c=0ax2+bx+c=0的求根公式：
			−b±b2−4ac2a\frac{\displaystyle-b\pm\sqrt{b^2-4ac}}{\displaystyle2a}2a−b±b2−4ac
			计算出一元二次方程的两个解：
			https://www.liaoxuefeng.com/wiki/1252599548343744/1255887847679616
			请将一组int值视为字符的Unicode编码，然后将它们拼成一个字符串：
			https://www.liaoxuefeng.com/wiki/1252599548343744/1255938912141568
         */
        
        int nx = -100;
        int xb = nx >= 0 ? nx : -nx;//上述语句的意思是，判断n >= 0是否成立，如果为true，则返回n，否则返回-n。这实际上是一个求绝对值的表达式。
        System.out.println(xb);
        
        double dformat = 12900000;
        System.out.println("格式化输出:" + dformat); // 1.29E7
        
        System.out.println("将double类型的值转换成IEEE 754规定的位:" + Double.doubleToLongBits(0.01));
        if (Double.compare((double)af, (double)bf) == 0) {
        	//Double.compare源码用的就是Double.doubleToLongBits比的
            System.out.println("Double.compare比较---------af和bf,相等");
        } else if (Double.compare((double)af, (double)bf) > 0) {
            System.out.println("Double.compare比较---------af大于bf");
        } else if (Double.compare((double)af, (double)bf) <0) {
            System.out.println("Double.compare比较---------af小于bf");
        }
        
        //如果要把数据显示成我们期望的格式，就需要使用格式化输出的功能。格式化输出使用System.out.printf()，通过使用占位符%?，printf()可以把后面的参数格式化成指定格式：
        //Java的格式化功能提供了多种占位符，可以把各种数据类型“格式化”成指定的字符串：
        /*
         * 语法规则
         * %[argument_index$][flags][width][.precision]conversion
         * %d	格式化输出整数
		   %x	格式化输出十六进制整数
		   %f	格式化输出浮点数
		   %e	格式化输出科学计数法表示的浮点数
		   %s	格式化字符串
		   注意，由于%表示占位符，因此，连续两个%%表示一个%字符本身。
		   占位符本身还可以有更详细的格式化参数。下面的例子把一个整数格式化成十六进制，并用0补足8位：
         */
        double dfor = 3.1415926;
        System.out.printf("%.2f\n", dfor); // 显示两位小数3.14
        System.out.printf("%.4f\n", dfor); // 显示4位小数3.1416
        
        int nfo = 12345000;
        System.out.printf("将正数转换为十六进制:n=%d, hex=%x\n", nfo, nfo); // 注意，两个%占位符必须传入两个数
        System.out.printf("将正数转换为十六进制,并用0补足8位:n=%d, hex=%08x\n", nfo, nfo); // 注意，两个%占位符必须传入两个数
        System.out.printf("将正数转换为十六进制,不足32位前面是空格: hex=%32x\n", nfo);
        System.out.printf("将正数转换为十六进制,并用0补足32位: hex=%032x\n", nfo); 
        System.out.printf("将正数转换为十六进制,#代表输出以0x开头: hex=%#32x\n", nfo); 

        /*
         * 详细的格式化参数请参考JDK文档java.util.Formatter
         * https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Formatter.html#syntax
         * https://www.cnblogs.com/Lowp/archive/2012/09/16/2687745.html
         * https://blog.csdn.net/kingboyworld/article/details/69951340
         */
        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb, Locale.US);
        System.out.println("formatter，4$代表第四个参数:" + formatter.format("%4$2s %3$2s %2$2s %1$2s", "111a", "b", "c", "d"));
        System.out.println("formatter的结果会存储再sb里面:" + sb.toString());
        System.out.println("****************");
        System.out.println("formatter，---" + formatter.format(Locale.FRANCE, "e = %+10.4f", Math.E));
        
        Formatter formatter1 = new Formatter();
        //如果该值为负，并且给出了 '(' 标志，那么预先考虑 '(' ('(')，并追加一个 ')' (')')。
        System.out.println(formatter1.format("Amount gained or lost since last statement: $ %(,.2f", -6271.58));
        
        System.out.println(formatter1.format("Amount gained or lost since last statement: $ %(,.2f", 6271.58));

        
        Scanner scanner = new Scanner(System.in); // 创建Scanner对象,接收从用户从控制台的输入
        System.out.println("Input your name: "); // 打印提示
        String name = scanner.nextLine(); // 读取一行输入并获取字符串
        System.out.print("Input your age: "); // 打印提示
        int age = scanner.nextInt(); // 读取一行输入并获取整数
        System.out.printf("Hi, %s, you are %d\n", name, age); // 格式化输
	}
}
