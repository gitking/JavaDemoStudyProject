package com.yale.test.math;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Formatter;
import java.util.Locale;
import java.util.Scanner;

//结合com.yale.test.java.demo.string.StringTest一起看
//com.yale.test.ps.HashDemo
/**
 * 2. 【强制】在long或者Long赋值时，数值后使用大写字母L，不能是小写字母l，小写容易跟数字混淆，造成误解。 
 * 说明：Long a = 2l; 写的是数字的21，还是Long型的2？
 * 《阿里巴巴Java开发手册嵩山版2020.pdf》
 *  7. 【强制】所有整型包装类对象之间值的比较，全部使用equals方法比较。 
 * 说明：对于Integer var = ? 在-128至127之间的赋值，Integer对象是在 IntegerCache.cache产生，会复用已有对象，这个区间内的Integer值可以直接使用==进行判断，
 * 但是这个区间之外的所有数据，都会在堆上产生，并不会复用已有对象，这是一个大坑，推荐使用equals方法进行判断。
 * 《阿里巴巴Java开发手册嵩山版2020.pdf》
 * @author issuser
 *
 */
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
        float fn = 0.1F;
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
         * 9. 【强制】浮点数之间的等值判断，基本数据类型不能用==来比较，包装数据类型不能用equals来判断。 
         * 说明：浮点数采用“尾数+阶码”的编码方式，类似于科学计数法的“有效数字+指数”的表示方式。二进制无法精确表示大部分的十进制小数，具体原理参考《码出高效》。
         * 《阿里巴巴Java开发手册嵩山版2020.pdf》
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
        boolean result = floatCompare(af, bf);
        if (result) {
        	System.out.println("变量比较不一样" + af + ",bf" + bf);
        } else {
        	System.out.println("变量比较不一样" + af + ",bf" + bf);
        }
        boolean resu = floatCompare(0.1f, 0.1f);
        if (resu) {
        	System.out.println("常量比较结果一样" + 0.1f + ",bf" + 0.1f);
        } else {
        	System.out.println("常量比较结果不一样" + 0.1f + ",bf" + 0.1f);
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
        
        if (xf.compareTo(yf) > 0) {//这里走的是不相等:包装数据类型不能用equals来判断:不相等0.100000024,0.099999964
        	// 预期进入此代码快，执行其它业务逻辑,但事实上equals的结果为false
        	System.out.println("包装数据类型不能用equals,但是应该可以用compareTo来判断:xf比yf大---->" + xf.toString() + "," + yf.toString());
        } else if (xf.compareTo(yf) < 0){
        	System.out.println("包装数据类型不能用equals,但是应该可以用compareTo来判断:xf比yf小---->"  + xf.toString() + "," + yf.toString());
        } else if (xf.compareTo(yf) == 0) {
        	System.out.println("包装数据类型不能用equals,但是应该可以用compareTo来判断:xf等于yf---->"  + xf.toString() + "," + yf.toString());
        }
        
    	System.out.println("结果不是0.2-->"  + (0.3 - 0.1));

        
        /*
         * https://club.perfma.com/question/2376934
         * 上面这个问题是我自己写的,到这里我算是明白了,不能用==或者equals去比较的原因,都是因为==和equals和compareTo比较的是精确的值,而不是我们预期的值知道吗？
         * 比如这俩个float af = 1.0f - 0.9f;float bf = 0.9f - 0.8f;我们预期af和bf是相等的都是0.1,但实际上af的值是0.100000024,bf的值是0.099999964
         * 如果你看精确值去比较,比如你用==和equals和compareTo去比较,那肯定是不一样的,因为==和equals和compareTo这三个比较的都是精确的值,而不是我们预期的值
         * 我们预期的值是有误差的不精确的,所以人家阿里巴巴《阿里巴巴Java开发手册（泰山版）.pd》才推荐：指定一个误差范围，两个浮点数的差值在此范围之内，则认为是相等的。 float a = 1.0f - 0.9f;
         * 《阿里巴巴Java开发手册（泰山版）.pd》里面的正例： 
			(1) 指定一个误差范围，两个浮点数的差值在此范围之内，则认为是相等的。 float a = 1.0f - 0.9f;
			float b = 0.9f - 0.8f;
			float diff = 1e-6f;
			if (Math.abs(a - b) < diff) {
			System.out.println("true");
			} 
			(2) 使用BigDecimal来定义值，再进行浮点数的运算操作。
			BigDecimal a = new BigDecimal("1.0");
			BigDecimal b = new BigDecimal("0.9");
			BigDecimal c = new BigDecimal("0.8");
			BigDecimal x = a.subtract(b);
			BigDecimal y = b.subtract(c);
			if (x.equals(y)) {
			System.out.println("true");
			}
		 * https://mp.weixin.qq.com/s/RW_8dMiGNJ2yWVB3upI-Wg
		 * Java中的浮点数到底应该怎么比较，才算是最优解？你真的知其然,知其所以然了吗？
         */
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
         * https://www.cnblogs.com/Lowp/archive/2012/09/16/2687745.html Java学习笔记之Formatter的用法详解(输出格式化)
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
        
        Double dbl = new Double(1.000600-1);
        System.out.println("double默认的toString是科学计数法:" + dbl);
        
    	double sdf = 0.12321321113;
		System.out.println("注意如果double的小数点是0.1这种开头的是不会变成科学计数法的:" + sdf);
		Double ddf = new Double(sdf);
		System.out.println("注意如果double的小数点是0.1这种开头的是不会变成科学计数法的:" + ddf);
		System.out.println(ddf.toString());
		double sdf123 = 0.0023333;
		System.out.println("一旦double小数点.后面的0超过3个就会变成科学计数法:" + sdf123);
		double sdf12 = 0.00023333;
		System.out.println("一旦double小数点.后面的0超过3个就会变成科学计数法:" +  sdf12);
		double sdf1 = 0.00000023333;
		System.out.println("一旦double小数点.后面的0超过3个就会变成科学计数法:" + sdf1);
		
		double numMax = 98000000.00009;
		System.out.println("double的整数部分大于等于8位数也会变成科学计数法,这个会变" + numMax);
		double numNoMax = 9800000.00;
		System.out.println("double的整数部分大于等于8位数也会变成科学计数法,这个就不会变" + numNoMax);
		double numNoMaxD = 9800000.000009;
		System.out.println("double的整数部分大于等于8位数也会变成科学计数法,这个就不会变" + numNoMaxD);

        NumberFormat nf = NumberFormat.getInstance();  
        //注意数字:1,514,940.1的分隔符,千位分隔符，百万分隔符，……)，使用时会直接显示出分隔符，如果想去掉，则使用.setGroupingUsed(false);1,514,940.1
		nf.setGroupingUsed(false);  
        nf.setMinimumFractionDigits(1);//最少保留1位小数，如果为0就是不保留小数
        nf.setMaximumFractionDigits(6);//最大保留俩位小数
		String doubStr = nf.format(dbl);//处理double 值过大的情况导致科学计数法的问题
        System.out.println("double格式化输出: " + doubStr); // 打印提示
        
		String doubStr021 = nf.format(numMax);//处理double 值过大的情况导致科学计数法的问题
        System.out.println("double格式化输出: " + doubStr021); // 打印提示
        
		String numNoMax021 = nf.format(numNoMax);//处理double 值过大的情况导致科学计数法的问题
		
        System.out.println("double格式化输出:这个格式化之后小数点后面的0没了: " + numNoMax021); // 打印提示
        System.out.println("double格式化输出:这个格式化之后小数点后面的0没了,加上nf.setMaximumFractionDigits(6);这行代码就有了" + numNoMax021); // 打印提示
        
        NumberFormat nfb = NumberFormat.getPercentInstance();//将数字格式化为百分比%
        nfb.setMinimumFractionDigits(6);//小数点后面最少保留6位
        nfb.setMaximumFractionDigits(6);//保留到小数点后几位        
        System.out.println("double格式化输出02: " + nfb.format(dbl));
        System.out.println("double格式化输出03: " + nfb.format(0.47));//显示：47.000000%
        
        Double amt11 = new Double (1800000000.00);
		System.out.println("数字太大，会变成科学计数法E:" + amt11);
		System.out.println("数字太大" + doubleToStr(amt11, 1, 2));


        DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
        String doubStr01 = df.format(dbl);
        System.out.println("doubStr01格式化输出: " + doubStr01); // 打印提示

        DecimalFormat df1 = new DecimalFormat("####.000");//保留3位小数
        String doubStr02 = df1.format(dbl);
        System.out.println("doubStr02格式化输出: " + doubStr02); //
        System.out.println("保留3位小数点:" +df1.format(1234.56));
        System.out.println("保留3位小数点:" +df1.format(91234.56));
        System.out.println("保留3位小数点:" +df1.format(0.00));
        System.out.println("保留3位小数点:" +df1.format(0.01));
        
        DecimalFormat df4 = new DecimalFormat("##.00%");    //##.00%   百分比格式，后面不足2位的用0补齐
        String baifenbi= df4.format(0.47);
        System.out.println("用DecimalFormat将数字格式化为百分比%:->" + baifenbi);
        baifenbi= df4.format(0.00);
        System.out.println("用DecimalFormat将数字格式化为百分比%:->" + baifenbi);
        baifenbi= df4.format(0.01);
        System.out.println("用DecimalFormat将数字格式化为百分比%:->" + baifenbi);
        
        DecimalFormat df09 = new DecimalFormat("###,##0.00");
        System.out.println(df09.format(24.7));//显示：24.70
        System.out.println(df09.format(23123.47));//显示：123,23.47
        System.out.println(df09.format(0.00));//显示：123,23.47
        System.out.println(df09.format(0.01));//显示：123,23.47
        //补充：0.00、0.01; 0.00%、0.12%这样的数据，如果按照上面的格式可能会造成数据显示成：.00、.01; .00%、.12%，怎么办呢？只要把格式改成：
        DecimalFormat df08 = new DecimalFormat("0.00");
        System.out.println("df08->" + df08.format(24.7));//显示：24.70
        System.out.println("df08->" + df08.format(23123.47));//显示：23123.47
        System.out.println("df08->" + df08.format(0.00));//显示：0.00
        System.out.println("df08->" + df08.format(0.01));//显示：0.01
        DecimalFormat df07 = new DecimalFormat("0.00%");
        System.out.println("df07->" + df07.format(24.7));//显示：2470.00%
        System.out.println("df07->" + df07.format(23123.47));//显示：2312347.00%
        System.out.println("df07->" + df07.format(0.00));//显示：0.00%
        System.out.println("df07->" + df07.format(0.01));//显示：1.00%

        System.out.println("double超过三位就开始用科学计数法了" + new Double(0.001));
	    System.out.println("double超过三位就开始用科学计数法了" + new Double(0.1));
	    System.out.println("double超过三位就开始用科学计数法了" + new Double(0.01));
	    System.out.println("double超过三位就开始用科学计数法了" + new Double(0.001));
	    System.out.println("double超过三位就开始用科学计数法了" +new Double(0.0001));
	    
	    System.out.println("Float超过三位就开始用科学计数法了" + new Float(0.001));
	    System.out.println("Float超过三位就开始用科学计数法了" + new Float(0.1));
	    System.out.println("Float超过三位就开始用科学计数法了" + new Float(0.01));
	    System.out.println("Float超过三位就开始用科学计数法了" + new Float(0.001));
	    System.out.println("Float超过三位就开始用科学计数法了" +new Float(0.0001));
	    System.out.println("---计算-保留2位小数-String.format--------四舍五入----------------");
	    System.out.println(String.format("%.2f",999.651));
	    System.out.println(String.format("%.2f",0.651));
	    //double是双精度占64位,float单精度占32位
	    //精确比较,0.19999999999999998,注意这个结果小数点后面有17位,因为double最多就能表达17位,结合BigDecimalTest.java这个类一起看
	    //【Java那些事.02.求你了，别用Double和Float进行小数计算/精度缺失引发的血案-哔哩哔哩】https://b23.tv/BhojxL
	    //可以结合BigDecimalTest.java这个类来看
	    System.out.println("结果不是0.2," + (0.3-0.1));
	    double sd = 0.199999999999999983000000012222;
	    System.out.println("最多17位:" + sd);
	    
	    System.out.println("结果是0.1," + (0.1));
	    System.out.println("结果是0.2," + (0.2));
	    /**
	     * 可以看到0.3在计算机中用二进制存储的时候是一个无限循环小数
	     * ERROR:0.0100110011001100110011001100110,循环段为1001,无限循环下去了。
	     * 所以计算机在存储0.3这个小数的时候存的也是一个近似值,所以0.3-01的结果肯定就不是0.2了。
	     * 对于计算机来说将十进制小数转化成二进制的时候呢,可能会出现无限循环或者说超出有效数的程度这种情况导致这个十进制小数没办法用二进制数完整的存储。
	     */
	    System.out.println("计算机在表示0.3的时候是怎么表示的:" + toBin(0.3));
	    
	    /**
	     * float，double如果前面是0的话,是可以省略的。比如,0.01可以写成这样.01。java中小数模仿是double类型的,如果要声明float要在后面加f,f小写大写都可以。
	     * 例如: float test = .01f;或者float test01 = .02F;小数点的运算从来不会发生异常,整数的运算是不能除以0的。因为0不可以做除数。但是浮点数是可以除以0的。1.0/0的结果是Infinity(正无穷大) -1.0/0的结果是-Infinity(负无穷大)，0.0/0的结果是NaN。NaN是Not a Number就是不是一个数字。Double.isNaN()可以判断double的值是否是一个数字
	     * 强制类型转换,如果要将小数点强制转换成整形,小数点后面的部分会直接被揭掉，而不是临近舍入。
	     * 静态方法Math.round(2.4);是根据第一位小数来进行四舍五入的，返回结果是long类型，结果是2。Math.round(2.49);的结果还是2，因为他是根据第一位小数来四舍五入的。Math.round(2.5);结果是3。Math.floor(2.99);是向下取整，返回结果是dobule类型，结果是2.0；floor英文单词是地面的意思。Math.ceil(2.11);是向上取整，返回结果是dobule类型，结果是3.0;ceil英文单词是天花板的意思。
	     * https://blog.csdn.net/u010477645/article/details/51713375
	     */
	    float test = .01F;
	    float test1 = 0.01f;
	    float test2 = 0.000000000000000000000000000000000000000000001f;
	    float test3 = 0.000000000000000000000000000000000000000000009f;
	    float ss = test2 * test3;
	    System.out.println("ss" + ss);
	}
	
	public static boolean floatCompare(float a, float b) {
		System.out.println("浮点数a的值为:" + a);
		System.out.println("浮点数b的值为:" + b);
		return a == b;
	}
	
	/**
	 * 这个算法呢用于将一个double类型的数字表示为它的二进制字符串。
	 * 如果长度大于32位就报错并只返回32位。这个算法怎么来的呢？大家可能要看一下计算机的组织原理了。
	 * ERROR:0.0100110011001100110011001100110
	 * @param num
	 * @return
	 */
	public static String toBin(double num) {
		StringBuilder ans = new StringBuilder("0.");
		while (num != 0) {
			//num *=2;
			num = num * 2;
			if (num >= 1) {
				ans.append("1");
				num -= 1;
			} else {
				ans.append("0");
			}
			if (ans.length() > 32) {
				return "ERROR:" + ans;
			}
		}
		return ans.toString();
	}
	
	/**
     * 将Double转换成字符串的数字,处理double值过大的情况导致科学计数法的问题,小数点部分超过maximumFractionDigits会四舍五入<br><pre>
     * 创建时间：Sep 24, 2021 9:40:45 AM </pre>
     * @param Double dbl 要转换的小数
     * @param int minimumFractionDigits 最少保留几位小数,minimumFractionDigits为0不保留小数点
     * @param int maximumFractionDigits 最大保留几位小数,超过maximumFractionDigits的小数点会地方四舍五入。
     * @return String 字符串类型的数字
     */
    public static String doubleToStr(Double dbl, int minimumFractionDigits, int maximumFractionDigits) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        nf.setMinimumFractionDigits(minimumFractionDigits);
        nf.setMaximumFractionDigits(maximumFractionDigits);
        return nf.format(dbl);
    }
}
