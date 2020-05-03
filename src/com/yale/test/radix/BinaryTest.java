package com.yale.test.radix;

/**
 * 二进制的计算规则
 * 1 * 1 = 1 
 * 1 * 0 = 0
 * 0 * 1 = 0
 * 0 * 0 = 0
 * 二进制的缺点:位数太长,可读性差
 * 二进制与八，十六进制之间的进制转换非常直接(每3位二进制数可以转换为1位八进制数，每4位二进制数可以转换为1位十六进制数).
 * java中可以直接声明二(0b开头的是二进制),八(0开头的是八进制),十(默认就是十进制),十六进制(0x开头的是十六进制)
 * 八进制和十六进制的优点：既缩短了二进制数,又保持了二进制的表达特点
 * 简化了二进制与十进制之间的转换。
 * @author dell
 */
public class BinaryTest {
	public static void main(String[] args) {
		int bin = 0b1100010;//0b开头的是二进制,英文字母b x是不区分大小写的,jvm底层存储的其实还是二进制
		int oct = 0142;
		int hex = 0x62;//0x开头的是十六进制,英文字母b x是不区分大小写的,jvm底层存储的其实还是二进制
		int dec = 98;
		System.out.println("2:" + bin);
		System.out.println("8:" + oct);
		System.out.println("16:" + hex);
		System.out.println("10:" + dec);
		
		System.out.println("**********************************************");
		System.out.println();
		
		System.out.println("java中的进制转换方法:");
		System.out.println("转换二进制显示:" + Integer.toBinaryString(dec));
		System.out.println("转换为八进制显示:" + Integer.toOctalString(dec));
		System.out.println("转换为十六进制显示:" + Integer.toHexString(dec));
		System.out.println("**********************************************");
		System.out.println();

		System.out.println("自由度更高的转换方法:Integer.toString(int i, int radix)方法:");
		System.out.println("radix参数的可使用范围是2-36,为什么最高是36？因为英文字母是26个,加上10个阿拉伯数字,最高就是36进制了");
		System.out.println("Integer.toString(int i, int radix)方法:转换为二进制显示:" + Integer.toString(dec, 2));
		System.out.println("Integer.toString(int i, int radix)方法:转换为八进制显示:" + Integer.toString(dec, 8));
		System.out.println("Integer.toString(int i, int radix)方法:转换为十六进制显示:" + Integer.toString(dec, 16));

		System.out.println("**********************************************");
		System.out.println();

		System.out.println("将字符串转换为数字:Integer.parseInt(String s, int radix)方法:");
		System.out.println("将字符串转换为数字:Integer.valueOf(String s, int radix)方法:");
		System.out.println("Integer.parseInt将二进制的字符串转换为十进制:" + Integer.parseInt("1100010", 2));
		System.out.println("Integer.parseInt将八进制的字符串转换为十进制:" + Integer.parseInt("142", 8));
		System.out.println("Integer.parseInt将十六进制的字符串转换为十进制:" + Integer.parseInt("62", 16));
		
		System.out.println("valueOf方法里面用的就是parseInt" + Integer.valueOf("1100010", 2));


		
	}
}
