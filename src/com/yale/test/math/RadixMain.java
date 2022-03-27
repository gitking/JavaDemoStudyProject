package com.yale.test.math;

/**
 * 慕课网
 * 二进制的计算规则
 * 1 * 1 = 1 
 * 1 * 0 = 0
 * 0 * 1 = 0
 * 0 * 0 = 0
 * 二进制的缺点:位数太长,可读性差
 * 二进制与八，十六进制之间的进制转换非常直接(每3位二进制数可以转换为1位八进制数，每4位二进制数可以转换为1位十六进制数).
 * java中可以直接声明二(0b开头的是二进制),八(0开头的是八进制),十(默认就是十进制),十六进制(0x开头的是十六进制)
 * 八进制和十六进制的优点：既缩短了二进制数,又保持了二进制的表达特点。
 * 二进制虽然只有0和1俩个数字,但表达信息时会很长。为了简化表达,常用到十六进制。来自微信读书《汇编语言简明教程》钱晓捷
 * 简化了二进制与十进制之间的转换。
 * 特别注意：同一个数的不同进制的表示是完全相同的，例如15=0xf＝0b1111。
 * @author dell
 */
public class RadixMain {
	public static void main(String[] args) {
		for (int i=0;i<256;i++) {
			System.out.print("1");
		}
		System.out.println("换行了");
		//0xff是十六进制的某一个数字,JAVA规定以0x开始的数据表示16进制，0xff换成十六进制为255。
		System.out.println(Integer.toBinaryString(0xff));//0xff代表11111111
		System.out.println();
		/**
		 * 每俩位的十六进制数就是8位的二进制,刚好是一个字节,所以Java的class文件叫字节码
		 * 那为什么一个十六进制数占一个字节呢？
		 * 答:1个字节是8位，二进制8位：xxxxxxxx 范围从00000000－11111111，表示0到255。一位16进制数（用二进制表示是xxxx）
		 * 最多只表示到15（即对应16进制的F），要表示到255,就还需要第二位。所以1个字节＝2个16进制字符，一个16进制位＝0.5个字节。
		 * 同理来说一个8进制数占几个字节？一个二进制数又占几个字节呢？
		 * clas文件为啥又叫字节码呢?
		 * 答:看美团技术团队公众号的文章 Java字节码增强探秘
		 * 
		 * 问：在比特币中，私钥本质上就是一个256位的随机整数。这里的256位整数的意思是？
		 * 答：在比特币中，私钥本质上就是一个256位的随机整数。这里的256位整数的意思是：这里的位是计算机中的术语位(bit)，注意不是我们从小学的数学中的几位数的“位”。在计算机中1位就是1bit,而1字节(byte)等于8位(bit)。1byte=8bit。32byte = 32 x 8 = 256bit(位)。十六进制：0-9加上6个英文字母abcdef刚好就是16个字符，这就是16进制。一个字节(byte)是由八个位(bit)组成的，如果用一个字节来表示一个最大的数字能表示哪个数字呢？那肯定就是11111111（八个二进制的1,在二进制中1就是最大的了，十进制中9就是最大的了）= 256（十进制数字二百五十六）。十六进制FF等于十进制的二百五十六。
		 *  那十进制的二百五十六用十六进制来表示就是FF了。所以一个字节能表示的最大十六进制数是FF(256)，所以我们就规定俩个十六进制的字符占一个字节的空间。
		 *	那私钥本质上就是一个256位的随机整数，那最大的私钥是多少呢？是哪个数字呢？很明显就是二进制的256个1啊：1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111
		 *	1字节(byte)等于8位(bit)，1字节能表示多少个数字呢？答案就是：2的8次方，最多可以表示256个数字。
		 *	那么256位的二进制数，能组合出来多少种可能呢？答案就是：2的256次方。最多可以表示115792089237316195423570985008687907853269984665640564039457584007913129639936个数字。你可以数数这个数字是多大，注意115792089237316195423570985008687907853269984665640564039457584007913129639936这个数字是十进制的，你按   “个十百千万” 去数吧。
		 *  https://www.liaoxuefeng.com/wiki/1207298049439968/1210197145906976?t=1638432725970
		 */
		System.out.println("二进制:" + Integer.toBinaryString(16));//十进制数字16转换成二进制,应该是10000
		System.out.println("十六进制:" + Integer.toHexString(15));//十进制数字15转换成十六进制,应该是F
		System.out.println("八进制:" + Integer.toOctalString(16));//十进制数字16转换成八进制,应该是20
		System.out.println();
		System.out.println(Integer.parseInt("1111", 2));//“111”转换为二进制，应该是15
		System.out.println(Integer.parseInt("1111", 8));//“111”转换为八进制，应该是585
		System.out.println(Integer.parseInt("ff", 16));//“111”转换为十六进制，应该是4369
		int num_8 =010;//以0开头的数字为八进制
		int num_16 =0xff;//以0x开头的数字为十六进制
		int num = 1001;//普通就是十进制
		System.out.println(num_8);
		System.out.println(num_16);
		System.out.println(num);
		System.out.println("**********");
		byte[] byteArr = RadixMain.intToBytes(8143);
		int result = RadixMain.byteToInt(byteArr);
		System.out.println(result);
		
		System.out.println("**********");
		byte[] byteArrSec = RadixMain.longToBytes(9999L);
		long resultLong = RadixMain.byteToLong(byteArrSec);
		System.out.println(resultLong);
		
		System.out.println("**********");
		byte[] byteStr = "字符串转换成字节".getBytes();
		String strByte = new String(byteStr);
		System.out.println(strByte);
	}
	
	/**
	 * 把int类型转换位字节数组
	 */
	public static byte[] intToBytes(int num) {
		byte [] arr = new byte[4];
//		arr[0] = (byte)((int)(num>>0*8)&0xff);
//		arr[1] = (byte)((int)(num>>1*8)&0xff);
//		arr[2] = (byte)((int)(num>>2*8)&0xff);
//		arr[3] = (byte)((int)(num>>3*8)&0xff);
		for (int i = 0; i<arr.length; i++) {
			arr[i] = (byte)((int)(num >> i*8) & 0xff);
		}
		return arr;
	}
	
	/**
	 * 把byte数组转换位int
	 * @param byteArr
	 * @return
	 */
	public static int byteToInt(byte[] byteArr) {
		int result = 0;
		for (int i=0; i<byteArr.length; i++) {
			result += (int)((byteArr[i] & 0xff) << i*8);
		}
		return result;
	}
	
	/**
	 * 把int类型转换位字节数组
	 */
	public static byte[] longToBytes(long num) {
		byte [] arr = new byte[8];
//		arr[0] = (byte)((int)(num>>0*8)&0xff);
//		arr[1] = (byte)((int)(num>>1*8)&0xff);
//		arr[2] = (byte)((int)(num>>2*8)&0xff);
//		arr[3] = (byte)((int)(num>>3*8)&0xff);
		for (int i = 0; i<arr.length; i++) {
			arr[i] = (byte)((long)(num >> i*8) & 0xff);
		}
		return arr;
	}
	
	/**
	 * 把byte数组转换位int
	 * @param byteArr
	 * @return
	 */
	public static long byteToLong(byte[] byteArr) {
		long result = 0;
		for (int i=0; i<byteArr.length; i++) {
			result += (long)((byteArr[i] & 0xff) << i*8);
		}
		return result;
	}
}
