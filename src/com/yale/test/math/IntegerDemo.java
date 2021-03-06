package com.yale.test.math;


/**
 * 我们已经知道，Java的数据类型分两种：
 * 基本类型：byte，short，int，long，boolean，float，double，char
 * 引用类型：所有class和interface类型
 * 装箱和拆箱会影响代码的执行效率，因为编译后的class代码是严格区分基本类型和引用类型的。并且，自动拆箱执行时可能会报NullPointerException：
 * 除了基本数据类型，其他的都是引用数据类型
 * 当你创建像下面这个包装对象时：
 * Integer iWrap = new Integer(42);
 * 它的值永远会是42,包装对象没有setter.你当然还是能够让iWrap引用别的包装对象,但是会产生俩个对象.
 * 包装对象创建后就会无法改变改对象的值!
 * @author dell
 */
public class IntegerDemo {
	public static void main(String[] args) {
		int iy = 3 *4 + 120;
		System.out.println("注意iy的值是在编译成class文件时候就直接变成132了,并不是在实际运行时再计算i的值,这叫JVM编译优化:" + iy);
		/*
		 * 所有的包装类型都是不变类。我们查看Integer的源码可知，它的核心代码如下：
		 * public final class Integer extends Number implements Comparable<Integer> {
		 * 因此，一旦创建了Integer对象，该对象就是不变的。
		 * 对两个Integer实例进行比较要特别注意：绝对不能用==比较，因为Integer是引用类型，必须使用equals()比较：
		 */
		Integer x = 127;
        Integer y = 127;
        Integer m = 99999;
        Integer n = 99999;
        System.out.println("比较俩个数字是否相等:" + Integer.compare(y,m));
        System.out.println("x == y: " + (x==y)); // true
        System.out.println("m == n: " + (m==n)); // false
        System.out.println("x.equals(y): " + x.equals(y)); // true
        System.out.println("m.equals(n): " + m.equals(n)); // true
        /*
         * 仔细观察结果的童鞋可以发现，==比较，较小的两个相同的Integer返回true，较大的两个相同的Integer返回false，这是因为Integer是不变类，
         * 编译器把Integer x = 127;自动变为Integer x = Integer.valueOf(127);，为了节省内存，Integer.valueOf()对于较小的数，
         * 始终返回相同的实例，因此，==比较“恰好”为true，但我们绝不能因为Java标准库的Integer内部有缓存优化就用==比较，必须用equals()方法比较两个Integer。
         * 在编译阶段,若将原始类型int赋值给Integer类型,就会将原始类型自动编译为Integer.valueOf(int);
         * 如果将Integer类型赋值给int类型,则会自动转换调用intValue()方法,如果Integer对象为null,会报空指针异常。(这个自动转换可以通过javap命令来证明)
         * Integer.valueOf()这个方法的源码可以看到会用到IntegerCache.cache缓存,默认值在IntegerCache.low(low是固定的-128)到IntegerCache.high之间
         * IntegerCache.high的值默认是127,不过可以通过设置JVM启动参数-Djava.lang.Integer.IntegerCache.high=200来设置,
         * 也可以通过-XX:AutoBoxCacheMax=<size>设置,这个看源码注释就知道了.《java特种兵》第29页
         * https://www.zhihu.com/question/58735131/answer/307771944 知乎用户 陈亮 的回答
         * 按照语义编程，而不是针对特定的底层实现去“优化”。 
         * 因为Integer.valueOf()可能始终返回同一个Integer实例，因此，在我们自己创建Integer的时候，以下两种方法：
		    方法1：Integer n = new Integer(100);
		    方法2：Integer n = Integer.valueOf(100);
		方法2更好，因为方法1总是创建新的Integer实例，方法2把内部优化留给Integer的实现者去做，即使在当前版本没有优化，也有可能在下一个版本进行优化。
		我们把能创建“新”对象的静态方法称为静态工厂方法。Integer.valueOf()就是静态工厂方法，它尽可能地返回缓存的实例以节省内存。
		 创建新对象时，优先选用静态工厂方法而不是new操作符。 
		 如果我们考察Byte.valueOf()方法的源码，可以看到，标准库返回的Byte实例全部是缓存实例，但调用者并不关心静态工厂方法以何种方式创建新实例还是直接返回缓存的实例。
         */
        //进制转换，Integer类本身还提供了大量方法，例如，最常用的静态方法parseInt()可以把字符串解析成一个整数：
        int x1 = Integer.parseInt("100"); // 100
        System.out.println(x1);
        int x2 = Integer.parseInt("100", 16); // 256,因为按16进制解析
        System.out.println(x2);
        //Integer还可以把整数格式化为指定进制的字符串：
        System.out.println(Integer.toString(100)); // "100",表示为10进制
        System.out.println(Integer.toString(100, 36)); // "2s",表示为36进制
        System.out.println(Integer.toHexString(100)); // "64",表示为16进制
        System.out.println(Integer.toOctalString(100)); // "144",表示为8进制
        System.out.println(Integer.toBinaryString(100)); // "1100100",表示为2进制
        //注意：上述方法的输出都是String，在计算机内存中，只用二进制表示，不存在十进制或十六进制的表示方法。int n = 100在内存中总是以4字节的二进制表示：
        /*
         *  ┌────────┬────────┬────────┬────────┐
			│00000000│00000000│00000000│01100100│
			└────────┴────────┴────────┴────────┘
			我们经常使用的System.out.println(n);是依靠核心库自动把整数格式化为10进制输出并显示在屏幕上，使用Integer.toHexString(n)则通过核心库自动把整数格式化为16进制。
			这里我们注意到程序设计的一个重要原则：数据的存储和显示要分离。
         */
        //Java的包装类型还定义了一些有用的静态变量
        // boolean只有两个值true/false，其包装类型只需要引用Boolean提供的静态字段:
        Boolean t = Boolean.TRUE;
        Boolean f = Boolean.FALSE;
        // int可表示的最大/最小值:
        int max = Integer.MAX_VALUE; // 2147483647
        int min = Integer.MIN_VALUE; // -2147483648
        // long类型占用的bit和byte数量:
        int sizeOfLong = Long.SIZE; // 64 (bits)
        int bytesOfLong = Long.BYTES; // 8 (bytes)
        //最后，所有的整数和浮点数的包装类型都继承自Number，因此，可以非常方便地直接通过包装类型获取各种基本类型：
        
        // 向上转型为Number:
        Number num = new Integer(999);
        // 获取byte, int, long, float, double:
        byte b = num.byteValue();
        System.out.println("999获取的byte:" + b);//这里为什么是-25,应该是把最高位舍去了
        int nm = num.intValue();
        long lnm = num.longValue();
        float fm = num.floatValue();
        double dm = num.doubleValue();
        
        /*
         * 在Java中，并没有无符号整型（Unsigned）的基本数据类型。byte、short、int和long都是带符号整型，最高位是符号位。
         * 而C语言则提供了CPU支持的全部数据类型，包括无符号整型。无符号整型和有符号整型的转换在Java中就需要借助包装类型的静态方法完成。
		         例如，byte是有符号整型，范围是-128~+127，但如果把byte看作无符号整型，它的范围就是0~255。
		         我们把一个负的byte按无符号整型转换为int：
         */
        byte xb = -1;
        byte yb = 127;
        System.out.println("因为byte的-1的二进制表示是11111111，以无符号整型转换后的int就是255:");
        System.out.println("把一个负的byte按无符号整型转换为int：" + Byte.toUnsignedInt(xb)); // 255
        System.out.println("把一个负的byte按无符号整型转换为int：" + Byte.toUnsignedInt(yb)); // 127
        //类似的，可以把一个short按unsigned转换为int，把一个int按unsigned转换为long。
        short sh = 20;
        System.out.println("无符号short:" + Short.toUnsignedInt(sh));
	}
}
