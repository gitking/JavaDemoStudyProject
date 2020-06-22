package com.yale.test.math;

public class StringTest {

	public static void main(String[] args) {
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
	}
}
