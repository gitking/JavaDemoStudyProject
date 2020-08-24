package com.yale.test.math;

/**
 * 可以用来自动创建文档的注释,一般写在类或者方法上面
 * 这种特殊的多行注释需要写在类和方法的定义处，可以用于自动创建文档。
 * 还有一种特殊的多行注释，以**开头，以*结束，如果有多行，每行通常以星号开头：
 * Java程序对格式没有明确的要求，多几个空格或者回车不影响程序的正确性，但是我们要养成良好的编程习惯，注意遵守Java社区约定的编码格式。
 * 那约定的编码格式有哪些要求呢？其实我们在前面介绍的Eclipse IDE提供了快捷键Ctrl+Shift+F（macOS是⌘+⇧+F）帮助我们快速格式化代码的功能，Eclipse就是按照约定的编码格式对代码进行格式化的，所以只需要看看格式化后的代码长啥样就行了。具体的代码格式要求可以在Eclipse的设置中Java-Code Style查看。
 */

/* 多行注释开始
注释内容
注释结束 */

/**
 * 不写public，也能正确编译，但是这个类将无法从命令行执行。
 * 基本数据类型是CPU可以直接进行运算的类型。
 * Java定义的这些基本数据类型有什么区别呢？要了解这些区别，我们就必须简单了解一下计算机内存的基本结构。
 * 计算机内存的最小存储单元是字节（byte），一个字节就是一个8位二进制数，即8个bit。它的二进制表示范围从00000000~11111111，换算成十进制是0~255，换算成十六进制是00~ff。
 * 内存单元从0开始编号，称为内存地址。每个内存单元可以看作一间房间，内存地址就是门牌号。
 *    0   1   2   3   4   5   6  ...
	┌───┬───┬───┬───┬───┬───┬───┐
	│   │   │   │   │   │   │   │...
	└───┴───┴───┴───┴───┴───┴───┘
 * 一个字节是1byte，1024字节是1K，1024K是1M，1024M是1G，1024G是1T。一个拥有4T内存的计算机的字节数量就是：
 * 4T = 4 x 1024G
 *    = 4 x 1024 x 1024M
 *    = 4 x 1024 x 1024 x 1024K
 *    = 4 x 1024 x 1024 x 1024 x 1024
 *    = 4398046511104+
 *    不同的数据类型占用的字节数不一样。我们看一下Java基本数据类型占用的字节数：
	       ┌───┐
	  byte │   │
	       └───┘
	       ┌───┬───┐
	 short │   │   │
	       └───┴───┘
	       ┌───┬───┬───┬───┐
	   int │   │   │   │   │
	       └───┴───┴───┴───┘
	       ┌───┬───┬───┬───┬───┬───┬───┬───┐
	  long │   │   │   │   │   │   │   │   │
	       └───┴───┴───┴───┴───┴───┴───┴───┘
	       ┌───┬───┬───┬───┐
	 float │   │   │   │   │
	       └───┴───┴───┴───┘
	       ┌───┬───┬───┬───┬───┬───┬───┬───┐
	double │   │   │   │   │   │   │   │   │
	       └───┴───┴───┴───┴───┴───┴───┴───┘
	       ┌───┬───┐
	  char │   │   │
	       └───┴───┘
	byte恰好就是一个字节，而long和double需要8个字节(64位bit)。
 * 对于整型类型，Java只定义了带符号的整型，因此，最高位的bit表示符号位（0表示正数，1表示负数）。各种整型能表示的最大范围如下：
    byte：-128 ~ 127
    short: -32768 ~ 32767
    int: -2147483648 ~ 2147483647
    long: -9223372036854775808 ~ 9223372036854775807
 * 我们一行一行地分析代码执行流程：
 * 执行int n = 100;，该语句定义了变量n，同时赋值为100，因此，JVM在内存中为变量n分配一个“存储单元”，填入值100：
 *        n
	      │
	      ▼
	┌───┬───┬───┬───┬───┬───┬───┐
	│   │100│   │   │   │   │   │
	└───┴───┴───┴───┴───┴───┴───┘
	执行n = 200;时，JVM把200写入变量n的存储单元，因此，原有的值被覆盖，现在n的值为200：
	      n
	      │
	      ▼
	┌───┬───┬───┬───┬───┬───┬───┐
	│   │200│   │   │   │   │   │
	└───┴───┴───┴───┴───┴───┴───┘
 * 执行int x = n;时，定义了一个新的变量x，同时对x赋值，因此，JVM需要新分配一个存储单元给变量x，并写入和变量n一样的值，结果是变量x的值也变为200：
 *        n           x
	      │           │
	      ▼           ▼
	┌───┬───┬───┬───┬───┬───┬───┐
	│   │200│   │   │200│   │   │
	└───┴───┴───┴───┴───┴───┴───┘
 * 执行x = x + 100;时，JVM首先计算等式右边的值x + 100，结果为300（因为此刻x的值为200），然后，将结果300写入x的存储单元，因此，变量x最终的值变为300：
 *        n           x
	      │           │
	      ▼           ▼
	┌───┬───┬───┬───┬───┬───┬───┐
	│   │200│   │   │300│   │   │
	└───┴───┴───┴───┴───┴───┴───┘
 * 可见，变量可以反复赋值。注意，等号=是赋值语句，不是数学意义上的相等，否则无法解释x = x + 100。
 * 定义变量时，要遵循作用域最小化原则，尽量将变量定义在尽可能小的作用域，并且，不要重复使用变量名。
 * https://www.liaoxuefeng.com/wiki/1252599548343744/1255883729079552
 * @author dell
 */
public class DataTypeDemo {

	public static void main(String[] args) {
		/* 整数的数值表示不但是精确的，而且整数运算永远是精确的，即使是除法也是精确的，因为两个整数相除只能得到结果的整数部分：
		 * 特别注意：整数的除法对于除数为0时运行时将报错，但编译不会报错。
		 * 要特别注意，整数由于存在范围限制，如果计算结果超出了范围，就会产生溢出，而溢出不会出错，却会得到一个奇怪的结果：
		 */
		int i = 2147483647;//声明变量的同时给它一个初始值,变量必须先定义后使用
		int defa;//不写初始值,就相当于给它指定了默认值,int类型默认总是0
        int i2 = -2147483648;
        int i3 = 2_000_000_000; // 加下划线更容易识别
        System.out.println("加下划线也行:" + i3);
        int i4 = 0xff0000; // 十六进制表示的16711680
        int i5 = 0b1000000000; // 二进制表示的512
        long l = 9000000000000000000L; // long型的结尾需要加L
        
        //特别注意：同一个数的不同进制的表示是完全相同的，例如15=0xf＝0b1111。
        
        /*
         * 要解释上述结果，我们把整数2147483640和15换成二进制做加法：
		  0111 1111 1111 1111 1111 1111 1111 1000
		+ 0000 0000 0000 0000 0000 0000 0000 1111
		-----------------------------------------
		  1000 0000 0000 0000 0000 0000 0000 0111
		 * 由于最高位计算结果为1，因此，加法结果变成了一个负数。
		 * https://www.liaoxuefeng.com/wiki/1252599548343744/1255888634635520
         */
        int x = 2147483640;
        int y = 15;
        int sum = x + y;
        System.out.println("奇怪的结果:" + sum); // -2147483641
        
        /*
         * 注意++写在前面和后面计算结果是不同的，++n表示先加1再引用n，n++表示先引用n再加1。
         * 不建议把++运算混入到常规运算中，容易自己把自己搞懵了。
         */
        int numC = 10;
        numC++;
        
        /**
         * 浮点型
		 * 浮点类型的数就是小数，因为小数用科学计数法表示的时候，小数点是可以“浮动”的，如1234.5可以表示成12.345x102，也可以表示成1.2345x103，所以称为浮点数。
		 * 浮点数可表示的范围非常大，float类型可最大表示3.4x1038，而double类型可最大表示1.79x10308。
         */
        float f1 = 3.14f;//对于float类型，需要加上f后缀。
        float f2 = 3.14e38f; // 科学计数法表示的3.14x10^38
        System.out.println("科学计数法的小数:" + f2);
        double d = 1.79e308;
        double d2 = -1.79e308;
        double d3 = 4.9e-324; // 科学计数法表示的4.9x10^-324
        
        //Java语言对布尔类型的存储并没有做规定，因为理论上存储布尔类型只需要1 bit，但是通常JVM内部会把boolean表示为4字节整数。
        boolean is = true;
        
        
      //字符类型char表示一个字符。Java的char类型除了可表示标准的ASCII外，还可以表示一个Unicode字符：
		char a = 'A';
	    char zh = '中';
	    System.out.println(a);
	    System.out.println(zh);
	    
	    //常量，定义变量的时候，如果加上final修饰符，这个变量就变成了常量：
	    final double PI = 3.14;//PI是一个常量
	    double r = 5.0;
	    double area = PI * r * r;
	    //PI = 300;//编译错误,常量在定义时进行初始化后就不可再次赋值，再次赋值会导致编译错误。
	    /*
	     * 常量的作用是用有意义的变量名来避免魔术数字（Magic number），例如，不要在代码中到处写3.14，而是定义一个常量。
	     * 如果将来需要提高计算精度，我们只需要在常量的定义处修改，例如，改成3.1416，而不必在所有地方替换3.14。
	     * 根据习惯，常量名通常全部大写。
	     */
	    
	   /* 除了上述基本类型的变量，剩下的都是引用类型。例如，引用类型最常用的就是String字符串：
	    * 引用类型的变量类似于C语言的指针，它内部存储一个“地址”，指向某个对象在内存的位置，后续我们介绍类的概念时会详细讨论。
	    * jdk 14 增加var关键字,有些时候，类型的名字太长，写起来比较麻烦。例如：
	    * StringBuilder sb = new StringBuilder();
	    * 这个时候，如果想省略变量类型，可以使用var关键字：
	    * 可以换成 var sb = new StringBuilder(); 编译器会根据赋值语句自动推断出变量sb的类型是StringBuilder。对编译器来说，语句：var sb = new StringBuilder();
	    * 实际上会自动变成：StringBuilder sb = new StringBuilder(); 因此，使用var定义变量，仅仅是少写了变量类型而已。
	    */
	}
}
