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
