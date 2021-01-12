package com.yale.test.math;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
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
		System.out.println("min方法返回俩个数字最小的那一个:" + Math.min(18.44, 55));
		
		System.out.println("求绝对值:" + Math.abs(-7.8));
		System.out.println("计算√x：" + Math.sqrt(2));
		System.out.println("计算ex次方：" + Math.exp(2));
		System.out.println("计算以e为底的对数：" + Math.log(2));
		System.out.println("计算以10为底的对数：" + Math.log10(2));
		/*
		 * 三角函数：
		 * Math.sin(3.14); // 0.00159...
			Math.cos(3.14); // -0.9999...
			Math.tan(3.14); // -0.0015...
			Math.asin(1.0); // 1.57079...
			Math.acos(1.0); // 0.0

		 */

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
		
		System.out.println("Random是随机数类,Random用来创建伪随机数。所谓伪随机数，是指只要给定一个初始的种子，产生的随机数序列是完全一样的。");
		System.out.println("有童鞋问，每次运行程序，生成的随机数都是不同的，没看出伪随机数的特性来。");
		System.out.println("这是因为我们创建Random实例时，如果不给定种子，就使用系统当前时间戳作为种子，因此每次运行时，种子不同，得到的伪随机数序列就不同。");
		Random dom = new Random();
		for (int x=0;x<10; x++) {
			System.out.print(dom.nextInt(100) + ",");//100是上限,输出的随机最大值是99
		}
		
		//如果我们在创建Random实例时指定一个种子，就会得到完全确定的随机数序列：
		Random rd = new Random(12345);
        for (int i = 0; i < 10; i++) {
            System.out.println("Main方法每次运行随机序列是完全一样的:" + rd.nextInt(100));
        }
        /*
         * 有伪随机数，就有真随机数。实际上真正的真随机数只能通过量子力学原理来获取，而我们想要的是一个不可预测的安全的随机数，SecureRandom就是用来创建安全的随机数的：
         * SecureRandom无法指定种子，它使用RNG（random number generator）算法。JDK的SecureRandom实际上有多种不同的底层实现，
         * 有的使用安全随机种子加上伪随机数算法来产生安全的随机数，
         * 有的使用真正的随机数生成器。实际使用的时候，可以优先获取高强度的安全随机数生成器，如果没有提供，再使用普通等级的安全随机数生成器：
         * SecureRandom的安全性是通过操作系统提供的安全的随机种子来生成随机数。这个种子是通过CPU的热噪声、读写磁盘的字节、网络流量等各种随机事件产生的“熵”。
		 * 在密码学中，安全的随机数非常重要。如果使用不安全的伪随机数，所有加密体系都将被攻破。因此，时刻牢记必须使用SecureRandom来产生安全的随机数。
		 * 需要使用安全随机数的时候，必须使用SecureRandom，绝不能使用Random！ 
		 * 
		 * https://github.com/openjdk/jdk/blob/jdk8-b120/jdk/src/share/classes/sun/security/provider/SunEntries.java
		 * SecureRandom内部用到了sun.security.provider.SecureRandom,而SecureRandom在大量获取随机数的时候有性能问题见:perfma连接
		 * https://club.perfma.com/article/2056948
		 * 堆栈显示，阻塞在：void sun.security.provider.SecureRandom.engineNextBytes(byte[])上面，这就是一个经典的问题，Java Random
		 * 涉及到两种随机数 seed 生成方式，一种是"file:/dev/random"，另一种是"file:/dev/urandom"，通过设置系统属性java.security.egd指定，默认是"file:/dev/random"
		 * 两种 Random 原理与解决在 Linux 4.8 之前：和在 Linux 4.8 之后：
		 * 在熵池不够用的时候，默认的"file:/dev/random"会阻塞，"file:/dev/urandom"不会，继续用。对于我们来说，"file:/dev/urandom"够用，
		 * 所以通过-Djava.security.egd=file:/dev/./urandom设置系统属性，使用 urandom 来减少阻塞。
		 * 注意：jvm参数值为/dev/./urandom而不是/dev/urandom，这里是jdk的一个bug引起。https://blog.51cto.com/leo01/1795447
		 * bug产生的原因请注意下面SeedGenerator第四行源码，如果java.security.egd参数指定的是file:/dev/random或者file:/dev/urandom，则调用了无参的NativeSeedGenerator构造函数，而无参的构造函数将默认使用file:/dev/random 。openjdk的代码和hotspot的代码已经不同，openjdk在后续产生随机数的时候没有使用这个变量。
		 * https://github.com/openjdk/jdk/blob/jdk8-b120/jdk/src/share/classes/sun/security/provider/SunEntries.java
         */
        SecureRandom sr = null;
        try {
            sr = SecureRandom.getInstanceStrong(); // 获取高强度安全随机数生成器
        } catch (NoSuchAlgorithmException e) {
            sr = new SecureRandom(); // 获取普通的安全随机数生成器
        }
        byte[] buffer = new byte[16];
        sr.nextBytes(buffer); //用安全随机数填充buffer
        System.out.println("真随机数:" + Arrays.toString(buffer));
        System.out.println("真随机数:" + sr.nextInt(100));
		
		/**
		 * Math.random()方法用的也是random生成随机数的,值范围是0-1,随机最大小数位0.9999999999
		 * 【强制】注意 Math.random() 这个方法返回是double类型，注意取值的范围 0≤x<1（能够取到零值，注意除零异常），
		 * 如果想获取整数类型的随机数，不要将x放大10的若干倍然后取整，直接使用Random对象的nextInt或者nextLong方法。
		 * 《阿里巴巴Java开发手册（泰山版）.pdf》
		 * 前面我们使用的Math.random()实际上内部调用了Random类，所以它也是伪随机数，只是我们无法指定种子。
		 */
		System.out.print("x的范围是0 <= x < 1：" + Math.random() + ",");//100是上限,输出的随机最大值是99
		//如果我们要生成一个区间在[MIN, MAX)的随机数，可以借助Math.random()实现，计算如下：
		double xrd = Math.random(); // x的范围是[0,1)
        double min = 10;
        double max = 50;
        double yd = xrd * (max - min) + min; // y的范围是[10,50)
        long nl = (long) yd; // n的范围是[10,50)的整数
        System.out.println(yd);
        System.out.println(nl);
        
        /*
         * 有些童鞋可能注意到Java标准库还提供了一个StrictMath，它提供了和Math几乎一模一样的方法。这两个类的区别在于，由于浮点数计算存在误差，不同的平台（例如x86和ARM）计算的结果可能不一致（指误差不同），
         * 因此，StrictMath保证所有平台计算结果都是完全相同的，而Math会尽量针对平台优化计算速度，所以，绝大多数情况下，使用Math就足够了。
         */
        double sm = StrictMath.random();
		
		System.out.println();
		
		/**
		 * 下面是阿里云魔乐科技的练习题
		 * https://edu.aliyun.com/clouder/exam/new/15
		 */
		int num = 50;
		num = num ++ * 2;
		System.out.println("num 的值为:" + num);
		
		int numb = 50;
		numb = numb ++;
		System.out.println("numb 的值为:" + numb);
		
		int maxNum = 2147483647;
		maxNum +=2;
		System.out.println("maxNum的值是多少?" + maxNum);
		System.out.println("int的最小值是多少?" + Integer.MIN_VALUE);
		System.out.println("int的最大值是多少?" + Integer.MAX_VALUE);
		
		int maxNumSec = 2147483640;
		maxNumSec +=9L;
		System.out.println("maxNumSec的值是多少?" + maxNumSec);

		int maxInt = Integer.MAX_VALUE;
		long temp = maxInt + 2L;
		System.out.println("temp 的值为:" + temp);
		System.out.println("maxInt 的值为:" + maxInt);

		
		boolean flag = 10%2 == 1 && 10/3==0 && 1/0==0;
		System.out.println(flag ? "mldn":"yootk");
		float sd = 1.0f;//float变量后面必须带f
		double dou = 1.0;//java中小数默认为double类型的
		
		int i = 1;
		int j = i++;
		if ((i == (++j)) && ((i++)==j)) {
			i+=j;
		}
		System.out.println("i的值是多少?" + i);
		
		int ii = 1;
		int jj = ii++;
		if ((ii ==++jj) && (ii++==jj)) {
			ii+=jj;
		}
		System.out.println("ii的值是多少?" + ii);
		
		int x =10;
		double y = 20.2;
		long z = 10L;
		
		long tempL = (long)(y * z);
		System.out.println("tempL的值是多少?" + tempL);
		
		double tempDD = y * z;//long * double 结果是double
		System.out.println("tempDD的值是多少?" + tempDD);
		
		String tempSt = "" + y * z;
		System.out.println("tempSt的值是多少? 这里先算乘法:" + tempSt);
		
		String str = "" + x + (y * z);// "" + 10 这个变成字符串10了,然后先算乘法202.0
		System.out.println("str的值是多少?" + str);
		
		//另外需要注意的是,枚举本身还支持switch判断,也就是说switch按照时间进度来讲,最初只支持int和char,到了JDK1.5的时候支持了枚举,到了
		//jdk1.7的时候支持了String
		//switch这种开关语句有个重要的特点:如果你在编写case的时候没有加上break;则会在满足的case语句之后一直执行,直到遇见break;或全部结束.所以要求case必须加上break;
		char c = 'A';
		int charNum = 10;
		switch (c) {
			case 'B':
				charNum++;
				System.out.println("11");
			case 'A':
				charNum++;
				System.out.println("12");//这里会执行
			case 'Y':
				charNum++;
				System.out.println("13");//这里也会执行
				break;
			default:
				System.out.println("14");
				charNum--;
		}
		System.out.println("charNum的值是多少?:" + charNum);
		
		int option = 2;
        switch (option) {
	        case 1:
	            System.out.println("Selected 1");
	            break;
	        case 2://如果有几个case语句执行的是同一组语句块，可以这么写：
	        case 3:
	            System.out.println("Selected 2, 3");
	            break;
	        default:
	            System.out.println("Not selected");
	            break;
        }
        //使用switch语句时，只要保证有break，case的顺序不影响程序逻辑：但是仍然建议按照自然顺序排列，便于阅读。
        switch (option) {
	        case 3:
	            System.out.println("Selected 3");
	            break;
	        case 2:
	            System.out.println("Selected 2");
	            break;
	        case 1:
	            System.out.println("Selected 1");
	            break;
        }
        
        /*
		 * 表面上看，上面的while循环是一个死循环，但是，Java的int类型有最大值，达到最大值后，再加1会变成负数，结果，意外退出了while循环。
		 */
		int sum = 0;
        int n = 1;
        while (n > 0) {
            sum = sum + n;
            n ++;
        }
        System.out.println(n); // -2147483648
        System.out.println(sum);
        // 不设置结束条件:
        for (int ie=0; ; ie++) {
        	System.out.println("需要自己");
            break;
        }
        // 不设置结束条件和更新语句:
        for (int ie=0; ;) {
            System.out.println("需要自己");
            break;
        }
        // 什么都不设置:
        for (;;) {
            System.out.println("需要自己");
            break;
        }
        
        /*
         * 使用switch时，如果遗漏了break，就会造成严重的逻辑错误，而且不易在源代码中发现错误。
         * 从Java 12开始，switch语句升级为更简洁的表达式语法，使用类似模式匹配（Pattern Matching）的方法，保证只有一种路径会被执行，并且不需要break语句：
         * String fruit = "apple";
        switch (fruit) {
        case "apple" -> System.out.println("Selected apple");
        case "pear" -> System.out.println("Selected pear");
        case "mango" -> {
            System.out.println("Selected mango");
            System.out.println("Good choice!");
        }
        default -> System.out.println("No fruit selected");
        }
        注意新语法使用->，如果有多条语句，需要用{}括起来。不要写break语句，因为新语法只会执行匹配的语句，没有穿透效应。
        很多时候，我们还可能用switch语句给某个变量赋值。例如：
        int opt;
		switch (fruit) {
		case "apple":
		    opt = 1;
		    break;
		case "pear":
		case "mango":
		    opt = 2;
		    break;
		default:
		    opt = 0;
		    break;
		}
		使用新的switch语法，不但不需要break，还可以直接返回值。把上面的代码改写如下：
		String fruit = "apple";
        int opt = switch (fruit) {
            case "apple" -> 1;
            case "pear", "mango" -> 2;
            default -> 0;
        }; // 注意赋值语句要以;结束
        System.out.println("opt = " + opt);//这样可以获得更简洁的代码。
        大多数时候，在switch表达式内部，我们会返回简单的值。
但是，如果需要复杂的语句，我们也可以写很多语句，放到{...}里，然后，用yield返回一个值作为switch语句的返回值：
String fruit = "orange";
        int opt = switch (fruit) {
            case "apple" -> 1;
            case "pear", "mango" -> 2;
            default -> {
                int code = fruit.hashCode();
                yield code; // switch语句返回值
            }
        };
        System.out.println("opt = " + opt);
        https://www.liaoxuefeng.com/wiki/1252599548343744/1259541030848864
         */
		
		long lm = 100;
		//int lin = lm + 2;//程序错误
		
		String _na = "";
		//String 100Book = "";
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
