package com.yale.test.java.demo.string;

import java.util.Date;

/*
 * 在Java中，String是一个引用类型，它本身也是一个class
 * 实际上字符串在String内部是通过一个char[]数组表示的，因此，按下面的写法也是可以的：
 * Java字符串的一个重要特点就是字符串不可变。这种不可变性是通过内部的private final char[]字段，以及没有任何修改char[]的方法实现的。
 * https://www.liaoxuefeng.com/wiki/1252599548343744/1260469698963456
 */
public class StringTest {

	public static void main(String[] args) {
		String s1 = "hello";
		String s2 = "hello";
		//从表面上看，两个字符串用==和equals()比较都为true，但实际上那只是Java编译器在编译期，会自动把所有相同的字符串当作一个对象放入常量池，自然s1和s2的引用就是相同的
		System.out.println(s1 == s2);
		
		String sreg = "A,,B;C ,D";
		sreg.replaceAll("[\\,\\;\\s]+", ","); // "A,B,C,D"
		System.out.println("通过正则表达式替换:" + sreg);
		
		String sp = "A,B,C,D";
		String[] ss = sp.split("\\,"); // {"A", "B", "C", "D"}
		System.out.println("使用split()方法，并且传入的也是正则表达式" + ss);
		//使用索引访问用String的split方法得到的数组时，需做最后一个分隔符后有无内容的检查，否则会有抛IndexOutOfBoundsException的风险。
		String str = "a,b,c,,";
		String[] ary = str.split(",");
		//预期大于3，结果是3
		System.out.println(ary.length);
		
		String[] arr = {"A", "B", "C"};
		String sar = String.join("***", arr); // "A***B***C"
		System.out.println("拼接字符串使用静态方法join()，它用指定的字符串连接字符串数组" + sar);
		
		 /*
		  * 
		    %s：显示字符串；
		    %d：显示整数；
		    %x：显示十六进制整数；参数必须是byte,short,int,long, BigInteger
		    %f：显示浮点数。
		    %c: character
		    占位符还可以带格式，例如%.2f表示显示两位小数。如果你不确定用啥占位符，那就始终用%s，因为%s可以显示任何数据类型。要查看完整的格式化语法，请参考JDK文档。
		  https://docs.oracle.com/en/java/javase/14/docs/api/java.base/java/util/Formatter.html#syntax
		  */
		 String sfo = "Hi %s, your score is %d!";
	     //System.out.println(s.formatted("Alice", 80));
	     System.out.println(String.format("Hi %s, your score is %.2f!", "Bob", 59.5));
	     //FloatDemo.java里面也有关于格式化的代码
	     //%是占位符',d'代表参数要使用的格式化的方式，%,d:这代表以十进制整数带有逗号的方式来表示。
	     System.out.println(String.format("%,d", 1000000000));//格式化的结果为1,000,000,000
	     //%.2f：这代表以小数点后俩位的方式来格式化此浮点数
	     System.out.println(String.format("I have %.2f bugs to fix.", 476578.09876));
	     System.out.println(String.format("I have %.2f bugs to fix.", 476578.09476));
	     //%,.2f代表整数部分以有逗号的形式表示,小数部分以俩位来格式化
	     System.out.println(String.format("I have %,.2f bugs to fix.", 476578.09476));
	     
	     //如果你要输出像下面这样的字符串:The rank is 20,456,654 out of 100,567,890,24
	     int one = 20456654;
	     double two = 100567890.248907;
	     System.out.println(String.format("The rank is %,d out of %,.2f", one, two));
	     /*
	      * 格式化说明最多会有5个部分(不包括%符号)。下面的[]符号里面都是选择性的项目,因此只有%与type是必须的.
	      * 格式化说明的顺序是有规定的,必须要以这个顺序来指定.
	      * %[argument number][flags][width][.precision]type
	      * [argument number],如果要格式化的参数超过一个以上,可以在这里指定是哪一个
	      * [flags]特定类型的特定选项,例如数字要加逗号或正负号
	      * [width]最小的字符数,注意这不是总数,输出可以超过此宽度,若不足则会主动补零
	      * [.precision]精确度,注意前面有个圆点符号
	      * type 一定要指定的类型
	      * 在格式化指令中一定要给类型,如果还要指定其他项目,要把类型type放在最后,类型修饰符有十几种(这不包括日期和时间,它们有自己的一组)
	      * 但大部分时间你会使用到%d和%f,且通常你会对%f加上精确度指示来设定所需要的小数长度。
	      * 《Head First Java》第297页
	      */
	     System.out.println(String.format("%,6.1f", 42.000));
	     System.out.println("%.3f会强制输出3位的小数");
	     System.out.println(String.format("%.3f", 42.0));
	     
	     System.out.println("%x hexadecimal 参数必须是byte,short,int, long,显示十六进制整数");
	     System.out.println(String.format("%x", 42));
	     
	     //%c参数必须是byte,short,int, long,ASCII的42代表"*"号
	     System.out.println(String.format("%c", 42));
	     
	     //%tc完整的日期与时间
	     System.out.println(String.format("%tc", new Date()));
	     //%tr只有时间
	     System.out.println(String.format("%tr", new Date()));
	     //%tA %tB %td 周 月  日
	     Date now = new Date();
	     System.out.println(String.format("%tA, %tB %td", now, now, now));
	     //同上不用重复给参数,"<"这个符号是个特殊的指示,用来告诉格式化程序重复利用之前用过的参数《Head First Java》
	     System.out.println(String.format("%tA, %<tB %<td", now));

		"".isEmpty(); // true，因为字符串长度为0
		"  ".isEmpty(); // false，因为字符串长度不为0
		//"  \n".isBlank(); // true，因为只包含空白字符
		//" Hello ".isBlank(); // false，因为包含非空白字符
		
		//另一个strip()方法也可以移除字符串首尾空白字符。它和trim()不同的是，类似中文的空格字符\u3000也会被移除：
		//JDK1.9才有
		//"\u3000Hello\u3000".strip(); // "Hello"
		//" Hello ".stripLeading(); // "Hello "
		//" Hello ".stripTrailing(); // " Hello"

		/**
		 * 因为字符串使用双引号"..."表示开始和结束，那如果字符串本身恰好包含一个"字符怎么表示？例如，"abc"xyz"，
		 * 编译器就无法判断中间的引号究竟是字符串的一部分还是表示字符串结束。这个时候，我们需要借助转义字符\：
		 * 因为\是转义字符，所以，两个\\表示一个\字符：
		 * 常见的转义字符包括：
		    \" 表示字符"
		    \' 表示字符'
		    \\ 表示字符\
		    \n 表示换行符
		    \r 表示回车符
		    \t 表示Tab
		    \\u#### 表示一个Unicode编码的字符
		 */
		String s = "abc\"xyz"; // 包含7个字符: a, b, c, ", x, y, z
		String sS = "ABC\n\u4e2d\u6587"; // 包含6个字符: A, B, C, 换行符, 中, 文
		System.out.println(sS);
		//如果我们要表示多行字符串，使用+号连接会非常不方便：
		String sST = "first line \n"
		         + "second line \n"
		         + "end";
		/*
		 * 从Java 13开始，字符串可以用"""..."""表示多行字符串（Text Blocks）了。举个例子：
		 * String s = """
                SELECT * FROM
                  users
                WHERE id > 100
                ORDER BY name DESC
                """;
     System.out.println(s);
     上述多行字符串实际上是5行，在最后一个DESC后面还有一个\n。如果我们不想在字符串末尾加一个\n，就需要这么写：
     String s = """ 
           SELECT * FROM
             users
           WHERE id > 100
           ORDER BY name DESC""";
           
           还需要注意到，多行字符串前面共同的空格会被去掉，即：
           String s = """
...........SELECT * FROM
...........  users
...........WHERE id > 100
...........ORDER BY name DESC
...........""";
用.标注的空格都会被去掉。
如果多行字符串的排版不规则，那么，去掉的空格就会变成这样：
String s = """
.........  SELECT * FROM
.........    users
.........WHERE id > 100
.........  ORDER BY name DESC
.........  """;
即总是以最短的行首空格为基准。
最后，由于多行字符串是作为预览特性（Preview Language Features）实现的，编译的时候，我们还需要给编译器加上参数：
javac --source 14 --enable-preview Main.java
		 */
		//Java的字符串除了是一个引用类型外，还有个重要特点，就是字符串不可变。考察以下代码：
		String sF = "hello";
        System.out.println(sF); // 显示 hello
        sF = "world";
        System.out.println(sF); // 显示 world
        //观察执行结果，难道字符串sF变了吗？其实变的不是字符串，而是变量sF的“指向”。
        /*
         * 执行String s = "hello";时，JVM虚拟机先创建字符串"hello"，然后，把字符串变量s指向它：
         * 紧接着，执行s = "world";时，JVM虚拟机先创建字符串"world"，然后，把字符串变量s指向它：
         * 原来的字符串"hello"还在，只是我们无法通过变量s访问它而已。因此，字符串的不可变是指字符串内容不可变。
         */
        String test = "hello";
        String tt = test;
        test = "world";
        System.out.println(tt); // test是"hello"还是"world"?
        
        // 请将下面一组int值视为字符的Unicode码，把它们拼成一个字符串：
        //https://www.liaoxuefeng.com/wiki/1252599548343744/1255938912141568
        //练习题
        int a = 72;
        int b = 105;
        int c = 65281;
        
        // FIXME:
        String result = "" + a + b + c;
        System.out.println("" + result);
        
        
        /*
         * 字符编码

在早期的计算机系统中，为了给字符编码，美国国家标准学会（American National Standard Institute：ANSI）制定了一套英文字母、数字和常用符号的编码，它占用一个字节，编码范围从0到127，最高位始终为0，称为ASCII编码。例如，字符'A'的编码是0x41，字符'1'的编码是0x31。

如果要把汉字也纳入计算机编码，很显然一个字节是不够的。GB2312标准使用两个字节表示一个汉字，其中第一个字节的最高位始终为1，以便和ASCII编码区分开。例如，汉字'中'的GB2312编码是0xd6d0。

类似的，日文有Shift_JIS编码，韩文有EUC-KR编码，这些编码因为标准不统一，同时使用，就会产生冲突。

为了统一全球所有语言的编码，全球统一码联盟发布了Unicode编码，它把世界上主要语言都纳入同一个编码，这样，中文、日文、韩文和其他语言就不会冲突。

Unicode编码需要两个或者更多字节表示，我们可以比较中英文字符在ASCII、GB2312和Unicode的编码：

英文字符'A'的ASCII编码和Unicode编码：

         ┌────┐
ASCII:   │ 41 │
         └────┘
         ┌────┬────┐
Unicode: │ 00 │ 41 │
         └────┴────┘

英文字符的Unicode编码就是简单地在前面添加一个00字节。

中文字符'中'的GB2312编码和Unicode编码：

         ┌────┬────┐
GB2312:  │ d6 │ d0 │
         └────┴────┘
         ┌────┬────┐
Unicode: │ 4e │ 2d │
         └────┴────┘

那我们经常使用的UTF-8又是什么编码呢？因为英文字符的Unicode编码高字节总是00，包含大量英文的文本会浪费空间，所以，出现了UTF-8编码，它是一种变长编码，用来把固定长度的Unicode编码变成1～4字节的变长编码。通过UTF-8编码，英文字符'A'的UTF-8编码变为0x41，正好和ASCII码一致，而中文'中'的UTF-8编码为3字节0xe4b8ad。

UTF-8编码的另一个好处是容错能力强。如果传输过程中某些字符出错，不会影响后续字符，因为UTF-8编码依靠高字节位来确定一个字符究竟是几个字节，它经常用来作为传输编码。

在Java中，char类型实际上就是两个字节的Unicode编码。如果我们要手动把字符串转换成其他编码，可以这样做：

byte[] b1 = "Hello".getBytes(); // 按系统默认编码转换，不推荐
byte[] b2 = "Hello".getBytes("UTF-8"); // 按UTF-8编码转换
byte[] b2 = "Hello".getBytes("GBK"); // 按GBK编码转换
byte[] b3 = "Hello".getBytes(StandardCharsets.UTF_8); // 按UTF-8编码转换

注意：转换编码后，就不再是char类型，而是byte类型表示的数组。

如果要把已知编码的byte[]转换为String，可以这样做：

byte[] b = ...
String s1 = new String(b, "GBK"); // 按GBK转换
String s2 = new String(b, StandardCharsets.UTF_8); // 按UTF-8转换

始终牢记：Java的String和char在内存中总是以Unicode编码表示。

对于不同版本的JDK，String类在内存中有不同的优化方式。具体来说，早期JDK版本的String总是以char[]存储，它的定义如下：

public final class String {
    private final char[] value;
    private final int offset;
    private final int count;
}

而较新的JDK版本的String则以byte[]存储：如果String仅包含ASCII字符，则每个byte存储一个字符，否则，每两个byte存储一个字符，这样做的目的是为了节省内存，因为大量的长度较短的String通常仅包含ASCII字符：

public final class String {
    private final byte[] value;
    private final byte coder; // 0 = LATIN1, 1 = UTF16

对于使用者来说，String内部的优化不影响任何已有代码，因为它的public方法签名是不变的。
https://www.liaoxuefeng.com/wiki/1252599548343744/1260469698963456
         */
        
        /*
         * 这里String对象ai的值"a"是一个常量,JVM在编译时会把他放到一个常量池里面.在HotSpotVM7及之前的版本,常量池都是
         * 存在在PermGen(永久代)这个版块中的,但是当你new String的时候会通过变量再+常量的方式来生成String对象的时候这时String对象是
         * 放在堆上面的,intern这个方法的作用：当调用intern方法时intern会去常量池里面用equals方法查找是否有内容一样的字符串,如果有直接
         * 把常量池里面的这个字符串地址返回出去,如果没有则会在常量池里面创建一个然后将常量池的这个地址返回出去.调用intern方法时,都会得到常量池中对应String的引用
         * 所以当你new 了一个字符串,然后又调用了intern这个方法,那么你的这个字符串会在堆和常量池里面都有一个相同的字符串.
         * 常量池里面没有重复的字符串,JVM会保证全局唯一
         * 注意:jdk1.7字符串常量池不是存放在PermGen(永久代)中,而是存在堆当中.JDK1.6字符串常量池是存放在PermGen(永久代)中的。
         * 永久代会注销吗？会的,在发生FULLGC的时候,如果没有任何引用指向它就会被注销掉.
         */
        String ai = "a";
        String bi = ai + "b";
        String ci = "ab";//
        String di = new String(bi);
        
        System.out.println("结果是false:" + (bi == ci));
        System.out.println("结果是false:" + (di == ci));
        System.out.println("结果是true:" + (ci == di.intern()));
        System.out.println("结果是true:" + (bi.intern() == di.intern()));
	}
}
