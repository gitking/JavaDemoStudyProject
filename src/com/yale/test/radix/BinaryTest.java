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
		int oct = 0142;//0开头的是八进制
		/**
		 * 每俩位的十六进制数就是8位的二进制,刚好是一个字节,所以Java的class文件叫字节码
		 */
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
		System.out.println("*************************************");
		System.out.println("什么是位运算:位运算就是直接对整数在内存中的二进制位进行操作,位运算实际上是对二进制进行的操作,而不是数学运算,位运算分为俩种:逻辑操作和位移操作");

		System.out.println("比特(bit):信息量的最小单位,单位是小写b,bit只能表示二进制的1或者0");
		System.out.println("字节(byte):表示信息的最小单位,单位是大写B,1byte=8bit,计算机中所有的数据都是以字节为单位存储的.");
		System.out.println("机器数:将数据的首位设定为符号位:,0为正,1为负,机器数的数值大小受到机器字长的限制,不像你在纸上想写多长的数字就写多长的数字");
		System.out.println("机器数的形式:原码,反码,补码,原码就是加入符号位的就叫原码");
		System.out.println("反码就是二进制数值全部取反了,好像只针对负数这样子");
		System.out.println("补码就是反码的基础上加1得到的结果,好像只针对负数这样子");
		System.out.println("补码计算方式:正数补码=反码=原码,负数补码=反码+1");
		System.out.println("在如今绝大多数的系统和语言当中记录数值都是以补码的方式存放的,为什么要用补码这么不直观的记录方式呢？实际上就是为了简化计算机的计算过程以提高效率,");
		System.out.println("用原码表示正负数,如果负数参与运算,我们就需要先识别他的符号位,然后再进行运算.而用补码就可以省略这一步,补码的符号位是可以直接参与运算的,并且可以得到正确的结果.");
		System.out.println("看个例子:");
		/**
		 * 整数                                      补码
		 * 1 		0000 0001
		 * -1		1111 1111
		 * 5		0000 0101
		 * -5		1111 1011
		 * 负数相加: -1 + -5 = (1111 1111) + (1111 1011)都是补码在参与运算
		 * 				  =  1 1111 1010(注意这里是9位,比一个字节多了一位,)
		 * 				  =  1111 1010 (直接把前面多出的部分删除,保留8位，刚好就是-6)
		 * 				  = -6
		 * 正负相加(实际就是减法): (+1) + (-1) = (0000 0001) + (1111 1111)都是补码在参与运算
		 * 								= (1 0000 0000)
		 * 								= (0000 00000)
		 * 							    = 0
		 * 使用补码就是为了方便实现运算过程,可以将符号位也参与到运算当中
		 */
		System.out.println("位运算分为俩种:逻辑操作和位移操作:");
		System.out.println("逻辑操作就是一下几种: java运算符: &按位与,|按位或,^按位异域,~按位取反");
		System.out.println("位移操作就是一下几种: java运算符: <<左移, >>右移, >>>无符号右移");

		System.out.println("按位与运算:&,定义:参加运算的俩个数据,按二进制位进行‘于’运算,与运算:俩个数都为 1,则结果为1");
		System.out.println("&位与运算:1&1 = 1, 1&0 = 0, 0&1 =0, 0&0 = 0 ");
		System.out.println("示例:14&3即0000 1110 & 0000 0011 = 0000 0010 = 整数2,因此14&3=2,特点:①清零特定位②获取特定位");
		System.out.println("注意,二进制的尾数如果是1的话,他就是奇数");
		
		System.out.println("按位或运算:|,定义:参加运算的俩个数据,按二进制位进行‘或’运算,或运算:俩个数有一个为 1,则结果为1");
		System.out.println("|位或运算:1|1 = 1, 1|0 = 1, 0|1 =1, 0|0 = 0 ");
		System.out.println("示例:14|3即0000 1110 | 0000 0011 = 0000 1111 = 整数15,因此14|3=15,特点:①将特定位设为1");
		
		System.out.println("按位异或运算:^,定义:参加运算的俩个数据,按二进制位进行‘异或’运算,异或运算:俩个数不同,则结果为1");
		System.out.println("|位或运算:1^1 = 0, 1^0 = 1, 0^1 =1, 0^0 = 0 ");
		System.out.println("示例:14|3即0000 1110 ^ 0000 0011 = 0000 1101 = 整数13,因此14^3=13,特点:①与自身异或得0 ②与同一个数连续异或得自身");
		
		System.out.println("按位取反运算:~,定义:对一个二进制数按位取反,0变1,1变0");
		System.out.println("~取反运算:~1 = 0, ~0 = 1 ");
		System.out.println("示例:~14即~0000 1110 得 1111 0001 = 整数-15,因此~14=-15,这里再次重申计算机当中整数都是以补码的方式存放的");
		
		
		System.out.println("<<左位移运算:<<,定义:将一个二进制数向左移动对应的位数,而这个移动是不包括符号位的");
		System.out.println("规则:符号位不变,右侧低位补0,左侧高位舍弃 ");
		System.out.println("示例:14<<1 即0000 1110 << 1 得 0001 1100 = 整数28,因此14<<1=28,");
		System.out.println("<<左位移的特点就是相当于乘以这个数的2的位移次方:m<<n = m x 2的n次方(在最高位不为1的情况下),");
		
		System.out.println(">>右位移运算:>>,定义:将一个二进制数向右移动对应的位数,而这个移动是不包括符号位的");
		System.out.println("规则:符号位不变,右侧低位舍弃,左侧高位正数补0,负数补1 ");
		System.out.println("示例:14>>1 即0000 1110 >> 1 得 0000 0111 = 整数7,因此14>>1=7,");
		System.out.println("示例:-14>>2 即1111 0010 >> 2 得 1111 1100 = 整数-4,因此-14>>2=-4,");
		System.out.println(">>右位移的特点就是相当于除以这个数的2的位移次方:m<<n = m ÷ 2的n次方(低位为1时,精度会丢失,并非为完全整除的情况),");
		
		
		System.out.println(">>>无符号右位移运算:>>>,无符号右位移是包括了符号位的,正数无符号位移操作没有什么区别,主要是在负数");
		System.out.println("规则:右侧低位舍弃,左侧高位补0 ");
		System.out.println("示例:-14>>>2 即1111 0010 >>> 2  得 0011 1100 = 整数60,因此-14>>>2=60,");
		System.out.println("这里要说明:数据规定存储的不同字节,负数位移得到的结果也是不同的,不同位数,负数的结果不同");
		System.out.println("示例:这次用俩个字节来存储-14得的结果就大不相同,-14>>>2 即1111 1111 1111 0010 >>> 2 得 0011 1111 1111 1100= 整数16380,");
		
		System.out.println("无符号右位移只是在操作的时候不去考虑符号位,而在最终结果的识别方面,它还是以补码的方式存储,也是有符号位的,这一点一定要注意");
		
		System.out.println("java中的byte(1字节),shor(2字节),int(字节),long(8字节),java中的整数类型都使用二进制的补码存储");
		
		System.out.println("大端模式与小端模式,多字节数据的俩种顺序:大端:高位字节放在低地址,低位字节放在高地址.小端:低位字节放在低地址,高位字节放在高地址.");
		System.out.println("java语言中默认使用的是大端模式");
	}
}
