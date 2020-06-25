package com.yale.test.math;

public class CharTest {
	/**
	 * 字符:各种文字和符号的总称,指各国家文字,标点等等
	 * 计算机只能处理数字,必须把文本转换为数字才能处理
	 * 字符集:包含字库表,编码字符集,字符编码
	 * 码点,码元
	 * ASCII字符集又叫美国标准信息交换代码:基于拉丁字符的编码系统。标准ASCII指定7位二进制数组合表示128中字符信息。
	 * 但是我们知道计算机最小的存储单位是字节,字节是8位的长度,所以标准的ASCII编码,它有一半的空间是空的,所以一些国家盯上了剩余的
	 * 一半空间,就开始扩展了ASCII编码,ASCII编码8位只能表示256中字符,对于中文来说远远不够
	 * GBK是俩个字节16位,不过前面的128都兼容了ASCII编码，
	 * Unicode字符集统一码,万国码:为世界上所有字符都分配了一个唯一的编号,但是Unicode有一个字符,太浪费空间了,
	 * 所以Unicode只有字库表和编码字符表,没有规定字符编码也就是字符的存储方式,最常用的就是UTF-8
	 * 在计算机中我们是以字节为存储单位的
	 * char(2个 字节)使用Unicode字符集,UTF-16,只能表示Unicode编码在65536以内的字符
	 * 使用char类型可以进行基本运算也可以进行位运算,不过结果默认是int类型
	 * boolean只能表示真或假,在编译后会使用1和0来表示,实际上0和1在内存当中只需要一个bit(位)就可以存储了,但是在计算机
	 * 当中最小的内存单位是字节8位,所以存储boolean也是一个字节,在java中虽然定了boolean这种数据类型,但是只对他提供了非常有限的支持,
	 * 在java的虚拟机中没有任何提供给Boolean类型的字节码指令, 所以java语言表达式所操作的boolean值 在编译之后都会使用虚拟机中int类型数据
	 * 来代替,实际上boolean类型占用了4个字节32位,这是单独定义的情况。
	 * 如果将boolean定义在数组当中,虚拟机就将它编译成byte类型也就是1个字节8位.
	 * 既然要节省空间那么为什么单独定义要用int类型来代替呢?这是因为当时的计算机大多是32位,一次处理数据的长度是32,所以在使用int时会更加的高效.
	 * 这也是为什么int,byte,short运算过后结果默认为int的原因之一,这是在存储空间和执行效率上做的取舍
	 * 关于字符编码 ：https://cjting.me/2014/04/24/about-string-encoding/
	 * 不论微信钉钉，我写了个通用消息监控处理机器人https://www.howie6879.cn/post/2019/10_monitor_os_notifications/
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("boolean单独定义占用4个字节,定义在数组中占1个字节");
		System.out.println("课程总结:值得注意的是二进制的小数问题,大多数的十进制小数转换成二进制存储都是无限循环的");
		
		byte num16 = 0xf;//byte是字节,byte变量占用一个字节,0x开头的是十六进制的数字
		System.out.println(num16);//0xf是15	
		
		byte num16b = 0x7f;//byte是字节,byte变量占用一个字节,0x开头的是十六进制的数字
		System.out.println(num16b);//0x7f是127	
		
		//byte num161 = 0x80;//这里会报错,0x80是128,超出了byte能存放的最大整数127,byte是字节,byte变量占用一个字节,
		//System.out.println(num161);//0x80是128	
		
		char num16c = 0xff;//255
		System.out.println((int)num16c);
		
		char num16cc = 0xffff;//65535
		System.out.println((int)num16cc);
		
		char ca = 0xca;
		System.out.println(ca);
		
		char fe = 0xfe;
		System.out.println(fe);
		
		char BA = 0xBA;
		System.out.println(BA);
		
		char BE = 0xBE;
		System.out.println(BE);
		
		char str8 = 56;
		System.out.println("56字符是数字8:" + str8);
		
		String res = Integer.toHexString(15);
		System.out.println("15转换成十六进制:" + res);
		
		int num = 68;
		char c = (char)num;
		System.out.println("char可以将数字转换为英文字母" + c);
		
		//字符类型char表示一个字符。Java的char类型除了可表示标准的ASCII外，还可以表示一个Unicode字符：
		char a = 'A';
        char zh = '中';
        System.out.println(a);
        System.out.println(zh);
        //字符类型char是基本数据类型，它是character的缩写。一个char保存一个Unicode字符：
        //因为Java在内存中总是使用Unicode表示字符，所以，一个英文字符和一个中文字符都用一个char类型表示，它们都占用两个字节。要显示一个字符的Unicode编码，只需将char类型直接赋值给int类型即可：
        int n1 = 'A'; // 字母“A”的Unicodde编码是65
        int n2 = '中'; // 汉字“中”的Unicode编码是20013
        //还可以直接用转义字符\\u+Unicode编码来表示一个字符：
        // 注意是十六进制:
        char c3 = '\u0041'; // 'A'，因为十六进制0041 = 十进制65
        char c4 = '\u4e2d'; // '中'，因为十六进制4e2d = 十进制20013
	}
}
