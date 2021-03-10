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
        System.out.println("结果是true:" + (ci == di.intern()));//intern会调用equals方法,去常量池里面找
        System.out.println("结果是true:" + (bi.intern() == di.intern()));
        
        String as = "a" + "b" + "1";//编译优化,再编译成是class文件的时候,这个就变成ab1,"a" "b" "1"都是常量,不是变量,编译器可以大胆做优化
        String bs = "ab1";
        System.out.println(as == bs);
        
        StringTest stObj = new StringTest();
        stObj.testStringPool();//这个方法会把"StringPool"这个常量放进String常量池
        String testStrP = stObj.testStrPool();//这里再运行的时候其实返回的String常量池里面的引用
        String alearStr = "StringPool";//这里也一样,其实返回的String常量池里面的引用
        System.out.println("我认为结果是true:" + (testStrP == alearStr));//所以这里就肯定是true了
        /**
         * 当程序越来越大时,不可避免会有很多String对象.为了安全性和节省空间(例如在手机上执行)的原因,String是不变的
         * 意思是下面的程序实际上会创建出10个String对象("0","01","012",...,"0123456789"),
         * 最后会引用到"0123456789"这个值,但此时会存在10个String.
         * 创建新的String时,Java虚拟机会把它放到称为"String Pool"的特殊存储区中,如果已经出现同值的String,Java虚拟不会重复建立String,只会引用已存在者.
         * 这是因为String是不变的,引用变量无法改变其他参考变量引用到的同一个String值。
         * String Pool不受Garbage Collector管理,因此我们在for循环中建立的10个String有9个是在浪费空间。
         * 这要如何节省内存,如果你不注意的话就不会,但如果你知道String的不变性,就可以利用它来节省空间.如果要执行一堆String操作,则StringBuilder会更合适.
         */
        String st = "0";
        for (int x=0; x<10; x++) {
        	st = st + x;
        }
        
        /*
         * http://www.mytju.com/classcode/tools/encode_utf8.asp UTF-8字符编码转换
         * https://www.qqxiuzi.cn/bianma/zifuji.php 汉字字符编码查询
         * https://home.unicode.org/
         * http://www.chi2ko.com/tool/CJK.htm
         * http://www.ruanyifeng.com/blog/2007/10/ascii_unicode_and_utf-8.html
         * https://cjting.me/2018/07/22/js-and-unicode/
         * 字符串 "noël" 和字符串 "noël" 规整化以后应该相等（他们看起来一样，但是内部表示不一样，一个 6 字节，一个 5 字节，这里涉及到 Unicode 的规整化）
         * 对于大部分编程语言，包括 Ruby，Python，JS，C#，Java 等，上面的问题都无法全部返回正确结果（但是，拥有超强 Unicode 支持的 Elixir(https://elixir-lang.org/) 可以）。
         * 基本概念
         * 首先来看关于字符串的几个基本概念。
         * 字符集（Character Set）：定义了有哪些字符，任何一段字符串，里面的字符都属于某个字符集，比如经典的 ASCII 字符集以及目前最为常用的 Unicode 字符集。
         * 码点（Code Point）：字符集中的每个字符，都有一个唯一的编号，叫做码点，比如 A 在 ASCII 字符集中的码点是 65。
         * 字符编码（Character Encoding）：将字符转换为字节的方式。对于某些字符集，比如 ASCII，字符编码很简单，直接存储码点即可，比如 A，计算机中存储就是 65 的二进制补码，0b01000001。但是对于 Unicode，字符编码就有很多种，后文我们再详细介绍。
         * 编码单元（Code Unit）：UTF16 中的一个概念，每两个字节称为一个编码单元，一个字符可能使用一个或两个编码单元进行编码
         * Unicode
         * Unicode 是一项了不起的发明，这个字符集诞生的初衷很简单，我们需要有一个大的字符集囊括地球上的所有语言中的所有文字。
         * 在 Unicode 诞生之前，每个语言有自己的字符集，比如英语的 ASCII，繁体中文的 Big Five，简体中文的 GB2312 等等。这就使得计算机处理多语言的文档变得十分麻烦，同时，跨语言交流也非常不便，A 语言的文档发给 B 语言的计算机，B 不知道该如何解码，说不定都没有安装 A 语言对应的字符集。
         * Unicode 项目诞生于 1987 年，1.0 版本于 1991 年发布，目前最新版是 11.0。
         * Unicode 字符集目前一共分为 17 个平面（Plane），编号为 0 - 16，每个平面由 16 位构成，也就是每个平面可以编码 2^16 = 65536 个字符。
         * 其中，第一个平面叫做基本平面，BMP, Basic Multilingual Plane，里面编码了最为常用的一些字符。
         * 剩下 16 个平面都叫做补充平面，Supplementary Plane。
         * Unicode 的码点从 0 开始，也就是说，目前，Unicode 的字符码点范围为 0x0000 - 0x10FFFF。当然，这中间很多码点没有定义。
         * Unicode Encoding
         * 有了字符集，剩下的问题就是字符编码，即怎样将码点编码成字节。常见的方式有 UTF32，UTF16 以及 UTF8。我们来分别看看每个编码的方式和优缺点。
         * UTF32
         * 因为目前 Unicode 只用了三个字节就可以完全表示，最为简单的做法是：使用三个字节直接编码码点。
         * 这种思路对应的编码方式是 UTF32，使用四个字节直接编码码点。这里可能有的同学会问，为什么不使用三个字节？有两个原因：
         * 1.为了以后扩充性考虑，虽然目前三个字节够用，但是以后可能不够用
         * 2.计算机处理四字节对齐数据会更快，使用三字节，虽然节省了内存，但是处理起来效率很低。这就和我们编程语言中一般有 int8，int16，int32，但是没有 int24 是一个道理。
         * UTF32 的优点是编码和解码都非常简单。缺点也非常明显：对于英文文本（互联网上绝大部分信息是英文），体积要比 ASCII 大4倍。这是一个无法接受的缺点，因此 UTF32 基本上是不使用的，HTML5 规范就明确规定网页不得使用 UTF32 进行编码。
         * UTF16 && UCS-2
         * UCS-2 (2-byte Universal Character Set)是一个已经废弃的定长编码，始终使用两个字节编码 BMP中 的字符。对于非 BMP 中的字符，UCS-2 无法编码。
         * UTF16 是 UCS-2 的一个扩展，是一个变长编码，结果可能为两个字节，也可能为四个字节。其中每两个字节叫做 Code Unit，编码单元。对于 BMP 中的字符，UTF16 的编码和 UCS-2 一样，使用一个编码单元直接编码字符的码点，对于非 BMP 中的字符，UTF16 使用一个叫做 Surrogate Pair 的技术来进行编码。
         * 在 BMP 中，并不是所有的码点都定义了字符，存在一个空白区，0xD800 - 0xDFFF这个范围内的码点不定义任何字符。
         * 除了 BMP，剩下的码点一共是 0x10FFFF - 0xFFFF = 1048576 = 2^20 个，也就是需要 20 位进行编码。
         * Surrogate Pair 使用两个编码单元来编码一个非 BMP 字符。第一个编码单元的范围为 0xD800 - 0xDBFF，换成二进制为0b1101_10xx_xxxx_xxxx，叫做 Lead Surrogate，正好可以编码 10 位。
         * 第二个编码单元的范围为 0xDC00 - 0xDFFF，换成二进制为 0b1101_11xx_xxxx_xxxx，叫做 Tail Surrogate，正好也可以用来编码 10 位。
         * 这样，通过使用两个编码单元，UTF16 就可以将非 BMP 字符的偏移码点值（减去 0x10000 以后的码点值），使用 Surrogate Pair 进行存储，从而编码非 BMP 字符。同时，由于编码单元的范围都在 BMP 未定义字符的区间中，解码也不会产生任何歧义。
         * 以 emoji 😜 为例，码点为 0x1F61C，减去 0x10000，结果为 0xF61C，换成二进制，填充为 20 位，结果是 0000_1111_0110_0001_1100。
         * 将这 20 位填充到 Surrogate Pair 中，得到的结果是，Lead Surrogate：1101_1000_0011_1101，Tail Surrogate：1101_1110_0001_1100，换成 16 进制便是 0xD83D 0xDE1C，这就是 😜 的 UTF16 编码。
         * UTF8
         * UTF8 是目前使用最多也是最为灵活的一种变长编码，同 UTF16 一样，UTF8 的编码结果是不定长的，在 1 到 4 个字节之间。
         * 具体规则如下，左边为码点范围，右边为二进制编码形式。
         * 0x0000 – 0x007F(0~127,刚好就是ASCII码): 0xxx_xxxx，使用一个字节，编码 7 位。
         * 0x0080 – 0x07FF(128~2047): 110x_xxxx, 10xx_xxxx，使用两个字节，编码 11 位。
         * 0x0800 – 0xFFFF(2048~65535): 1110_xxxx, 10xx_xxxx, 10xx_xxxx，使用三个字节编码 16 位。
         * 0x10000 – 0x1FFFFF(65536~2097151): 1111_0xxx, 10xx_xxxx, 10xx_xxxx, 10xx_xxxx，使用四个字节，编码 21 位
         * 还是以 emoji 😜 为例，码点为 0x1F61C，在区间 0x10000 - 0x1FFFFF 之中，需要使用四个字节进行编码。首先将其转换为二进制，填充为 21 位，结果是 0_0001_1111_0110_0001_1100，
         * 然后将这 21 位按照上述说明填入，结果是 1111_0000，1001_1111，1001_1000，1001_1100，换成 16 进制便是 0xF0 0x9F 0x98 0x9C，这就是 😜 的 UTF8 编码。
         * 严的 Unicode 是4E25（100111000100101），根据上表，可以发现4E25处在第三行的范围内（0000 0800 - 0000 FFFF），
         * 因此严的 UTF-8 编码需要三个字节，即格式是1110xxxx 10xxxxxx 10xxxxxx。然后，从严的最后一个二进制位开始，依次从后向前填入格式中的x，多出的位补0。
         * 这样就得到了，严的 UTF-8 编码是11100100 10111000 10100101，转换成十六进制就是E4B8A5。
         * UTF8 因为它的灵活性，尤其是与 ASCII 的兼容性，目前已经成为事实上的标准。对于编码问题的处理很简单，一律选择使用 UTF8 即可。
         * JS 中的字符串问题和解决方法
         * JS 中的字符串，我们可以认为是 理解 Surrogate Pair 的 UCS-2。
         * 这是因为，JS 中的字符串，我们可以使用 Surrogate Pair 来编码非 BMP 字符，这是 UTF16 的特性，单纯的 UCS-2 是不能理解 Surrogate Pair 的。
         * 但是 JS 中的字符允许无效的 Surrogate Pair，比如 "\uDFFF\uD800"，或者单个 Surrogate，比如 "\uD800" 。因此 JS 中的字符也不是 UTF16，单纯的 UTF16 是不允许上面的字符串的。
         * 另一个问题是，在 JS 看来，什么样的东西是一个字符？因为 JS 是理解 Surrogate Pair 的 UCS-2，因此，在 JS 眼中，一个编码单元是一个字符。
         * 这就给 JS 中的 Unicode 处理带来了很多问题，基本上所有的字符串操作函数在处理非 BMP 字符时都是错误的。
         * length
         * 最基本的问题就是，非 BMP 的字符，由于使用了 Surrogate Pair，含有两个编码单元，导致 JS 认为字符的长度为 2，这显然不是我们要的结果。
         * "😜".length // 2
         * 解决这个问题，可以自己编写一个 strLength 函数，特别处理码点范围在 0xD800 - 0xDFFF 中的字符，当然这比较麻烦，简单的方案是使用 Punycode(https://github.com/bestiejs/punycode.js/)库。
         * var puny = require("punycode")
         * puny.ucs2.decode("😜").length // 1
         * 或者利用 ES6 的新特性：ES6 中的 for of 循环可以正确识别 Unicode，这也就使得和 for of 循环使用相同机制的 ... 操作符可以正确识别 Unicode。
         * // 这个做法很浪费内存
         * [..."😜"].length // 1
         * charAt && charCodeAt
         * charAt 以及 charCodeAt 两个方法用于返回某个偏移量的字符和字符码点，对于非 BMP 字符，返回结果是错的，返回的是 Lead Surrogate 的字符和码点。
         * "😜".charAt(0) // "�"
         * "😜".charCodeAt(0) // 55357
         * 可以使用 ES6 的 String.prototype.codePointAt 和 String.fromCodePoint 两个方法来解决这个问题。
         * "😜".codePointAt(0) // 128540
         * String.fromCodePoint("a😜b".codePointAt(1)) // "😜"
         * Unicode Escape
         * JS 中允许使用 \udddd 以及 \xdd 的形式指定十六进制的数字插入字符。但是对于非 BMP 的字符，使用这个方式插入，需要首先得到 Surrogate Pair 才行，不能直接根据码点插入，比较麻烦。
         * "\u1F61C" // "ὡC"
         * ES6新提供了 \\u{} 方式，使得根据码点插入字符变得非常简单。注意 escape 中填写的都是码点的十六进制值。
         * "\\u{1F61C}" // "😜"
         * Substring, Substr, Slice
         * 这三个函数的行为很类似，参数的含义以及是否允许负数索引上有一些细微的不同。他们同样也都不能正确处理非 BMP 字符。
         * "😜".substr(0, 1) // "�"
         * "😜".substring(0, 1) // "�"
         * "😜".slice(0, 1) // "�"
         * 我们可以利用 ES6 的 for of 实现重新编写这三个函数，下面的实现只用来说明思路，并不完全。
         * String.prototype.newSubstr = function(start, length) {
			  return [...this].slice(start, start + length).join("")
			}
			String.prototype.newSubstring = function(start, end) {
			  return [...this].slice(start, end).join("")
			}
			String.prototype.newSlice = function(start, end) {
			  return [...this].slice(start, end).join("")
			}
			"😜".newSubstr(0, 1) // "😜"
			"😜".newSubstring(0, 1) // "😜"
			"😜".newSlice(0, 1) // "😜"
		 * 其他的一些函数都可以用类似的思路解决，不在赘述了。
		 * Regexp Dot
		 * JS 中的正则，在处理非 BMP 字符时同样存在问题。
		 * 我们首先来看 . 字符。. 字符在正则中的含义是匹配非换行符以外的任意字符。但是在 JS 中，. 只能匹配一个编码单元，对于使用两个编码单元的非 BMP 字符，则无法匹配。
		 * /./.test("😜") // false
		 * 这个问题的解决方案有两个。第一，自己编写范围来匹配非 BMP 字符。
		 * /[\u0000-\uD7FF][\uE000-\uFFFF]|[\uD800-\uDBFF][\uDC00-\uDFFF]/.test("😜") // true
		 * 第二，使用 ES6 引入的 u 标志。
		 * /./u.test("😜") // true
		 * Regexp Range
		 * 第二个问题是正则中的范围。范围中如果使用了非 BMP 字符，JS 会报错。
		 * /[😆-😜]/.test("😜")
		 * Uncaught SyntaxError: Invalid regular expression: /[😆-😜]/: Range out of order in character class
		 * at <anonymous>:1:1
		 * 出错的原因在于 /[😆-😜]/ 在 JS 中等价于 /[\uD83D\uDE06-\uD83D\uDE1C]/，而 \uDE06-\uD83D 会被解释为一个范围，而这个范围的起始值比结束值要大，因此错误。
		 * 解决方法同样有两个。第一，改写正则。
		 * /\uD83D[\uDE06-\uDE1C]/.test("😆") // true
		 * 第二，还是使用 ES6 引入的 u 标志。
		 * /[😆-😜]/u.test("😜") // true
		 * /[\\u{1F606}-\\u{1F61C}]/u.test("😜") // true
		 * Unicode Normalization
		 * 最后，我们来谈谈 Unicode 的规整化。这个问题和 JS 没关系，是 Unicode 字符集本身的问题。
		 * 根据 Unicode 定义，有些字符属于 修饰字符，也就是和别的字符一起出现的时候会修饰别的字符，两个合在一起构成一个我们人眼中的字符。
		 * 比如，ë 这个字符，由两个 Unicode 码点构成，分别是 U+0065 和 U+0308。这两个都是 Unicode 中的合法字符，拥有自己的码点，但他们合在一起的时候，构成一个我们人类眼中的字符。
		 * 同时，在 Unicode 中，还有一个单独的字符 ë，码点为 U+00EB。
		 * ë 和 ë 在我们眼中是一样的字符，但在 Unicode 中却是不同的表现，一个是由两个字符拼接而成，另一个是独立的字符，因此，如果直接比较的话，肯定是不相等的。
		 * "ë" === "ë" // false
		 * 这时候就需要引入规整化，将字符转变为某种特定的形式。Unicode 中定义了四种形式，常用的两种是：
		 * 1.NFD: Normalization Form Canonical Decomposition，将所有的单个的复合字符转换为多个字符拼接而成的形式
		 * 2.NFC: Normalization Form Canonical Composition，将所有的拼接而成的符合字符转换为单个字符的形式
		 * 因此，在比较 Unicode 字符串之前，我们需要对两边的字符串规整化到相同的形式，这样结果才是准确的。ES6 中引入的 String.prototype.normalize 方法可以用于字符串的规整化。
		 * "ë".normalize("NFC") === "ë".normalize("NFC") // true
		 * Reverse the String
		 * 由于存在修饰字符，使得字符串取反变成了一个复杂的操作。
		 * 如果不考虑非 BMP 字符，在 JS 中，对字符串取反的一般方式为 str.split("").reverse().join("")。
		 * 考虑到非 BMP字符，我们可以使用 [...str].reverse().join("")。
		 * 但是，如果含有修饰字符的话，使用 ... 一样无法返回正确的结果。
		 * [..."mañana"].reverse().join("") // "anãnam"
		 * 这里的问题在于对于 "mañana" 使用 ... 产生的字符数组为 ["m", "a", "n", "̃", "a", "n", "a"]，取反以后，修饰字符会跟在 a 的后面，从而产生 ã。
		 * 这个问题需要做手动做一些的处理，在取反之前，将修饰字符和被修饰的字符颠倒一下顺序，然后再取反就行了。我们可以直接使用 esrever(https://github.com/mathiasbynens/esrever)库来处理。
		 * esrever 的 reverse 函数具体实现可以看这里(https://github.com/mathiasbynens/esrever/blob/14b34013dad49106ca08c0e65919f1fc8fea5331/src/esrever.js#L23)。
		 * esrever.reverse("mañana") // "anañam"
         */
        String reStr = "noël";
        System.out.println("反转(逆转,翻转)之前,原始字符串:" + reStr);
        System.out.println("获取前三个字符,正确结果应该是 noë：但是你看:" + reStr.substring(0,3));
        StringBuilder reverStr = new StringBuilder(reStr);
        String reverSe = reverStr.reverse().toString();//反转(逆转,翻转)字符串,逆转字符串
        System.out.println("反转(逆转,翻转)之后,字符串为:" + reverSe);
        String bq = "😸😾";//获取bq字符串 "😸😾" 的长度，正确答案应该是 2,这个字符串是一个emoji表情
        System.out.println("bq的字符串长度为:" + bq.length());
        
	}
	
	public void testStringPool() {
		String str = "StringPool";
	}
	
	public String testStrPool() {
		String str = "StringPool";
		return str;
	}
}
